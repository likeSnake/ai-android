package com.skythinker.gptassistant.fragment;


import static com.skythinker.gptassistant.util.MyUtil.APP_USER_GET_TEMPLATE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.activity.mainUI.ChangePwdAct;
import com.skythinker.gptassistant.entity.base.BaseListEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    private List<TextTemplate> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        initData();
        return rootView;
    }

    public void initData(){

    }
    public void initView(View rootView){

    }

    @OnClick({R.id.share_layout})
    public void myListener(View view){
        switch (view.getId()){
            case R.id.share_layout:
                startActivity(new Intent(getActivity(), ChangePwdAct.class));
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onClick(View view) {

    }
}
