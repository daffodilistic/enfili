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

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

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
	
	public static String toJson(Object obj, boolean prettyPrint) {
		try {
			if(prettyPrint){
				ObjectWriter jsonWriter = (new ObjectMapper()).writerWithDefaultPrettyPrinter();
				return jsonWriter.writeValueAsString(obj);
			}
			else{
				ObjectMapper jsonMapper = new ObjectMapper();
				return jsonMapper.writeValueAsString(obj);
			}
		} catch (JsonMappingException e) {
			throw new JsonProcessingException(e, obj);
		} catch (JsonGenerationException e) {
			throw new JsonProcessingException(e, obj);
		} catch (IOException e) {
			// this is impossible. We are writing a string, not an InputStream.
			throw new RuntimeException(e);
		}
	}
	public static String toJson(Object obj) {
		return toJson(obj, false);
	}	
}
