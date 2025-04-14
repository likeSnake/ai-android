package com.skythinker.gptassistant.util;

import static com.skythinker.gptassistant.util.MyUtil.APP_BASE_URL;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mmkv.MMKV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: jiang
 * http请求方法
 * @date: 2024/10/25
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils请求";

    // get请求
    public static void sendGetRequestNormal(String urlString, final HttpCallback<String> callback) {
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时为10秒
                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时为30秒
                .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时为30秒
                .build();

        Request request = new Request.Builder()
                .url(urlString)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(TAG, "Response: " + responseString);
                callback.onSuccess(responseString);

            }
        });
    }

    // get请求
    public static void sendGetRequest(String urlString, final HttpCallback<String> callback) {
        String url = APP_BASE_URL+urlString;
        String token = MMKV.defaultMMKV().decodeString(MyUtil.ACCESS_TOKEN,"");
        //OkHttpClient client = new OkHttpClient();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时为10秒
                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时为30秒
                .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时为30秒
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e.getMessage());
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseString = response.body().string();
                Log.d(TAG, "Response: " + responseString);
                callback.onSuccess(responseString);

            }
        });
    }

    // post传参 地址、json值，回调
    public static void sendPostRequest(String urlString, String requestBody, final HttpCallback<String> callback) {
        String url = APP_BASE_URL+urlString;
        String token = MMKV.defaultMMKV().decodeString(MyUtil.ACCESS_TOKEN,"");
        //OkHttpClient client = new OkHttpClient();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时为10秒
                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时为30秒
                .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时为30秒
                .build();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"),//设置数据类型
                requestBody//数据
        );
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e);
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String responseString = response.body().string();
                    Log.d(TAG, "Response: " + responseString);
                    callback.onSuccess(responseString);
                }
            }
        });
    }
    // post请求 无参数
    public static void sendPostRequest(String urlString, final HttpCallback<String> callback) {
        String url = APP_BASE_URL+urlString;
        String token = MMKV.defaultMMKV().decodeString(MyUtil.ACCESS_TOKEN,"");
        //OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization",token)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时为10秒
                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时为30秒
                .writeTimeout(30, TimeUnit.SECONDS) // 设置写入超时为30秒
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Request failed: " + e);
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response!=null) {
                    String responseString = response.body().string();
                    Log.d(TAG, "Response: " + responseString);
                    callback.onSuccess(responseString);
                }
            }
        });
    }

    public interface HttpCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}
