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
package com.r573.enfili.common.resource.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r573.enfili.common.doc.json.JsonHelper;
import com.r573.enfili.common.io.file.FileHelper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class RestClient {

	private final Logger log = LoggerFactory.getLogger(RestClient.class);
	
	private Client jerseyClient;
	private String baseUrl;
	private Map<String, Cookie> cookies;

	public RestClient(String baseUrl) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		jerseyClient = Client.create(clientConfig);
		cookies = new HashMap<String, Cookie>();
		this.baseUrl = baseUrl;
	}

	private WebResource.Builder getResource(String path) {
		String resourcePath = FileHelper.combinePath(baseUrl,path);
		log.debug("Creating resource path: " + resourcePath);
		WebResource.Builder builder = jerseyClient.resource(resourcePath).type(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE);
		Set<String> cookieNames = cookies.keySet();
		for (String cookieName : cookieNames) {
			builder = builder.cookie(cookies.get(cookieName));
		}
		return builder;
	}

	public <T> ResponseWrapper<T> get(String path, Class<T> clazz) {
		ClientResponse response = getResource(path).get(ClientResponse.class);
		return processResponse(response, clazz);
	}

	public <T> ResponseWrapper<T> post(String path, Object postObj, Class<T> clazz) {
		String postObjJson = JsonHelper.toJson(postObj);
		ClientResponse response = getResource(path).post(ClientResponse.class, postObjJson);
		log.debug("status=" + response.getStatus());
		return processResponse(response, clazz);
	}

	public <T> ResponseWrapper<T> put(String path, Object postObj, Class<T> clazz) {
		String postObjJson = JsonHelper.toJson(postObj);
		ClientResponse response = getResource(path).put(ClientResponse.class, postObjJson);
		return processResponse(response, clazz);
	}

	public <T> ResponseWrapper<T> delete(String path, Class<T> clazz) {
		ClientResponse response = getResource(path).delete(ClientResponse.class);
		return processResponse(response, clazz);
	}
	
	private <T> ResponseWrapper<T> processResponse(ClientResponse response, Class<T> clazz) {		
		List<NewCookie> newCookies = response.getCookies();
		for (NewCookie newCookie : newCookies) {
			cookies.put(newCookie.getName(), newCookie);
		}

		String wsResponseJson = response.getEntity(String.class);
		log.debug(wsResponseJson);

		@SuppressWarnings({ "unchecked", "rawtypes" })
		ResponseWrapper<T> wsResponse = new ResponseWrapper(JsonHelper.fromJson(wsResponseJson, clazz));
		return wsResponse;
	}

}
