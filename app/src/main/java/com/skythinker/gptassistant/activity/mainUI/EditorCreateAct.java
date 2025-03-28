package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.entity.base.ChatStreamClient.sendChatStream;

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
        showContentDialog();
        /*thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //sendChatStream("给我一周健康食谱", System.out::println);
                    //sendChatStream("给我一周健康食谱,我在减脂，年龄23，男", text -> runOnUiThread(() -> myText.append(text)));
                    //String data = sendDeepseekChat(DEEPSEEK_API_URL_COMPLETIONS, "如何学习Java代码");
                    // 发送流式请求
                    sendChatStream("给我一周健康食谱,我在减脂，年龄23，男", markdownText ->
                            runOnUiThread(() -> {
                                Markwon markwon = Markwon.create(EditorCreateAct.this);
                                markwon.setMarkdown(myText, markdownText);
                            })
                    );
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();*/
    }

    @OnClick({R.id.saveContent,R.id.optimizeContent})
    public void myClickListener(View view) {
        switch (view.getId()){
            case R.id.saveContent:

                break;
            case R.id.optimizeContent:
                if (isFinish){

                }else {
                    MyToastUtil.showError("请等待文案生成");
                }
                break;
        }
    }

    public void showContentDialog(){
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

        Button close = view.findViewById(R.id.dialogInputHClose);
        Button submit = view.findViewById(R.id.dialogInputHSubmit);
        EditText promptText = view.findViewById(R.id.promptText);

        close.setOnClickListener((v)->{});
        submit.setOnClickListener((v)->{
            String s_prompt = promptText.getText().toString();
            if (s_prompt.isEmpty()){
                MyToastUtil.showError("请先输入提示内容");
                return;
            }
            sendQuestion(s_prompt);
            dialog.dismiss();
        });
    }

    public void sendQuestion(String msg){
        showLoading();
        ChatRequest.Message systemMessage = new ChatRequest.Message(nowTemplate.getPrompt(), "system", "小助手");
        ChatRequest.Message userMessage = new ChatRequest.Message(msg, "user", "文案工作者");

        messageList.add(systemMessage);
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
                onContentFinish();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
