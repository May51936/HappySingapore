package module.data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.WangTianyu.HappySingapore.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Utils.HTTPUtils;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import module.adapter.RecyclerAdapter;
import module.url.NewsRspAll;
import module.url.RetrofitModule;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class Picture {

    private Bitmap bitmap;
    private Observable<ResponseBody> observable;
    private Activity mActivity;
    private RecyclerAdapter.MyViewHolder holder;

    private static final String TAG = Picture.class.toString();

    public Picture(Activity activity, RecyclerAdapter.MyViewHolder holder){
        this.mActivity = activity;
        this.holder = holder;
    }

    public Bitmap getPic(){
        return bitmap;
    }

    public void setPic(){
        holder.img.setImageBitmap(bitmap);
    }

    public void getFromURL(String path) throws IOException {
        //分割URL以便满足retrofit要求
        Log.i(TAG, path);
        int split = path.indexOf("/", 8);
        String base_url = path.substring(0,split+1);
        String addi_url = path.substring(split+1);
        Log.i(TAG, base_url);
        Retrofit retrofit = new RetrofitModule().setURL(base_url);
        HTTPUtils service = retrofit.create(HTTPUtils.class);
        observable = service.getPic(addi_url);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "Connection Start...");
                    }
                    @Override
                    public void onNext(ResponseBody result) {
                        //对返回的数据进行处理
                        bitmap = BitmapFactory.decodeStream(result.byteStream());
                        setPic();
                        Log.e(TAG, "Successful data");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Failed connection：" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "Successful connection");
                    }
                });
        }
}
