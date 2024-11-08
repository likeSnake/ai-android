package com.skythinker.gptassistant.entity.base;

import java.io.Serializable;

public class BaseEntity<T> implements Serializable {
	public int code;
	public String msg;
	public T data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
