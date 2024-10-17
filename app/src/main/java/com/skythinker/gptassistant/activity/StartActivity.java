package com.skythinker.gptassistant.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skythinker.gptassistant.entity.Entity_Info;
import com.skythinker.gptassistant.util.PreferencesUtil;
import com.skythinker.gptassistant.R;

/**
 * 作者: jiang
 *
 * @date: 2024/10/17
 */
public class StartActivity extends AppCompatActivity {
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        isLogin = (boolean) PreferencesUtil.getParam(Entity_Info.IS_LOGIN, false);

        if (isLogin){
            // 主界面
            startActivity(new Intent(StartActivity.this,MainActivity.class));
        }else {
            // 去登陆
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
        }
        finish();
    }
}