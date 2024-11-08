package com.skythinker.gptassistant.entity.base;

import java.io.Serializable;
import java.util.List;

public class BaseListEntity<T> implements Serializable {
	public int code;
	public String msg;
	public List<T> data;
	public int totalPage;//总页数

	public boolean isSuccess(){
		return code == 0;
	}

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

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
