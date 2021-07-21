package common.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.WangTianyu.HappySingapore.R;
import com.bumptech.glide.RequestManager;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.HTTPUtils;
import Utils.URLUtils;
import common.nav.changeLanguage;
import common.test.TestActivity;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import module.url.NewsRsp;
import module.url.RequestModule;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class CovidReq extends RequestModule {
    private Observable<ResponseBody> observable;
    private Retrofit retrofit;
    private Activity mActivity;
    String regEx = "[^0-9]";
    Pattern p = Pattern.compile(regEx);
    Matcher m = null;
    private String newLocalCases;
    private String newTotalCases;
    private String newImportCases;
    private String newDeadCases;
    private String newRecoveredCases;
    private String activeCases;
    private String totalCases;
    private String recoverdCases;
    private String deadCases;
    private String new_info = null;
    private String date = null;


    private static final String TAG = CovidReq.class.toString();

    public CovidReq (Activity activity){
        this.mActivity = activity;
    }

    public void init(String path){
        //初始化Retrofit
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.COVID_19);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.getCovid(path);
    }

    public void sendReq(){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        Log.d(TAG, "Connection Start...");
                    }

                    @Override
                    public void onNext(@NotNull ResponseBody responseBody) {
                        try {
                            //回调处理数据
                            processingData(responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "Successful data");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d(TAG, "Failed connection");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Successful connection");
                    }
                });
    }

    public void processingData(String returnData){
        //爬取H5，处理接取数据
        int start = returnData.indexOf("<div class=\"slider slider-multiple invert latest-news-slider\">");
        int end = returnData.indexOf("<div data-category=\"Speeches\">");
        if (start == -1)
            Log.e(TAG, "cannot find start");
        if (end == -1)
            Log.e(TAG, "cannot find end");
        //获取最新疫情资讯文章
        String data = returnData.substring(start,end);
        String[] array = data.split("<div data-category=\"Press Releases\">");
        for (int i = 0; i < array.length; i++)
            if (array[i].indexOf("On Local Covid-19 Situation") != -1) {
                new_info = array[i].substring(array[i].indexOf("details"), array[i].indexOf("\">"));
                date = array[i].substring(array[i].indexOf("<span>")+6, array[i].indexOf("</span>"));
                Log.e(TAG,date);
                break;
            }
        Log.i(TAG, new_info);
        reqForDetails(new_info);
    }

    public void reqForDetails(String path){
        //爬取最新疫情资讯文章
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.COVID_19);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.getCovid(path);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        Log.d(TAG, "Connection Start...");
                    }

                    @Override
                    public void onNext(@NotNull ResponseBody responseBody) {
                        String originalData = null;
                        try {
                            originalData = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //根据文章特性，获取疫情数据
                        int label = originalData.indexOf("there are");
                        newLocalCases = originalData.substring(label+10, label+15);
                        m = p.matcher(newLocalCases);
                        newLocalCases = m.replaceAll("").trim();
                        label = originalData.indexOf("imported cases");
                        newImportCases = originalData.substring(label-6,label-1);
                        m = p.matcher(newImportCases);
                        newImportCases = m.replaceAll("").trim();
                        newTotalCases = String.valueOf(Integer.parseInt(newLocalCases)+Integer.parseInt(newImportCases));
                        Log.i(TAG, newLocalCases);
                        Log.i(TAG, newImportCases);
                        Log.i(TAG, newTotalCases);
//                        reqForTotalInfo();
                        Log.e(TAG, "Successful data");
                        reqForTotalInfo();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d(TAG, "Failed connection");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Successful connection");
                        changeLanguage language = new changeLanguage(mActivity, TestActivity.class);
                        View view = mActivity.findViewById(R.id.changeLanguage);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                language.changeAppLanguage();
                            }
                        });
                    }
                });
    }

    public void reqForTotalInfo(){
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.moh);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.getCovidSummary("");
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                Log.d(TAG, "Connection Start...");
            }

            @Override
            public void onNext(@NotNull ResponseBody responseBody) {
                try {
                    getSummary(responseBody.string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "Successful data");
            }

            @Override
            public void onError(@NotNull Throwable e) {
                Log.d(TAG, "Failed connection");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "Successful connection");
                initUI();
                //同步调用
                NewsReq news = new NewsReq(mActivity);
                news.init();
                news.sendReq();
            }
        });
    }

    public void getSummary(@NotNull String returnData){
        String temp = returnData.substring(returnData.indexOf("contentPlaceholder_C072_Col01\""), returnData.indexOf("<div id=\"ContentPlaceHolder_contentPlaceholder_C073_Col00\""));
        recoverdCases = temp.substring(temp.indexOf("<span"), temp.indexOf("span>"));
        recoverdCases = recoverdCases.substring(recoverdCases.indexOf("<b>"), recoverdCases.lastIndexOf("</b>"));
        m = p.matcher(recoverdCases);
        recoverdCases = m.replaceAll("").trim();
        temp = returnData.substring(returnData.indexOf("contentPlaceholder_C072_Col00\""), returnData.indexOf("<div id=\"ContentPlaceHolder_contentPlaceholder_C072_Col01\""));
        activeCases = temp.substring(temp.indexOf("<span"), temp.indexOf("span>"));
        activeCases = activeCases.substring(activeCases.indexOf("<b>"), activeCases.lastIndexOf("</b>"));
        m = p.matcher(activeCases);
        activeCases = m.replaceAll("").trim();
        temp = returnData.substring(returnData.indexOf("contentPlaceholder_C073_Col03\""), returnData.indexOf("Vaccination Data"));
        deadCases = temp.substring(temp.indexOf("<span"), temp.lastIndexOf("span>"));
        deadCases = deadCases.substring(deadCases.indexOf("<b>"), deadCases.lastIndexOf("</b>"));
        m = p.matcher(deadCases);
        deadCases = m.replaceAll("").trim();
        totalCases = String.valueOf(Integer.parseInt(recoverdCases)+Integer.parseInt(deadCases)+Integer.parseInt(activeCases));
        Log.i(TAG, totalCases);
        Log.i(TAG, activeCases);
        Log.i(TAG,recoverdCases);
        Log.i(TAG, deadCases);
        storeData();

    }

    public void storeData(){
        SharedPreferences sp = mActivity.getSharedPreferences("SP_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        初始化
//        editor.putString("recoveredCases", String.valueOf(Integer.parseInt(recoverdCases)-6));
//        editor.putString("deadCases", deadCases);
//        editor.putString("totalCases", totalCases);
//        editor.putString("date", "18 Jul 2021");
//        editor.putString("newRecover", "0");
//        editor.putString("newDead", "0");
//        editor.apply();
        String ori_recovered = sp.getString("recoveredCases", "-1");
        String ori_dead = sp.getString("deadCases", "-1");
        if (!date.equals(sp.getString("date", "0"))){
            editor.putString("recoveredCases", recoverdCases);
            editor.putString("deadCases", deadCases);
            editor.putString("totalCases", totalCases);
            editor.putString("activeCases", activeCases);
            editor.putString("date", date);
            newRecoveredCases = String.valueOf(Integer.parseInt(recoverdCases)-Integer.parseInt(ori_recovered));
            newDeadCases = String.valueOf(Integer.parseInt(deadCases)-Integer.parseInt(ori_dead));
            editor.putString("newRecover", newRecoveredCases);
            editor.putString("newDead", newDeadCases);
            editor.commit();
            Log.i(TAG, "Update");
        }
        else{
            newRecoveredCases = sp.getString("newRecover", "0");
            newDeadCases = sp.getString("newDead", "0");
            Log.i(TAG, "Not update");
        }
        Log.i(TAG, date);

    }

    public void initUI(){
        TextView recovered = (TextView) mActivity.findViewById(R.id.recoveredCases);
        TextView dead = (TextView) mActivity.findViewById(R.id.deadCases);
        TextView total = (TextView) mActivity.findViewById(R.id.totalCases);
        TextView newRecovered = (TextView) mActivity.findViewById(R.id.new_recoveredCases);
        TextView newDead = (TextView) mActivity.findViewById(R.id.new_deadCases);
        TextView newTotal = (TextView) mActivity.findViewById(R.id.new_totalCases);
        recovered.setText(recoverdCases);
        dead.setText(deadCases);
        total.setText(totalCases);
        newRecovered.setText("+" + newRecoveredCases);
        newDead.setText("+" + newDeadCases);
        newTotal.setText("+" + newTotalCases);
        View view = (View) mActivity.findViewById(R.id.Covid_news);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URLUtils.COVID_19 + new_info));
                mActivity.startActivity(intent);
            }
        });
    }


}
