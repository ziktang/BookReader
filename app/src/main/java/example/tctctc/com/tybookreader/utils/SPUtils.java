package example.tctctc.com.tybookreader.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.R.attr.value;

/**
 * Created by tctctc on 2017/3/26.
 * Function:SharedPreferences工具类
 */

public class SPUtils {
    private SharedPreferences.Editor mEditor;
    private SharedPreferences mPreferences;

    public SPUtils(Context context, String name) {
        mPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();
    }


    /**
     * 保存字段
     **/
    public void saveFloat(String key, float value) {
        mEditor.putFloat(key, value).commit();
    }

    public void saveLong(String key, Long value) {
        mEditor.putLong(key, value).commit();
    }

    public void saveBoolean(String key, Boolean value) {
        mEditor.putBoolean(key, value).commit();
    }

    public void saveString(String key, String value) {
        mEditor.putString(key, value).commit();
    }

    public void saveInt(String key, int value) {
        mEditor.putInt(key, value).commit();
    }


    /**
     * 获取字段
     **/
    public float getFloat(String key, float defaultValue) {
        return mPreferences.getFloat(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mPreferences.getInt(key, defaultValue);
    }


    public long getLong(String key, Long defaultValue) {
        return mPreferences.getLong(key, defaultValue);
    }

    public boolean getBoolean(String key, Boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }
}
