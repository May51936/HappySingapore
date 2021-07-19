package common.network;

import android.app.Activity;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import Utils.HTTPUtils;
import Utils.URLUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import module.url.RequestModule;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class LTAReq extends RequestModule {
    private Observable<ResponseBody> observable;
    private Activity mActivity;

    public static final String TAG = LTAReq.class.toString();

    public LTAReq(Activity activity){
        this.mActivity = activity;
    }

    public void init(){
        Retrofit retrofit = new RetrofitModule().setURL(URLUtils.bus_arrival);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.getBusArrivals();
    }

    public void sendReq(){
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {
                        Log.d(TAG, "Connection Start...");
                    }

                    @Override
                    public void onNext(@NotNull ResponseBody responseBody) {
                        try {
                            Log.i(TAG, responseBody.string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e(TAG, "Successful data");
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Log.d(TAG, "Failed connection:"+e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Successful connection");
                    }
                });
    }
}
