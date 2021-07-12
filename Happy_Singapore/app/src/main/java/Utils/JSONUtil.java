package Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class JSONUtil {
    /**
     * 请求返回处理帮助类
     * @param json
     * @param clazz
     * @return
     */
    private static String TAG = "json_processing";

    public static Object fromJson(String json, Class<?> clazz) {
        Object obj = null;
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Log.i(TAG,"fromJson");
        obj = gson.fromJson(json, clazz);
        return obj;
    }


}
