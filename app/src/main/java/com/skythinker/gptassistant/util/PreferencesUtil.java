package com.skythinker.gptassistant.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 作者: jiang
 *
 * @date: 2024/10/17
 */
public class PreferencesUtil {

    private static Context myContext;
    private static final String FILE_NAME = "ai_writer";

    public static void init(Context context) {
        PreferencesUtil.myContext = context.getApplicationContext();
    }

    public static Context getMyContext(){
        if (myContext != null) {
            return myContext;
        }
        throw new NullPointerException("先初始化");
    }

    public static void setParam(String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = getMyContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        switch (type) {
            case "String":
                editor.putString(key, (String) object);
                break;
            case "Integer":
                editor.putInt(key, (Integer) object);
                break;
            case "Boolean":
                editor.putBoolean(key, (Boolean) object);
                break;
            case "Float":
                editor.putFloat(key, (Float) object);
                break;
            case "Long":
                editor.putLong(key, (Long) object);
                break;
        }
        editor.commit();
    }

    public static Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = getMyContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        switch (type) {
            case "String":
                return sp.getString(key, (String) defaultObject);
            case "Integer":
                return sp.getInt(key, (Integer) defaultObject);
            case "Boolean":
                return sp.getBoolean(key, (Boolean) defaultObject);
            case "Float":
                return sp.getFloat(key, (Float) defaultObject);
            case "Long":
                return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }
}
