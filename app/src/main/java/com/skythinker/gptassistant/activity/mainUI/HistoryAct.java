package com.skythinker.gptassistant.activity.mainUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.activity.HistoryActivity;
import com.skythinker.gptassistant.entity.base.HistoryEntity;
import com.skythinker.gptassistant.fragment.CreateFragment;
import com.skythinker.gptassistant.thisInterFace.AppDatabase;
import com.skythinker.gptassistant.util.ChatManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("NonConstantResourceId")
public class HistoryAct extends AppCompatActivity {
    private Unbinder unbinder;

    @BindView(R.id.rv_history_list)
    RecyclerView rv_history_list;

    private HistoryListAdapter historyListAdapter;
    private List<HistoryEntity> historyEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.dark_blue_04));
        }

        unbinder = ButterKnife.bind(this);
        initData();
    }
    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(HistoryAct.this);
                historyEntityList = db.qrCodeInfoDao().getAllHistory();
                runOnUiThread(()->{
                    initAdapter();
                });
            }
        }).start();
    }

    public void initAdapter(){
        if (historyListAdapter == null)historyListAdapter = new HistoryListAdapter(historyEntityList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_history_list.setLayoutManager(manager);
        rv_history_list.setAdapter(historyListAdapter);
    }

    static private class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
        private List<HistoryEntity> dataList;

        public HistoryListAdapter(List<HistoryEntity> dataList) {
            this.dataList = dataList;
        }
        @SuppressLint("NotifyDataSetChanged")
        public void updateData(List<HistoryEntity> dataList) {
            this.dataList = dataList;
            notifyDataSetChanged();
        }

        @Override
        public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
            return new HistoryListAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(HistoryListAdapter.ViewHolder holder, int position) {
            HistoryEntity data = dataList.get(position);
            holder.tvTitle.setText(data.getTitle());
            holder.tvDetail.setText(data.getContent().replace("\n", "").replace("\r", ""));
            holder.tvTime.setText(data.getCreateTime());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTitle, tvDetail, tvTime;
            private LinearLayout llOuter;
            public ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_history_item_title);
                tvDetail = itemView.findViewById(R.id.tv_history_item_detail);
                tvTime = itemView.findViewById(R.id.tv_history_item_time);
                llOuter = itemView.findViewById(R.id.ll_history_item_outer);
                llOuter.setOnClickListener((view) -> {
                    /*Intent intent = new Intent();
                    intent.putExtra("id", historyActivity.chatManager.getConversationAtPosition(getAdapterPosition(), historyActivity.searchKeyword).id);
                    historyActivity.setResult(RESULT_OK, intent);
                    historyActivity.finish();*/
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
