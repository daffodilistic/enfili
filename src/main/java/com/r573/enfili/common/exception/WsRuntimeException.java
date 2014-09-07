package com.r573.enfili.common.exception;

public class WsRuntimeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	private String errCode;
	private String errDesc;
	
	public WsRuntimeException(String errCode, String errDesc) {
		this.errCode = errCode;
		this.errDesc = errDesc;
	}
	public WsRuntimeException(String errCode) {
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
	
	@Override
	public String getMessage(){
		return errDesc + "("+errCode + ")";
	}
}
