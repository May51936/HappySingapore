package common.network;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.speech.RecognitionService;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WangTianyu.HappySingapore.R;
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
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.ResponseCache;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import Utils.HTTPUtils;
import Utils.URLUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import module.adapter.RecyclerAdapter;
import module.data.FileProcessing;
import module.data.Picture;
import module.url.NewsRsp;
import module.url.NewsRspAll;
import module.url.RequestModule;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsReq extends RequestModule{
    private Observable<NewsRspAll> observable;
    private int max;
    private Activity mActivity;
    private ArrayList<NewsRsp> array = new ArrayList<NewsRsp>();
    private Handler handler = new Handler();
    private Bundle bundle;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;


    private static final String TAG = NewsReq.class.toString();

    public NewsReq(Activity mActivity){
        this.mActivity = mActivity;
    }

    public void sendReq(){
        //发送网络请求
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsRspAll>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "Connection Start...");
                    }

                    @Override
                    public void onNext(NewsRspAll result) {
                        //对返回的数据进行处理
                        try {
                            processJSONData(result);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "Successful data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Failed connection");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Successful connection");
                        //呈现新闻给用户
                        init_view();
                    }
                });
        Log.i(TAG, String.valueOf(array.size()));
    }

    public void init(){
        //retrofit初始化
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.news);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.getNews();
    }


    public void processJSONData(NewsRspAll json) throws IOException {
        //处理请求返回的新闻信息
        List data;
        NewsRsp rsp;
        FileProcessing file = null;
        max = json.getTotalResults();
        data = json.getNews();
        Log.i(TAG, data.toString());
        for(int i = 0; i < data.size(); i++){
            rsp = setOneNews(i, data);
            if (rsp.get_pic()!=null)
                array.add(rsp);
        }
//        set_pic();
    }

    public NewsRsp setOneNews(int num, List data) throws IOException {
        //将单个新闻JSON转为NewsRsp类
        NewsRsp news = new NewsRsp();
        JsonObject obj = new JsonParser().parse(data.get(num).toString()).getAsJsonObject();
        try{
            news.set_date(obj.get("publishedAt").getAsString());
            news.set_title(obj.get("title").getAsString());
            news.set_name(obj.get("source").getAsJsonObject().get("name").getAsString());
            news.set_url(obj.get("url").getAsString());
            news.set_pic(obj.get("urlToImage").getAsString());
        } catch (RuntimeException e){
            e.printStackTrace();
        }
        return news;
    }

    public NewsRsp getOneNews(int num){
        return array.get(num);
    }

    public ArrayList<NewsRsp> getArray(){
        return array;
    }

    public void init_view(){
        recyclerView = (RecyclerView) mActivity.findViewById(R.id.news_view);
        adapter = new RecyclerAdapter(mActivity, array);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

//    public void assign(ArrayList<NewsRsp> array){
//        Message message = new Message();
//        bundle = new Bundle();
//        bundle.putSerializable("array", (Serializable) array);
//        message.setData(bundle);
//        handler.sendMessage(message);
//    }

}
