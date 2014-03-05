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
package com.r573.enfili.common.cache.simple;

import java.util.Date;

public class CacheItem<T> {
	private T cachedObject;
	private Date timestamp;
	private Date expiry;
	
	public CacheItem(T obj, long lifespan){
		cachedObject = obj;
		timestamp = new Date();
		expiry = new Date(timestamp.getTime() + lifespan);
	}
	
	/**
	 * Constructor allowing for fixed expiry time (e.g. end of business day)
	 * 
	 * @param obj
	 * @param expiry
	 */
	public CacheItem(T obj, Date expiry){
		cachedObject = obj;
		timestamp = new Date();
		this.expiry = expiry;
	}

	public boolean hasExpired(){
		Date now = new Date();
		if(now.after(expiry)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public T getCachedObject() {
		return cachedObject;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Date getExpiry() {
		return expiry;
	}
}
