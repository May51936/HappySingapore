package common.network;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import Utils.HTTPUtils;
import Utils.URLUtils;
import module.url.NewsRsp;
import module.url.RequestModule;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.GET;

public class NewsReq extends RequestModule{

}
