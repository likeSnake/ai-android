package com.skythinker.gptassistant.entity.base;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import okhttp3.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ChatStreamClient {
    private static final String API_URL = "https://api.deepseek.com/chat/completions";
    private static final String API_KEY = "sk-1df1477409e347d98951ecac5277cf82";
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // 连接超时 30 秒
            .readTimeout(60, TimeUnit.SECONDS)     // 读取超时 60 秒
            .writeTimeout(60, TimeUnit.SECONDS)    // 写入超时 60 秒
            .retryOnConnectionFailure(true)        // 允许自动重试
            .build();

    /*public static void sendChatStream(String context, Consumer<String> callback) {
        ChatRequest.Message systemMessage = new ChatRequest.Message("你是", "system", "小助手");
        ChatRequest.Message userMessage = new ChatRequest.Message(context, "user", "路人甲");
        ChatRequest chatRequest = new ChatRequest(Arrays.asList(systemMessage, userMessage));

        String jsonRequest = new Gson().toJson(chatRequest);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonRequest))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (responseBody == null) return;

                    BufferedReader reader = new BufferedReader(responseBody.charStream());
                    String line;
                    while ((line = reader.readLine()) != null) {
                        callback.accept(line); // 逐步处理响应
                    }
                }
            }
        });
    }*/
    public static void sendChatStream(ChatRequest chatRequest, Consumer<String> callback) {

        String jsonRequest = gson.toJson(chatRequest);

        Request request = new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(MediaType.get("application/json; charset=utf-8"), jsonRequest))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.accept("Request failed: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.accept("Error: " + response.code() + " " + response.message());
                    return;
                }

                try (ResponseBody responseBody = response.body();
                     BufferedReader reader = new BufferedReader(responseBody.charStream())) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.startsWith("data:")) {
                            String jsonString = line.substring(5).trim();
                                try {
                                    JsonObject jsonObject = gson.fromJson(jsonString.toString(), JsonObject.class);
                                    JsonArray choices = jsonObject.getAsJsonArray("choices");
                                    if (choices != null && choices.size() > 0) {
                                        JsonObject delta = choices.get(0).getAsJsonObject().getAsJsonObject("delta");
                                        if (delta != null && delta.has("content")) {
                                            String content = delta.get("content").getAsString();
                                            callback.accept(content);
                                        }
                                    }
                                } catch (JsonSyntaxException e) {
                                    System.err.println("JSON 解析错误: " + e.getMessage());
                                }
                                //callback.accept(jsonBuffer.toString());
                                //jsonBuffer.setLength(0);
                        }
                    }
                }
            }
        });
    }
}
