package module.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Picture {

    private Bitmap bitmap;
    private static final String TAG = Picture.class.toString();

    public Bitmap getPic(){
        return bitmap;
    }

    public void getFromURL(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        conn.getResponseCode();
        if (conn.getResponseCode() == 200) {
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.i(TAG, "Successfully get picture");
        }
        else{
            Log.e(TAG, "Get pic failed");
        }
    }
}
