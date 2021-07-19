package common.network;

import android.app.Activity;
import android.util.Log;

import com.bumptech.glide.RequestManager;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Utils.HTTPUtils;
import Utils.URLUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import module.url.NewsRsp;
import module.url.RequestModule;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class CovidReq extends RequestModule {
    private Observable<ResponseBody> observable;
    private Retrofit retrofit;
    private Activity mActivity;
    private String newLocalCases;
    private String newTotalCases;
    private String newImportCases;
    private String currentCases;
    private String totalCases;


    private static final String TAG = CovidReq.class.toString();

    public CovidReq (Activity activity){
        this.mActivity = activity;
    }

    public void init(String path){
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
        int start = returnData.indexOf("<div class=\"slider slider-multiple invert latest-news-slider\">");
        int end = returnData.indexOf("<div data-category=\"Speeches\">");
        if (start == -1)
            Log.e(TAG, "cannot find start");
        if (end == -1)
            Log.e(TAG, "cannot find end");
        String data = returnData.substring(start,end);
        String[] array = data.split("<div data-category=\"Press Releases\">");
        String new_info = null;
        for (int i = 0; i < array.length; i++)
            if (array[i].indexOf("On Local Covid-19 Situation") != -1) {
                new_info = array[i].substring(array[i].indexOf("details"), array[i].indexOf("\">"));
                break;
            }
        Log.i(TAG, new_info);
            reqForDetails(new_info);

    }

    public void reqForDetails(String path){
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
                        int label = originalData.indexOf("there are");
                        newLocalCases = originalData.substring(label+10, label+15);
                        newLocalCases = newLocalCases.split(" ")[0];
                        label = originalData.indexOf("imported cases");
                        newImportCases = originalData.substring(label-6,label-1);
                        newImportCases = newImportCases.split(" ")[1];
                        newTotalCases = String.valueOf(Integer.parseInt(newLocalCases)+Integer.parseInt(newImportCases));
                        Log.i(TAG, newLocalCases);
                        Log.i(TAG, newImportCases);
                        Log.i(TAG, newTotalCases);
//                        reqForTotalInfo();
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

//    public void reqForTotalInfo(){
//        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.moh);
//        HTTPUtils service = retrofit.create(HTTPUtils.class);
//        observable = service.getCovid("map?hl=en-US&mid=%2Fm%2F06t2t&gl=US&ceid=US%3Aen");
//        observable.subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ResponseBody>() {
//            @Override
//            public void onSubscribe(@NotNull Disposable d) {
//                Log.d(TAG, "Connection Start...");
//            }
//
//            @Override
//            public void onNext(@NotNull ResponseBody responseBody) {
//                try {
//                    Log.i(TAG, responseBody.string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                Log.e(TAG, "Successful data");
//            }
//
//            @Override
//            public void onError(@NotNull Throwable e) {
//                Log.d(TAG, "Failed connection");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "Successful connection");
//            }
//        });
//}


}
