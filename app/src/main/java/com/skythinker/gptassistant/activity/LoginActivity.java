package com.skythinker.gptassistant.activity;

import static com.skythinker.gptassistant.util.MyUtil.APP_APPINFO_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_BASE_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_LOGIN_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_INFO_URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.entity.base.MyToken;
import com.skythinker.gptassistant.entity.user.User;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.skythinker.gptassistant.util.SignInUtil;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("NonConstantResourceId")
public class LoginActivity extends AppCompatActivity{


    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.start_login)
    Button start_login;
    private Unbinder unbinder;
    @BindView(R.id.inputUserName)
    EditText inputUserName;
    @BindView(R.id.inputUserPwd)
    EditText inputUserPwd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){

    }


    @OnClick({R.id.register,R.id.start_login})
    public void myListener(View view) {
        switch (view.getId()) {
            case R.id.register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.start_login:
                startLogin();
                break;
        }
    }

    public void startLogin(){
        String userName = inputUserName.getText().toString().trim();
        String userPwd = inputUserPwd.getText().toString().trim();

        User user = new User();
        user.setUserName(userName);
        user.setPassword(userPwd);

        String json = new Gson().toJson(user);
        HttpUtils.sendPostRequest(APP_LOGIN_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //BaseEntity baseEntity = new Gson().fromJson(result, BaseEntity.class);
                BaseEntity<MyToken> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<MyToken>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    MyToastUtil.showError(baseEntity.msg);
                }else {
                    MyToken myToken = baseEntity.getData();
                    if (myToken == null){
                        MyToastUtil.showSuccessful("登录失败，Token为空！");
                    }else {
                        MMKV.defaultMMKV().encode(MyUtil.ACCESS_TOKEN,myToken.getAccessToken());
                        MMKV.defaultMMKV().encode(MyUtil.REFRESH_TOKEN,myToken.getRefreshToken());
                        getUserInfo();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
            }
        });
    }

    public void getUserInfo(){

        HttpUtils.sendGetRequest(APP_USER_INFO_URL, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<User> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<User>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    if (baseEntity.code == MyUtil.HTTP_CODE_TOKEN_OVERDUE){
                        // token过期
                        MyToastUtil.showSuccessful("登录状态过期，请重新登录");

                    }else {
                        MyToastUtil.showError(baseEntity.msg);
                    }
                }else {
                    User user = baseEntity.getData();
                    if (user!=null){
                        MyToastUtil.showSuccessful("登录成功");
                        MMKV.defaultMMKV().encode(MyUtil.IS_LOGIN,true);
                        SignInUtil.signInSuccess(user);
                        startMain();
                    }else {
                        MyToastUtil.showError("出错了，请重试");
                    }

                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
            }
        });
    }

    public void startMain(){
        startActivity(new Intent(LoginActivity.this, MainActivity2.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}