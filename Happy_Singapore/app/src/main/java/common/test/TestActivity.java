package common.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.WangTianyu.HappySingapore.R;
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
import module.data.Picture;
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
        setContentView(R.layout.test);
        NewsReq news = new NewsReq(TestActivity.this);
        Picture pic = new Picture();
        news.init();
        news.sendReq();
//        news.test();
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
}
