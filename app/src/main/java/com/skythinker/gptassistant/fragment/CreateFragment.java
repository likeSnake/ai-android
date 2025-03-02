package com.skythinker.gptassistant.fragment;


import static com.skythinker.gptassistant.util.MyUtil.APP_USER_GET_TEMPLATE;
import static com.skythinker.gptassistant.util.MyUtil.APP_USER_INFO_URL;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.BaseEntity;
import com.skythinker.gptassistant.entity.base.BaseListEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.util.HttpUtils;
import com.skythinker.gptassistant.util.MyToastUtil;
import com.skythinker.gptassistant.util.MyUtil;
import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateFragment extends Fragment implements View.OnClickListener {

    private Unbinder unbinder;
    @BindView(R.id.recyclerView1)
    RecyclerView recyclerView1;

    private AlignmentBoxAdapter adapter;
    private List<TextTemplate> data;

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
        data = new ArrayList<>();
        refreshData();
    }
    public void initView(View rootView){

    }
    public void updateData(){
        adapter.updateData(data);
    }

    public void refreshData(){
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

                    }else {
                        MyToastUtil.showError(baseEntity.msg);
                    }
                }else {
                    data = baseEntity.getData();
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

    public void initAdapter(){
        if (adapter == null)adapter = new AlignmentBoxAdapter(data);
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


        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
        class ViewHolder2 extends RecyclerView.ViewHolder {
            CheckBox menu_box;

            public ViewHolder2(View itemView) {
                super(itemView);

            }
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
