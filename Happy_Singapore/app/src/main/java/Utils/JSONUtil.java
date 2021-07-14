package Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JSONUtil {
    /**
     * 请求返回处理帮助类
     * @param json
     * @param clazz
     * @return
     */
    private static String TAG = "json_processing";

    public static JsonObject fromJson(String json) {
        JsonObject data = new JsonParser().parse(json).getAsJsonObject();
        return data;
    }

    public static JsonArray getArray(JsonObject json, String name){
        JsonArray array = json.getAsJsonArray(name);
        return array;
    }


}
