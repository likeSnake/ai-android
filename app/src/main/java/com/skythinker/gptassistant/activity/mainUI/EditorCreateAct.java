package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.entity.base.ChatStreamClient.sendChatStream;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.ChatRequest;
import com.skythinker.gptassistant.entity.base.ChatStreamClient;
import com.skythinker.gptassistant.entity.copyWriter.TextTemplate;
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

    private StringBuilder markdownContent = new StringBuilder();
    private TextTemplate nowTemplate;
    private List<ChatRequest.Message> messageList;
    private android.app.AlertDialog dialog;
    private boolean isFinish = false;
    private boolean isContinuous = true;    // 连续对话
    private String AiName = "小助手";
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
    @OnClick({R.id.saveContent,R.id.optimizeContent})
    public void myClickListener(View view) {
        switch (view.getId()){
            case R.id.saveContent:
                if (isFinish){

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
        }
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
            textTitle.setText(getString(R.string.editor_create_act_hint1));
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
            String s_prompt = promptText.getText().toString();
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
