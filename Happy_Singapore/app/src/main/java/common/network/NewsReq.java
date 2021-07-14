package common.network;

import android.app.Activity;
import android.speech.RecognitionService;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.ResponseCache;

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
    private JsonArray data;
    private Call<ResponseBody> call;
    private int max;

    private static final String TAG = NewsReq.class.toString();

    public void sendReq(){
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //处理网络请求返回的数据
                Log.e(TAG, "Successful req: " + response.body().toString());
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

    public void processJSONData(String json){
        JsonObject returnData = new JsonParser().parse(json).getAsJsonObject();
        max = returnData.get("totalResults").getAsInt();
        data = returnData.getAsJsonArray("articles");
        Log.i(TAG, data.toString());
    }

    public NewsRsp getOneNews(int num){
        NewsRsp news = new NewsRsp();
        JsonObject obj = data.get(num).getAsJsonObject();
        news.set_date(obj.get("publishedAt").getAsString());
        news.set_title(obj.get("title").getAsString());
        news.set_name(obj.get("source").getAsJsonObject().get("name").getAsString());
        news.set_url(obj.get("url").getAsString());
        news.set_pic(obj.get("urlToImage").getAsString());
        return news;
    }
}
