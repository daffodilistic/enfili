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

package com.r573.enfili.common.doc.json;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelper {

	public static <T> T fromJson(String jsonStr, Class<T> objClass) {
		ObjectMapper jsonMapper = new ObjectMapper();
		JavaType javaType = jsonMapper.getTypeFactory().constructType(objClass);
		try {
			return jsonMapper.readValue(jsonStr, javaType);
		} catch (JsonParseException e) {
			throw new JsonProcessingException(e, jsonStr);
		} catch (JsonMappingException e) {
			throw new JsonProcessingException(e, jsonStr);
		} catch (IOException e) {
			// this is impossible. We are parsing a string, not an InputStream.
			throw new RuntimeException(e);
		}
	}
	public static <T> T fromJson(String jsonStr, JavaType javaType) {
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			return jsonMapper.readValue(jsonStr, javaType);
		} catch (JsonParseException e) {
			throw new JsonProcessingException(e, jsonStr);
		} catch (JsonMappingException e) {
			throw new JsonProcessingException(e, jsonStr);
		} catch (IOException e) {
			// this is impossible. We are parsing a string, not an InputStream.
			throw new RuntimeException(e);
		}
	}
	
	public static Map<String, Object> convertToMap(Object obj) {
		ObjectMapper jsonMapper = new ObjectMapper();
		TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
		};
		return jsonMapper.convertValue(obj, typeRef);
	}
	
	public static String toJson(Object obj) {
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			return jsonMapper.writeValueAsString(obj);
		} catch (JsonMappingException e) {
			throw new JsonProcessingException(e, obj);
		} catch (JsonGenerationException e) {
			throw new JsonProcessingException(e, obj);
		} catch (IOException e) {
			// this is impossible. We are writing a string, not an InputStream.
			throw new RuntimeException(e);
		}
	}	
}
