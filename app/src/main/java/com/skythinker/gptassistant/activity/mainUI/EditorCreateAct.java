package com.skythinker.gptassistant.activity.mainUI;

import static android.view.View.GONE;
import static com.skythinker.gptassistant.entity.base.ChatStreamClient.sendChatStream;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.ChatRequest;
import com.skythinker.gptassistant.entity.base.ChatStreamClient;
import com.skythinker.gptassistant.entity.base.HistoryEntity;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
import com.skythinker.gptassistant.thisInterFace.AppDatabase;
import com.skythinker.gptassistant.util.MyToastUtil;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.noties.markwon.Markwon;
import okhttp3.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.hutool.json.JSONObject;

public class EditorCreateAct extends AppCompatActivity {
    private Unbinder unbinder;
    @BindView(R.id.myText)
    TextView myText;
    @BindView(R.id.contentPro)
    ProgressBar contentPro;
    @BindView(R.id.saveContent)
    TextView saveContent;
    @BindView(R.id.optimizeContent)
    TextView optimizeContent;
    @BindView(R.id.textCount)
    TextView textCount;
    @BindView(R.id.textLayoutBot)
    LinearLayout textLayoutBot;
    @BindView(R.id.ic_copy)
    ImageView ic_copy;

    private StringBuilder markdownContent = new StringBuilder();
    private TextTemplate nowTemplate;
    private List<ChatRequest.Message> messageList;
    private android.app.AlertDialog dialog;
    private boolean isFinish = false;
    private boolean isContinuous = true;    // 连续对话
    private String AiName = "小助手";
    private String firstMsg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){
        messageList = new ArrayList<>();
        nowTemplate = (TextTemplate) getIntent().getSerializableExtra("textTemplate");
        if (nowTemplate == null){
            MyToastUtil.showError("文案模板获取失败!");
            finish();
            return;
        }
        initAi();
        showContentDialog(0);
    }

    public void initAi(){
        ChatRequest.Message systemMessage = new ChatRequest.Message(nowTemplate.getPrompt(), "system", AiName);
        messageList.add(systemMessage);
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.saveContent,R.id.optimizeContent,R.id.ic_copy})
    public void myClickListener(View view) {
        switch (view.getId()){
            case R.id.saveContent:
                if (isFinish){
                    saveCont();
                }else {
                    MyToastUtil.showError("请等待文案生成");
                }
                break;
            case R.id.optimizeContent:
                if (isFinish){
                    showContentDialog(1);
                }else {
                    MyToastUtil.showError("请等待文案生成");
                }
                break;
            case R.id.ic_copy:
                copyToClipboard(this,stripMarkdown(myText.getText().toString()));
                break;
        }
    }

    public void copyToClipboard(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text);
        clipboard.setPrimaryClip(clip);
        MyToastUtil.showSuccessful("已复制");
    }


    public void saveCont(){
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setTitle(firstMsg);
        historyEntity.setContent(myText.getText().toString());

        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance();
                db.qrCodeInfoDao().insertHistory(historyEntity);

                runOnUiThread(()->{
                    MyToastUtil.showSuccessful("已保存");
                });
            }
        }).start();
    }

    // type:0生成 1优化
    public void showContentDialog(int type){
        EditText dialogExportName;
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_content_input, null);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        // 设置弹窗的背景颜色为透明
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();

        TextView textTitle =  view.findViewById(R.id.dialogInputHTitle);
        Button close = view.findViewById(R.id.dialogInputHClose);
        Button submit = view.findViewById(R.id.dialogInputHSubmit);
        EditText promptText = view.findViewById(R.id.promptText);
        if (type == 0){
            textTitle.setText(nowTemplate.getTitle());
            promptText.setHint(getString(R.string.editor_create_act_hint2));
        }else {
            textTitle.setText(getString(R.string.editor_create_act_hint3));
            promptText.setHint(getString(R.string.editor_create_act_hint4));
        }

        close.setOnClickListener((v)->{
            if (isFinish){
                dialog.dismiss();
            }else {
                finish();
            }
        });
        submit.setOnClickListener((v)->{
            firstMsg = promptText.getText().toString();
            String s_prompt = firstMsg;
            if (s_prompt.isEmpty()){
                MyToastUtil.showError("请先输入提示内容");
                return;
            }
            String s1_prompt = "";
            if (type == 1){
                s1_prompt = "优化文案，";
            }
            s1_prompt +=s_prompt;
            sendQuestion(s1_prompt);
            dialog.dismiss();
        });
    }

    public void sendQuestion(String msg){
        showLoading();
        // 清空
        markdownContent.setLength(0);
        markdownContent.trimToSize();  // 将容量缩减到与长度匹配

        ChatRequest.Message userMessage = new ChatRequest.Message(msg, "user", "文案工作者");
        messageList.add(userMessage);

        ChatRequest chatRequest = new ChatRequest(messageList);

        Markwon markwon = Markwon.create(EditorCreateAct.this);
        sendChatStream(chatRequest, new ChatStreamClient.HttpCallBack() {
            @Override
            public void onSucceed(String content) {
                runOnUiThread(() -> {
                    markdownContent.append(content);
                    markwon.setMarkdown(myText, markdownContent.toString());
                    textCount.setText(String.valueOf(stripMarkdown(myText.getText().toString()).length()));
                });
            }

            @Override
            public void onFail(String msg) {
                MyToastUtil.showError(msg);
            }

            @Override
            public void onFinish() {
                isFinish = true;
                runOnUiThread(() -> {
                    onContentFinish();
                });
            }

            @Override
            public void onStart() {
                runOnUiThread(() -> {
                    disLoading();
                });

            }
        });
    }

    public static String stripMarkdown(String markdown) {
        return markdown
                .replaceAll("(?m)^#+\\s*", "") // 标题
                .replaceAll("(!)?\\[.*?]\\(.*?\\)", "") // 链接和图片
                .replaceAll("(?<=\\s|^)\\*\\*(.*?)\\*\\*", "$1") // 粗体
                .replaceAll("(?<=\\s|^)\\*(.*?)\\*", "$1") // 斜体
                .replaceAll("`{1,3}[^`]*`{1,3}", "") // 代码块或行内代码
                .replaceAll(">\\s*", "") // 引用
                .replaceAll("[-*_]{3,}", "") // 分割线
                .replaceAll("`", "") // 清除遗留的 `
                .trim();
    }

    public void showLoading(){
        isFinish = false;
        contentPro.setVisibility(View.VISIBLE);
        onContentLoading();
    }
    public void disLoading(){
        contentPro.setVisibility(View.GONE);
    }

    public void onContentLoading(){
        saveContent.setClickable(false);
        optimizeContent.setClickable(false);
    }

    public void onContentFinish(){
        saveContent.setClickable(true);
        optimizeContent.setClickable(true);
        if (isContinuous){
            messageList.add(new ChatRequest.Message(markdownContent.toString(), "system", AiName));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
