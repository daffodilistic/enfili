package com.r573.enfili.common.exception;

@SuppressWarnings("serial")
public class BaseRuntimeException extends RuntimeException{

	private String errCode;
	private String errDesc;
	
	public BaseRuntimeException(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
	public BaseRuntimeException(String errCode) {
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getErrDesc() {
		return errDesc;
	}

	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}
}
