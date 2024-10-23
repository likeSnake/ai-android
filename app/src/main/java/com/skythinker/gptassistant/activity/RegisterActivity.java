package com.skythinker.gptassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.util.SmsCodeHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.signInVGetCodeBut)
    Button signInVGetCodeBut;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        unbinder = ButterKnife.bind(this);

    }

    @OnClick({R.id.signInVGetCodeBut})
    public void initListener(View view){
        switch (view.getId()){
            case R.id.signInVGetCodeBut:
                getVerificationCode();
                break;
        }
    }

    public void getVerificationCode(){
        new SmsCodeHelper(signInVGetCodeBut, 60).smsCodeGet(new SmsCodeHelper.SmsTimerCall() {
            @Override
            public void call(boolean finished) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}