package module.url;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitModule {

    public Retrofit setURL(String API_base){
        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(API_base)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                .build();
        return retrofit;
    }

}
