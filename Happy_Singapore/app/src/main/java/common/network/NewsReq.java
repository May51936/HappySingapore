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
    private ArrayList<NewsRsp> news = null;

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
        JsonObject returnData = new JsonParser().parse(json).getAsJsonObject();
        max = returnData.get("totalResults").getAsInt();
        data = returnData.getAsJsonArray("articles");
        Log.i(TAG, data.toString());
        Log.i(TAG, ""+returnData.size());
        Log.i(TAG, ""+returnData.getAsString().length());
        for(int i = 0; i < returnData.size(); i++){
            Log.i(TAG, ""+getOneNews(i, data).get_date());
        }
    }

    public NewsRsp getOneNews(int num, JsonArray data) throws IOException {
        NewsRsp news = new NewsRsp();
        JsonObject obj = data.get(num).getAsJsonObject();
        news.set_date(obj.get("publishedAt").getAsString());
        news.set_title(obj.get("title").getAsString());
        news.set_name(obj.get("source").getAsJsonObject().get("name").getAsString());
        news.set_url(obj.get("url").getAsString());
        news.set_pic(obj.get("urlToImage").getAsString());
        return news;
    }

    public void save (String text, String name) throws IOException{
        BufferedWriter writer = null;
        FileOutputStream output = null;
        try {
            File file = new File(mActivity.getCacheDir() + File.separator + name);
            output = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(writer != null){
                writer.close();
            }
            if(output != null){
                output.close();
            }
        }
        Log.i(TAG, "Successfully saved in " + name);
    }

    public String read (String name) throws IOException {
        FileInputStream input = null;
        String result = null;
        try {
            File file = new File(mActivity.getCacheDir() + File.separator + name);
            input = new FileInputStream(file);
            int size = input.available();
            byte[] result_bytes = new byte[size];
            input.read(result_bytes);
            result = new String(result_bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                input.close();
            }
        }
        return result;
    }

}
