package common.test;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import retrofit2.http.GET;

public class TestActivity extends BaseActivity {

    private static final String TAG = TestActivity.class.toString();
    private Handler handler;
    private Bundle bundle = new Bundle();
//    private ArrayList<NewsRsp> array;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_layout);
        NewsReq news = new NewsReq(TestActivity.this);
        news.init();
        news.sendReq();
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
