package com.hxx.sbcommon.model;

import java.io.Serializable;


public class HttpResEntity implements Serializable {
	private static final long serialVersionUID = -5716617440635510066L;
	private Integer status;
	private String data;
	private String msg;

	// 提示
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 状态为200成功
	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	// 返回结果
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
