package com.skythinker.gptassistant.activity.mainUI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.skythinker.gptassistant.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateTemplateAct extends AppCompatActivity {
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
