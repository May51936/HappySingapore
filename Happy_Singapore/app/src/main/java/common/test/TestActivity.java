package common.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.news);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        Call<ResponseBody> call = service.getNews();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //处理网络请求返回的数据
                Log.e("彳亍", "测试Retrofit");
                try {
                    Log.e("彳亍", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("彳亍", t.toString());
            }
        });
    }
}
