package com.skythinker.gptassistant.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class ChatRequest {
    @JsonProperty("messages")
    private List<Message> messages;

    @JsonProperty("model")
    private String model;

    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("max_tokens")
    private int maxTokens;

    @JsonProperty("presence_penalty")
    private double presencePenalty;

    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    @JsonProperty("stop")
    private Object stop;

    @JsonProperty("stream")
    private boolean stream;

    @JsonProperty("stream_options")
    private Object streamOptions;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("top_p")
    private double topP;

    @JsonProperty("tools")
    private Object tools;

    @JsonProperty("tool_choice")
    private String toolChoice;

    @JsonProperty("logprobs")
    private boolean logprobs;

    @JsonProperty("top_logprobs")
    private Object topLogprobs;

    public ChatRequest(List<Message> messages) {
        this.messages = messages;
        this.model = "deepseek-chat";
        this.frequencyPenalty = 0;
        this.maxTokens = 2048;
        this.presencePenalty = 0;
        this.responseFormat = new ResponseFormat("text");
        this.stop = null;
        this.stream = false;
        this.streamOptions = null;
        this.temperature = 1;
        this.topP = 1;
        this.tools = null;
        this.toolChoice = "none";
        this.logprobs = false;
        this.topLogprobs = null;
    }

    // JSON 序列化
    public String toJson() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsString(this);
    }

    // 消息类
    public static class Message {
        @JsonProperty("content")
        private String content;

        @JsonProperty("role")
        private String role;

        @JsonProperty("name")
        private String name;

        public Message(String content, String role, String name) {
            this.content = content;
            this.role = role;
            this.name = name;
        }
    }

    // 响应格式类
    public static class ResponseFormat {
        @JsonProperty("type")
        private String type;

        public ResponseFormat(String type) {
            this.type = type;
        }
    }
}

