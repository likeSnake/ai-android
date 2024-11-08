package com.skythinker.gptassistant.activity;

import static com.skythinker.gptassistant.util.MyUtil.APP_BASE_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_INFO_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_REFRESH_TOKEN;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.appcompat.app.AppCompatActivity;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.entity.base.MyToken;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.tencent.mmkv.MMKV;

public class WelcomeActivity extends AppCompatActivity {
    private boolean is_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void startMain(){
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    public void startLogin(){
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }

    public void initData(){
        // 判断是登录过
        is_Login = MMKV.defaultMMKV().decodeBool(MyUtil.IS_LOGIN,false);

        if (is_Login){
            // 登录过使用token登录
            loginToToken();
        }else {
            // 没登陆过就去登陆界面
            startLogin();
        }
    }

    public void loginToToken(){
        String refreshToken = MMKV.defaultMMKV().decodeString(MyUtil.REFRESH_TOKEN, "");
        MyToken myToken = new MyToken();
        myToken.setRefreshToken(refreshToken);
        String json = new Gson().toJson(myToken);

        HttpUtils.sendPostRequest(APP_BASE_URL+APP_USER_REFRESH_TOKEN,json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<MyToken> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<MyToken>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    if (baseEntity.code == MyUtil.HTTP_CODE_TOKEN_OVERDUE){
                        // token过期
                        MyToastUtil.showSuccessful("登录状态过期，请重新登录");
                        // 去登录
                        startLogin();
                    }else {
                        MyToastUtil.showError(baseEntity.msg);
                    }
                }else {
                    MyToken myToken = baseEntity.getData();
                    if (myToken == null){
                        MyToastUtil.showSuccessful("登录失败，Token为空！");
                    }else {
                        MMKV.defaultMMKV().encode(MyUtil.ACCESS_TOKEN,myToken.getAccessToken());
                        MMKV.defaultMMKV().encode(MyUtil.REFRESH_TOKEN,myToken.getRefreshToken());
                        startMain();
                    }

                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
                // 去登录
                startLogin();
            }
        });
    }

}