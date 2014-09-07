package com.r573.enfili.ws.client;

import com.r573.enfili.common.exception.WsException;
import com.r573.enfili.ws.data.WsError;

public class WsRestException extends WsException {
	private static final long serialVersionUID = 1L;

	public WsRestException(WsError responseData) {
		super(responseData.getErrorCode(),responseData.getErrorDesc());
	}

}
