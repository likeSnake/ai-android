package com.skythinker.gptassistant.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.skythinker.gptassistant.R;

/**
 * 作者: jiang
 * toast工具类
 * @date: 2024/10/25
 */
public class MyToastUtil {
    private static Context context;
    private static TextView tipsText;
    private static Toast toast;

    public static void init(Context context){
        MyToastUtil.context = context;
        toast = new Toast(context);
    }

    public static void setGravity(int gravity){
        toast.setGravity(gravity, 0, 0);
    }

    public static void showError(String content){
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null, false);
        tipsText = view.findViewById(R.id.ErrorTips);
        toast.setView(view);
        tipsText.setText(content);
        toast.show();
    }

    public static void showZ(String content){
        View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null, false);
        tipsText = view.findViewById(R.id.ErrorTips);
        toast.setView(view);
        tipsText.setText(content);
        toast.show();
    }
    public static void setShowTime(int time){
        toast.setDuration(time);
    }
    public static void setTextColor(int color){
        tipsText.setTextColor(context.getResources().getColor(color));
    }
    public static void setTextSize(float size){
        tipsText.setTextSize(size);
    }
}
