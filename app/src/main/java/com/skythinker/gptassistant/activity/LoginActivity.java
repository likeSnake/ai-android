package com.skythinker.gptassistant.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.skythinker.gptassistant.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.start_login)
    Button start_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    @OnClick({R.id.register})
    public void myOnClick(View view){
        switch (view.getId()){
            case R.id.register:
                break;

        }
    }
}