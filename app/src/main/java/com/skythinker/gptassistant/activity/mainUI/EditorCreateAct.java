package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.entity.base.ChatStreamClient.sendChatStream;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.ChatRequest;
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
    private StringBuilder markdownContent = new StringBuilder();
    private TextTemplate nowTemplate;
    private List<ChatRequest.Message> messageList;
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

    public void showContentDialog(){

    }

    public void sendQuestion(String msg){
        ChatRequest.Message systemMessage = new ChatRequest.Message(nowTemplate.getPrompt(), "system", "小助手");
        ChatRequest.Message userMessage = new ChatRequest.Message(msg, "user", "文案工作者");

        messageList.add(systemMessage);
        messageList.add(userMessage);

        ChatRequest chatRequest = new ChatRequest(messageList);

        Markwon markwon = Markwon.create(EditorCreateAct.this);
        sendChatStream(chatRequest, markdownText ->
                runOnUiThread(() -> {
                    markdownContent.append(markdownText);
                    markwon.setMarkdown(myText, markdownContent.toString());
                })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
