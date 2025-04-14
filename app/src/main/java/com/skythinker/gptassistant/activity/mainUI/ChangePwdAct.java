package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.util.HttpUtils.sendGetRequest;
import static com.skythinker.gptassistant.util.HttpUtils.sendGetRequestNormal;
import static com.skythinker.gptassistant.util.MyUtil.API_MSG_SEND;
import static com.skythinker.gptassistant.util.MyUtil.APP_UPDATE_PWD_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_TEMPLATE_URL;
import static com.skythinker.gptassistant.util.MyUtil.MSG_API_ID;
import static com.skythinker.gptassistant.util.MyUtil.MSG_API_KEY;
import static com.skythinker.gptassistant.util.MyUtil.TOURIST;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.activity.LoginActivity;
import com.skythinker.gptassistant.entity.SignInInfo;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.thisInterFace.AppDatabase;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.skythinker.gptassistant.util.PreferencesUtil;
import com.tencent.mmkv.MMKV;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * jcl
 * 2025/4/9
 */
public class ChangePwdAct extends AppCompatActivity {
    private Unbinder unbinder;

    @BindView(R.id.resetPPhoneED)
    EditText resetPPhoneED;
    @BindView(R.id.resetPasswordOneED)
    EditText resetPasswordOneED;
    @BindView(R.id.resetPasswordTwoED)
    EditText resetPasswordTwoED;
    @BindView(R.id.resetPCodeED)
    EditText resetPCodeED;

    private String newPwd;
    private String reNewPwd;
    private String userPhone;
    private String mobile_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue_04));
        }*/

        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){
         userPhone = (String)PreferencesUtil.getParam(TOURIST,"");
         if (userPhone==null){
             MyToastUtil.showError("获取用户信息出差!");
             finish();
         }
        resetPPhoneED.setText(userPhone);
    }

    @OnClick({R.id.resetPGetCodeBut})
    public void myListener(View view){
        switch (view.getId()){
            case R.id.resetPGetCodeBut:
                sendMsgCode();
                break;
        }
    }

    public void sendMsgCode(){
        if (userPhone.isEmpty()){
            MyToastUtil.showSuccessful("请先输入手机号");
        }
        mobile_code = String.valueOf((int)((Math.random()*9+1)*100000));
        String msgINfo = "&mobile="+userPhone+"&content=您的验证码是："+mobile_code+"。请不要把验证码泄露给其他人。";
        String urlString = API_MSG_SEND + msgINfo;
        sendGetRequestNormal(urlString, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    public void submit(){
        String passwordOne = resetPasswordOneED.getText().toString();
        String passwordTwo = resetPasswordTwoED.getText().toString();
        String phoneCode = resetPCodeED.getText().toString();
        if (mobile_code.isEmpty()){
            MyToastUtil.showError("请输入验证码!");
            return;
        }
        if (!phoneCode.equals(mobile_code)){
            MyToastUtil.showError("验证码错误!");
            return;
        }
        if (!passwordOne.equals(passwordTwo)){
            MyToastUtil.showSuccessful("两次密码输入不一致!");
            return;
        }


        SignInInfo info = new SignInInfo();
        info.setPhone(userPhone);
        info.setCheckType(4); // 4--修改密码
        info.setPassword(passwordTwo);// 原始密码
        info.setNewPassword(newPwd);// 新密码

        String json = new Gson().toJson(info);
        HttpUtils.sendPostRequest(APP_UPDATE_PWD_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<Integer> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<Integer>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    MyToastUtil.showError(baseEntity.msg);
                }else {
                    MyToastUtil.showSuccessful("密码重置成功，请重新登录");
                    ClearInfo(ChangePwdAct.this);
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
            }
        });
    }
    public static void ClearInfo(Activity activity) {
        // 清空登录Token
        MMKV.defaultMMKV().encode(MyUtil.ACCESS_TOKEN, "");
        MMKV.defaultMMKV().encode(MyUtil.REFRESH_TOKEN,"");
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
