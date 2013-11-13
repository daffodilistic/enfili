/*
 * Enfili
 * Project hosted at https://github.com/ryanhosp/enfili/
 * Copyright 2013 Ho Siaw Ping Ryan
 *    
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *   
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.r573.enfili.ws.jersey;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r573.enfili.common.doc.json.JsonHelper;
import com.r573.enfili.common.text.StringHelper;
import com.r573.enfili.ws.data.WsError;
import com.r573.enfili.ws.data.WsResponse;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<Exception> {

	private static final Logger log = LoggerFactory.getLogger(GeneralExceptionMapper.class);

	@Override
	public Response toResponse(Exception e) {
		log.error("Exception " + e.getClass().getName() + " caught in GeneralExceptionMapper");
		log.error(StringHelper.stackTraceToString(e));
		GenericEntity<String> entity = new GenericEntity<String>(JsonHelper.toJson(new WsResponse<WsError>(WsResponse.RESP_CODE_ERROR, new WsError(WsError.GENERAL_ERROR, "General Error")))) {
		};
		return Response.status(Status.OK).entity(entity).type(MediaType.APPLICATION_JSON).build();
	}
}
