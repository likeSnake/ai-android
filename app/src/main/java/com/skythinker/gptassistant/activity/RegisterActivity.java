package com.skythinker.gptassistant.activity;

import static com.skythinker.gptassistant.util.HttpUtils.sendGetRequestNormal;
import static com.skythinker.gptassistant.util.MyUtil.API_MSG_SEND;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_REGISTER_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.user.User;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
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
    private String mobile_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);
        initTextChangedListener();

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

        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setPhone_number(phone);

        String json = new Gson().toJson(user);

        HttpUtils.sendPostRequest(APP_USER_REGISTER_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyToastUtil.showSuccessful("注册成功");
                finish();
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }

    public boolean checkVerificationCode(String code){
        return true;
    }

    public void getVerificationCode(){
        sendMsgCode();
        new SmsCodeHelper(signInVGetCodeBut, 60).smsCodeGet(new SmsCodeHelper.SmsTimerCall() {
            @Override
            public void call(boolean finished) {

            }
        });
    }
    public void sendMsgCode(){
        if (isUserPhoneEmpty){
            MyToastUtil.showSuccessful("请先输入手机号");
        }
        String userPhone = inputPhone.getText().toString().trim();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}