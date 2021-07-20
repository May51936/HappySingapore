package common.nav;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

import common.test.TestActivity;

public class changeLanguage {
    private Activity mActivity;
    private Class activityClass;


    public changeLanguage(Activity activity, Class activityClass){
        this.mActivity = activity;
        this.activityClass = activityClass;
    }

    public void changeAppLanguage() {
        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
        Locale locale = null;
        Configuration configuration = mActivity.getResources().getConfiguration();
        if (configuration.locale == Locale.SIMPLIFIED_CHINESE)
            locale = Locale.ENGLISH;
        else
            locale = Locale.SIMPLIFIED_CHINESE;
        SharedPreferences sp = mActivity.getSharedPreferences("SP_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("language", Locale.SIMPLIFIED_CHINESE.toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }
        mActivity.getResources().updateConfiguration(configuration, metrics);
        //重新启动Activity
        Intent intent = new Intent(mActivity,activityClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
    }
}

