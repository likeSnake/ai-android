package com.skythinker.gptassistant.activity.mainUI;

import static com.skythinker.gptassistant.entity.base.ChatStreamClient.sendChatStream;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skythinker.gptassistant.R;
import com.skythinker.gptassistant.entity.base.ChatRequest;

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
import java.util.Arrays;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        unbinder = ButterKnife.bind(this);
        initData();
    }

    public void initData(){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        sendChatStream("给我一周健康食谱", System.out::println);

                        //String data = sendDeepseekChat(DEEPSEEK_API_URL_COMPLETIONS, "如何学习Java代码");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
    private static final String DEEPSEEK_API_URL_COMPLETIONS = "https://api.deepseek.com/chat/completions"; // API地址 ——
    // 对话补全

    private static final String DEEPSEEK_API_KEY = "sk-1df1477409e347d98951ecac5277cf82"; // 官网申请的api key


    public String sendDeepseekChat(String deepseekUrl, String context) throws IOException {
        String result = "";

        URL url_req = new URL(deepseekUrl);

        HttpsURLConnection connection = (HttpsURLConnection) url_req.openConnection();

        // 设置参数
        connection.setDoOutput(true); // 需要输出
        connection.setDoInput(true); // 需要输入
        connection.setUseCaches(false); // 不允许缓存
        connection.setConnectTimeout(60000); // 设置连接超时
        connection.setReadTimeout(60000); // 设置读取超时
        connection.setRequestMethod("POST"); // 设置POST方式连接

        // 设置请求属性
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Charset", "UTF-8");

        // 设置请求头参数
        connection.addRequestProperty("Authorization", "Bearer " + DEEPSEEK_API_KEY); // 设置appId

        HttpsURLConnection https = (HttpsURLConnection) connection;
        SSLSocketFactory oldSocketFactory = trustAllHosts(https);
        HostnameVerifier oldHostnameVerifier = https.getHostnameVerifier();
        https.setHostnameVerifier(DO_NOT_VERIFY);

        ChatRequest.Message systemMessage = new ChatRequest.Message("你是", "system", "小助手");
        ChatRequest.Message userMessage = new ChatRequest.Message(context, "user", "路人甲");

        ChatRequest chatRequest = new ChatRequest(Arrays.asList(systemMessage, userMessage));

        ObjectMapper objectMapper = new ObjectMapper();
        String requestData = objectMapper.writeValueAsString(chatRequest);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestData.getBytes("utf-8");
            os.write(input,0,input.length);
        }

        // 输出数据
        InputStream in = connection.getInputStream(); // 获取返回数据
        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        while (-1 != (c = bis.read())) {
            baos.write(c);
        }
        bis.close();
        in.close();
        baos.flush();

        byte[] data = baos.toByteArray();
        String responseMsg = new String(data);
        System.out.println(responseMsg);

        JSONObject jsonObject = new JSONObject(responseMsg);

        if(jsonObject.containsKey("choices") && !jsonObject.getJSONArray("choices").isEmpty()) {
            JSONObject delta = ((JSONObject) jsonObject.getJSONArray("choices").get(0)).getJSONObject("message");
        }


        return "result";
    }

    private SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }

    private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    } };

    private HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

}
