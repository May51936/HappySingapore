package common.nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.WangTianyu.HappySingapore.R;

import java.util.Locale;

import common.test.TestActivity;

public class changeLanguage {
    private Activity mActivity;
    private Class activityClass;

    private static final String TAG = changeLanguage.class.toString();

    public changeLanguage(Activity activity, Class activityClass){
        this.mActivity = activity;
        this.activityClass = activityClass;
    }

    public void changeAppLanguage() {
        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
        Locale locale = null;
        Configuration configuration = mActivity.getResources().getConfiguration();
        SharedPreferences sp = mActivity.getSharedPreferences("SP_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String language = sp.getString("language", null);
        if (language.equals("Chinese")){
            locale = Locale.ENGLISH;
            editor.putString("language", "English");
        }
        else{
            locale = Locale.SIMPLIFIED_CHINESE;
            editor.putString("language", "Chinese");
        }
        editor.commit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        mActivity.getResources().updateConfiguration(configuration, metrics);
        //重新启动Activity
        Log.i(TAG, language);
        Log.i(TAG, configuration.locale.toLanguageTag());
        Intent intent = new Intent(mActivity,activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
    }
}

