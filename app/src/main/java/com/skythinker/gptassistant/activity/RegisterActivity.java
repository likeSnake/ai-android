package com.skythinker.gptassistant.activity;

import static com.skythinker.gptassistant.util.HttpUtils.sendGetRequestNormal;
import static com.skythinker.gptassistant.util.MyUtil.API_MSG_SEND;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_REGISTER_URL;
import static com.skythinker.gptassistant.util.MyUtil.OPEN_APP_DEBUG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.entity.user.User;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.skythinker.gptassistant.util.SmsCodeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;

@SuppressLint("NonConstantResourceId")
public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.signInVGetCodeBut)
    Button signInVGetCodeBut;
    @BindView(R.id.start_register)
    Button start_register;
    @BindView(R.id.inputUserName)
    EditText inputUserName;
    @BindView(R.id.inputUserPwd)
    EditText inputUserPwd;
    @BindView(R.id.inputPhone)
    EditText inputPhone;
    @BindView(R.id.verificationCode)
    EditText verificationCode;


    private Unbinder unbinder;
    private boolean isUserNameEmpty = true;
    private boolean isUserPwdEmpty = true;
    private boolean isUserPhoneEmpty = true;
    private boolean isUserCodeEmpty = true;
    private String mobile_code = "AABBCC";
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        initTextChangedListener();
        initLoading();
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.signInVGetCodeBut, R.id.start_register})
    public void MyListener(View view){
        switch (view.getId()){
            case R.id.signInVGetCodeBut:
                getVerificationCode();
                break;
            case R.id.start_register:
                registerSubmit();
                break;
        }
    }

    public void initTextChangedListener(){
        inputUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString().trim();
                isUserNameEmpty = text.isEmpty();
                changeButState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputUserPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString().trim();
                isUserPwdEmpty = text.isEmpty();
                changeButState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        inputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString().trim();
                isUserPhoneEmpty = text.isEmpty();
                changeButState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        verificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString().trim();
                isUserCodeEmpty = text.isEmpty();
                changeButState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void changeButState(){
        if((isUserCodeEmpty || isUserPhoneEmpty || isUserPwdEmpty || isUserNameEmpty)){
            start_register.setBackgroundResource(R.drawable.bt_round_login_no);
            start_register.setTextColor(getColor(R.color.teal_200));
            start_register.setClickable(false);
        }else {
            start_register.setBackgroundResource(R.drawable.bt_round_login);
            start_register.setTextColor(getColor(R.color.black));
            start_register.setClickable(true);
        }
    }

    public void registerSubmit(){
        String userName = inputUserName.getText().toString().trim();
        String password = inputUserPwd.getText().toString().trim();
        String phone = inputPhone.getText().toString().trim();
        String code = verificationCode.getText().toString().trim();

        if (!code.equals(mobile_code) && !OPEN_APP_DEBUG){
            MyToastUtil.showError("验证码错误");
            return;
        }
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setPhone_number(phone);

        String json = new Gson().toJson(user);
        showLoading();
        HttpUtils.sendPostRequest(APP_USER_REGISTER_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<Integer> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<Integer>>(){}.getType());
                hideLoading();
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    MyToastUtil.showError(baseEntity.msg);
                }else {
                    MyToastUtil.showSuccessful("注册成功");
                    finish();
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showSuccessful("注册失败");
                hideLoading();
            }
        });

    }

    public boolean checkVerificationCode(String code){
        return true;
    }

    public void getVerificationCode(){
        sendMsgCode();
    }
    public void sendMsgCode(){
        if (isUserPhoneEmpty){
            MyToastUtil.showSuccessful("请先输入手机号");
            return;
        }
        signInVGetCodeBut.setEnabled(false);
        signInVGetCodeBut.setClickable(false);
        String userPhone = inputPhone.getText().toString().trim();
        mobile_code = String.valueOf((int)((Math.random()*9+1)*100000));
        String msgINfo = "&mobile="+userPhone+"&content=您的验证码是："+mobile_code+"。请不要把验证码泄露给其他人。";
        String urlString = API_MSG_SEND + msgINfo;
        sendGetRequestNormal(urlString, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                new SmsCodeHelper(signInVGetCodeBut, 59).smsCodeGet(new SmsCodeHelper.SmsTimerCall() {
                    @Override
                    public void call(boolean finished) {
                        if (finished){
                            signInVGetCodeBut.setEnabled(true);
                            signInVGetCodeBut.setClickable(true);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void initLoading(){
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_load, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setView(view);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void showLoading(){
        runOnUiThread(()->{
            dialog.show();
        });
    }
    public void hideLoading(){
        runOnUiThread(()->{
            dialog.dismiss();
        });
    }
}