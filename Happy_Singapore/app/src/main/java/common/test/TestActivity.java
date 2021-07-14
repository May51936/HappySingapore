package common.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashMap;

import Utils.HTTPUtils;
import Utils.URLUtils;
import common.network.NewsReq;
import module.activity.BaseActivity;
import module.url.NewsRsp;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;

public class TestActivity extends BaseActivity {

    private static final String TAG = TestActivity.class.toString();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        NewsReq news = new NewsReq();
        news.init();
        news.sendReq();
        news.getOneNews(0);
    }
}
