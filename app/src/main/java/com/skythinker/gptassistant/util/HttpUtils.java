package com.skythinker.gptassistant.util;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者: jiang
 * http请求方法
 * @date: 2024/10/25
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils";

    // get请求
    public static void sendGetRequest(String urlString, final HttpCallback<ArrayList<String>> callback) {

        OkHttpClient client = new OkHttpClient();
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
                try {
                    String responseString = response.body().string();
                    Log.d(TAG, "Response: " + responseString);

                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray resultArray = jsonObject.getJSONArray("result");

                    ArrayList<String> resultArrayList = new ArrayList<>();
                    int length = resultArray.length();
                    for (int i = 0; i < length; i++) {
                        String item = resultArray.getString(i);
                        resultArrayList.add(item);
                    }

                    callback.onSuccess(resultArrayList);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON parse failed: " + e.getMessage());
                    callback.onFailure(e);
                }
            }
        });
    }

    // post传参 地址、json值，回调
    public static void sendPostRequest(String urlString, String requestBody, final HttpCallback<String> callback) {


        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"),//设置数据类型
                requestBody//数据
        );
        Request request = new Request.Builder()
                .url(urlString)
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
                    try {
                        String responseString = response.body().string();
                        Log.d(TAG, "Response: " + responseString);
                        callback.onSuccess(responseString);
                    } catch (Exception e) {
                        Log.e(TAG, "JSON parse failed: " + e.getMessage());
                        callback.onFailure(e);
                    }
                }
            }
        });
    }

    public interface HttpCallback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }
}
