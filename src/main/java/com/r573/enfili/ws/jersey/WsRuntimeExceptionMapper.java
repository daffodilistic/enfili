package com.r573.enfili.ws.jersey;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r573.enfili.common.doc.json.JsonHelper;
import com.r573.enfili.common.exception.WsRuntimeException;
import com.r573.enfili.ws.data.WsError;
import com.r573.enfili.ws.data.WsResponse;

@Provider
public class WsRuntimeExceptionMapper implements ExceptionMapper<WsRuntimeException> {
	private static final Logger log = LoggerFactory.getLogger(WsRuntimeExceptionMapper.class);

	@Override
	public Response toResponse(WsRuntimeException e) {
		log.error(ExceptionUtils.getStackTrace(e));		
		WsResponse<WsError> errorResp = new WsResponse<WsError>(WsResponse.RESP_CODE_ERROR, new WsError(e.getErrCode(), e.getErrDesc()));
		String errorJson = JsonHelper.toJson(errorResp);
		GenericEntity<String> respEntity = new GenericEntity<String>(errorJson) {};
		return Response.status(Status.OK).entity(respEntity).type(MediaType.APPLICATION_JSON).build();
	}
}
