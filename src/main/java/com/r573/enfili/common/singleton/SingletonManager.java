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

package com.r573.enfili.common.singleton;

import java.util.HashMap;

/**
 * All instances of the Singleton should be created during the application bootstrap.
 * This is to minimize having to synchronize and having to check for an existing instance
 * every time an operation is requested.
 * 
 * @author ryanho
 *
 * @param <T>
 */
public class SingletonManager<T> {
	private static final String DEFAULT_TAG = "default";

	private HashMap<String,T> instances;
	
	public SingletonManager(){
		 instances = new HashMap<String,T>();
	}
	
	public void addInstance(String tag, T instance){
    	if(tag == null) {
    		tag = DEFAULT_TAG;
    	}
    	if(instances.containsKey(tag)){
    		throw new IllegalStateException("Instance with tag " + tag + " already exists. Please remove before adding.");
    	}
    	instances.put(tag,instance);	
	}
	
    public boolean removeInstance(String tag){
    	T removedInstance = instances.remove(tag);
    	if(removedInstance != null) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }

    public T getDefaultInstance(){
    	T instance = instances.get(DEFAULT_TAG);
    	if(instance == null){
    		throw new IllegalStateException("Default instance not yet created.");
    	}
    	return instance;
    }
    public T getInstance(String tag) {
    	T instance = instances.get(tag);
    	if(instance == null){
    		throw new IllegalArgumentException("Instance with tag " + tag + " does not exist.");
    	}
    	return instance;
    }

    public boolean hasInstance(String tag){
    	return instances.containsKey(tag);
    }
}
