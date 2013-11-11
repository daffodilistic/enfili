package com.r573.enfili.common.json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class JsonProcessingException extends RuntimeException {
	private static final Logger log = LoggerFactory.getLogger(JsonProcessingException.class);
	
	private static final long serialVersionUID = 1L;

	public JsonProcessingException(Throwable t, String jsonDocument) {
		super(t);
		log.error(this.getClass().getName() + " while parsing document\n" + jsonDocument);
	}
	public JsonProcessingException(Throwable t, Object jsonObject) {
		super(t);
		String objDesc = "null";
		if(jsonObject != null) {
			objDesc = jsonObject.getClass().getName();
		}
		log.error(this.getClass().getName() + " while processing object\n" + objDesc);
	}
}
