package common.network;

import android.app.Activity;
import android.content.Context;
import android.speech.RecognitionService;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.ResponseCache;
import java.nio.CharBuffer;
import java.util.ArrayList;

import Utils.HTTPUtils;
import Utils.URLUtils;
import module.url.NewsRsp;
import module.url.RequestModule;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsReq extends RequestModule{
    private Call<ResponseBody> call;
    private int max;
    private Activity mActivity;
    private ArrayList<NewsRsp> array = new ArrayList<NewsRsp>();

    private static final String TAG = NewsReq.class.toString();

    public NewsReq(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void sendReq(){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //处理网络请求返回的数据
                Log.i(TAG, "Successful req: " + response.body().toString());
                try {
                    processJSONData(response.body().string());
                    Log.e(TAG, "Successful data");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Fail req: " + t.toString());
            }
        });
    }

    public void init(){
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.news);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        call = service.getNews();
    }


    public void processJSONData(String json) throws IOException {
        JsonArray data;
        NewsRsp rsp;
        JsonObject returnData = new JsonParser().parse(json).getAsJsonObject();
        max = returnData.get("totalResults").getAsInt();
        data = returnData.getAsJsonArray("articles");
        Log.i(TAG, data.toString());
        for(int i = 0; i < data.size(); i++){
            rsp = setOneNews(i, data);
            array.add(rsp);
        }
    }

    public NewsRsp setOneNews(int num, JsonArray data) throws IOException {
        NewsRsp news = new NewsRsp();
        JsonObject obj = data.get(num).getAsJsonObject();
        news.set_date(obj.get("publishedAt").getAsString());
        news.set_title(obj.get("title").getAsString());
        news.set_name(obj.get("source").getAsJsonObject().get("name").getAsString());
        news.set_url(obj.get("url").getAsString());
        news.set_pic(obj.get("urlToImage").getAsString());
        return news;
    }

    public NewsRsp getOneNews(int num){
        return array.get(num);
    }

    public void test(){

    }

}
