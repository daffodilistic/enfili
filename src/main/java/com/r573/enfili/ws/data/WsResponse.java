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
package com.r573.enfili.ws.data;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

// Notes: javax.ws.rs.core.Response, ClientResponse has status.
@JsonIgnoreProperties(ignoreUnknown = true)
public class WsResponse<T> {
	
	public static final String RESP_CODE_OK = "OK";
	public static final String RESP_CODE_ERROR = "ERR";
	
	private String statusCode;
	private List<T> responseDataArray;
	
	public WsResponse() {
	}
	
	public WsResponse(String statusCode, T responseData) {
		this.statusCode = statusCode;
		this.responseDataArray = new ArrayList<T>();
		responseDataArray.add(responseData);
	}

	public WsResponse(String statusCode, List<T> responseDataArray) {
		this.statusCode = statusCode;
		this.responseDataArray = responseDataArray;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	@JsonIgnore
	public T getResponseData() {
		if((responseDataArray != null) && (!responseDataArray.isEmpty())){
			return responseDataArray.get(0);
		}
		else {
			return null;
		}
	}

	public List<T> getResponseDataArray() {
		return responseDataArray;
	}	
}
