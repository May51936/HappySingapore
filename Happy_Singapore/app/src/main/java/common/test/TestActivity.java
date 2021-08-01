package common.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.WangTianyu.HappySingapore.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import Utils.HTTPUtils;
import Utils.URLUtils;
import common.nav.changeLanguage;
import common.network.CovidReq;
import common.network.LTAReq;
import common.network.NewsReq;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import module.activity.BaseActivity;
import module.data.Picture;
import module.url.NewsRsp;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class TestActivity extends BaseActivity {

    private static final String TAG = TestActivity.class.toString();
    private Handler handler;
    private Bundle bundle = new Bundle();
    private Observable<ResponseBody> observable;
    private String language;
    private ;
//    private ArrayList<NewsRsp> array;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setLanguage();
        setContentView(R.layout.news_layout);
        getCovid();
//        getNews();

//        LTAReq test = new LTAReq(TestActivity.this);
//        test.init();
//        test.sendReq();

//        try {
//            news.getOneNews(0).get_pic();
//            pic.getFromURL(news.getOneNews(0).get_pic());
//            ImageView view = (ImageView) findViewById(R.id.test);
//            view.setImageBitmap(pic.getPic());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void getNews(){
        //新闻接口
        NewsReq news = new NewsReq(TestActivity.this);
        news.init();
        news.sendReq();
    }

    public void getCovid(){
        //疫情接口
        CovidReq covid = new CovidReq(TestActivity.this);
        covid.init("/");
        covid.sendReq();
    }

    public void reqTest(String url){
        //测试URL用接口
        Retrofit retrofit = new RetrofitModule().setURL(url);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.test("");
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
                            Log.i(TAG, responseBody.string());
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

    public void setLanguage(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        Locale locale = null;
        Configuration configuration = getResources().getConfiguration();
        SharedPreferences sp = getSharedPreferences("SP_data", Context.MODE_PRIVATE);
        String language = sp.getString("language", null);
        Log.i(TAG, language);
        Log.i(TAG, configuration.locale.toLanguageTag());
        if (!language.equals("Chinese") && configuration.locale.toLanguageTag().equals("zh-CN")){
            locale = Locale.ENGLISH;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                configuration.setLocale(locale);
            } else {
                configuration.locale = locale;
            }
            getResources().updateConfiguration(configuration, metrics);
            //重新启动Activity
            Intent intent = new Intent(this, TestActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
