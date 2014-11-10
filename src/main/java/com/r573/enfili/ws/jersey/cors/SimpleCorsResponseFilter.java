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
package com.r573.enfili.ws.jersey.cors;

import javax.ws.rs.core.MultivaluedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Simple CORS response filter with default settings for the enfili REST stack
 * 
 * @author ryanho
 *
 */
public class SimpleCorsResponseFilter implements ContainerResponseFilter {
	private final Logger log = LoggerFactory.getLogger(SimpleCorsResponseFilter.class);
	
	@Override
	public ContainerResponse filter(ContainerRequest req, ContainerResponse resp) {
		String requestOrigin = req.getHeaderValue("Origin");
        if (requestOrigin == null) {
    		log.debug("SimpleCorsResponseFilter.filter no origin");
            return resp;
        }
        else{
    		log.debug("SimpleCorsResponseFilter.filter with origin "+requestOrigin);

    		MultivaluedMap<String, Object> headers = resp.getHttpHeaders();
            
            headers.add("Access-Control-Allow-Origin", requestOrigin);
            headers.add("Access-Control-Allow-Credentials", "true");
            headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            return resp;	
        }		
 	}

}
