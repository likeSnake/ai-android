package com.skythinker.gptassistant.fragment;


import static com.skythinker.gptassistant.util.MyUtil.APP_USER_GET_TEMPLATE;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_GET_TEMPLATE_BY_ID;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_INFO_URL;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_TEMPLATE_URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.activity.LoginActivity;
import com.skythinker.gptassistant.activity.mainUI.CreateTemplateAct;
import com.skythinker.gptassistant.activity.mainUI.EditorCreateAct;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.entity.base.BaseListEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.skythinker.gptassistant.util.NoticeUtil;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

@SuppressLint("NonConstantResourceId")
public class CreateFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;
    @BindView(R.id.createTemplate)
    TextView createTemplate;
    @BindView(R.id.textNameTitle)
    TextView textNameTitle;
    @BindView(R.id.switchTemp)
    ImageView switchTemp;
    @BindView(R.id.image1)
    ImageView image1;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;

    private AlignmentBoxAdapter adapter;
    private List<TextTemplate> hotData;
    private List<TextTemplate> myData;
    private int nowTempType = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_creation, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        initView(rootView);
        initData();
        initAdapter();
        initLinsenner();
        return rootView;
    }

    public void initData(){
        hotData = new ArrayList<>();
        myData = new ArrayList<>();
        refreshData();
    }
    public void initView(View rootView){

    }
    public void updateData(){
        hideLoading();
        adapter.updateData(nowTempType == 0?hotData:myData);
    }

    @OnClick({R.id.createTemplate,R.id.switchTemp})
    public void myClickListener(View view) {
        switch (view.getId()){
            case R.id.createTemplate:
                startActivity(new Intent(getContext(),CreateTemplateAct.class));
                break;
            case R.id.switchTemp:
                showSwitchTemp(view);
                break;
        }
    }

    public void showSwitchTemp(View view){
        ContextThemeWrapper contextThemeWrapper =
                new ContextThemeWrapper(getContext(), R.style.CustomPopMenuStyle);
        PopupMenu popupMenu = new PopupMenu(contextThemeWrapper, view);
        popupMenu.getMenuInflater().inflate(R.menu.item_temp_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_hot_temp:
                        image1.setVisibility(View.VISIBLE);
                        textNameTitle.setText(getContext().getString(R.string.editor_create_act_hint12));
                        changeTempType(0);
                        return true;
                    case R.id.menu_my_temp:
                        image1.setVisibility(View.INVISIBLE);
                        textNameTitle.setText(getContext().getString(R.string.editor_create_act_hint13));
                        changeTempType(1);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }

    // 模板类型 0热门模板 1我的模板
    public void changeTempType(int tempType){
        if (nowTempType == tempType)return;
        nowTempType = tempType;
        refreshData();
    }

    public void refreshData(){
        showLoading();
        if (nowTempType == 0){
            HttpUtils.sendGetRequest(APP_USER_GET_TEMPLATE, new HttpUtils.HttpCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    BaseListEntity<TextTemplate> baseEntity = new Gson().fromJson(result, new TypeToken<BaseListEntity<TextTemplate>>(){}.getType());
                    if (baseEntity == null){
                        return;
                    }
                    if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                        if (baseEntity.code == MyUtil.HTTP_CODE_TOKEN_OVERDUE){
                            // token过期
                            MyToastUtil.showSuccessful("登录状态过期，请重新登录");
                            startActivity(new Intent(getContext(), LoginActivity.class));
                        }else {
                            MyToastUtil.showError(baseEntity.msg);
                        }
                    }else {
                        hotData = baseEntity.getData();
                        getActivity().runOnUiThread(()->{
                            updateData();
                        });

                    }
                }
                @Override
                public void onFailure(Exception e) {
                    MyToastUtil.showError(e.getMessage());
                }
            });
        }else {
            HttpUtils.sendGetRequest(APP_USER_GET_TEMPLATE_BY_ID, new HttpUtils.HttpCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    BaseListEntity<TextTemplate> baseEntity = new Gson().fromJson(result, new TypeToken<BaseListEntity<TextTemplate>>(){}.getType());
                    if (baseEntity == null){
                        return;
                    }
                    if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                        if (baseEntity.code == MyUtil.HTTP_CODE_TOKEN_OVERDUE){
                            // token过期
                            MyToastUtil.showSuccessful("登录状态过期，请重新登录");

                        }else {
                            MyToastUtil.showError(baseEntity.msg);
                        }
                    }else {
                        myData = baseEntity.getData();
                        getActivity().runOnUiThread(()->{
                            updateData();
                        });

                    }
                }
                @Override
                public void onFailure(Exception e) {
                    MyToastUtil.showError(e.getMessage());
                }
            });
        }

    }

    public void initAdapter(){
        if (adapter == null)adapter = new AlignmentBoxAdapter(nowTempType == 0?hotData:myData);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        recyclerView1.setLayoutManager(manager);
        recyclerView1.setAdapter(adapter);
    }

    public void initLinsenner(){

    }

    class AlignmentBoxAdapter extends RecyclerView.Adapter<AlignmentBoxAdapter.ViewHolder2> {

        private List<TextTemplate> dataList;

        public AlignmentBoxAdapter(List<TextTemplate> dataList) {
            this.dataList = dataList;
        }
        @SuppressLint("NotifyDataSetChanged")
        public void updateData(List<TextTemplate> dataList) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            // 给每个ItemView指定不同的类型，这样在RecyclerView看来，这些ItemView全是不同的，不能复用
            return position;
        }


        @NonNull
        @Override
        public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_template, parent, false);
            return new ViewHolder2(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder2 holder, int position) {
            TextTemplate data = dataList.get(position);
            if (data == null){
                return;
            }
            String title = data.getTitle();
            String content = data.getContent();
            int permissionLevel = data.getPermissionLevel();
            holder.content_item.setText(content);
            holder.title_item.setText(title);
            if (nowTempType == 1){
                holder.layoutBottom.setVisibility(View.VISIBLE);
                holder.tempEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(),CreateTemplateAct.class);
                        intent.putExtra("data",data);
                        startActivity(intent);
                    }
                });
                if (permissionLevel == 0){
                    holder.tempShare.setText("已共享");
                    holder.tempShare.setTextColor(getContext().getColor(R.color.gray7));
                }else {
                    holder.tempShare.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NoticeUtil.showDialogChooseCountdown(getContext(), "是否共享该模板，共享后所有人都能搜索看到你创建的模板", "共享模板",0,new NoticeUtil.NoticeCallback() {
                                @Override
                                public void onYes() {
                                    dd(data);
                                }

                                @Override
                                public void onNo() {

                                }
                            });
                        }
                    });
                }
            }else {
                holder.layoutBottom.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EditorCreateAct.class);
                    intent.putExtra("textTemplate",data);

                    getActivity().startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
        class ViewHolder2 extends RecyclerView.ViewHolder {
            TextView title_item,content_item,tempShare,tempEdit;
            LinearLayout layoutBottom;

            public ViewHolder2(View itemView) {
                super(itemView);
                title_item = itemView.findViewById(R.id.title_item);
                content_item = itemView.findViewById(R.id.content_item);
                layoutBottom = itemView.findViewById(R.id.layoutBottom);
                tempShare = itemView.findViewById(R.id.tempShare);
                tempEdit = itemView.findViewById(R.id.tempEdit);

            }
        }
    }

    public void dd(TextTemplate textTemplate){
        showLoading();
        textTemplate.setPermissionLevel(0);
        String json = new Gson().toJson(textTemplate);
        HttpUtils.sendPostRequest(APP_USER_TEMPLATE_URL, json, new HttpUtils.HttpCallback<String>() {
            @Override
            public void onSuccess(String result) {
                BaseEntity<Integer> baseEntity = new Gson().fromJson(result, new TypeToken<BaseEntity<Integer>>(){}.getType());
                if (baseEntity.code != MyUtil.HTTP_CODE_SUCCESSFUL){
                    MyToastUtil.showError(baseEntity.msg);
                }else {
                    refreshData();
                }
            }

            @Override
            public void onFailure(Exception e) {
                MyToastUtil.showError(e.getMessage());
            }
        });
    }

    public void showLoading(){
        getActivity().runOnUiThread(()->{
            recyclerView1.setVisibility(View.GONE);
            progressBar1.setVisibility(View.VISIBLE);
        });
    }
    public void hideLoading(){
        getActivity().runOnUiThread(()->{
            recyclerView1.setVisibility(View.VISIBLE);
            progressBar1.setVisibility(View.GONE);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }
}
