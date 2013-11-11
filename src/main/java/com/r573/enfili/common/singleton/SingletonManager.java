package com.r573.enfili.common.singleton;

import java.util.Hashtable;

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

/**
 * Manages Singletons for the entire app.
 * 
 * @author ryanho
 *
 */
public class SingletonManager {
	private static SingletonManager instance;
	
	private Hashtable<String,ISingleton> instanceTable;
	
	public static SingletonManager getInstance(){
		if(instance == null){
			instance = new SingletonManager();
		}
		return instance;
	}
	
	public SingletonManager(){
		instanceTable = new Hashtable<String, ISingleton>();
	}
	
	public <T extends ISingleton>T getService(Class<T> serviceClass) {
		@SuppressWarnings("unchecked")
		T serviceInstance = (T) instanceTable.get(serviceClass.getName());
		if(serviceInstance == null) {
			try {
				serviceInstance = serviceClass.newInstance();
			}
			catch (InstantiationException e) {
				// Can only be caused by programming error. Throwing RuntimException.
				throw new RuntimeException(e);
			}
			catch (IllegalAccessException e) {
				// Can only be caused by programming error. Throwing RuntimException.
				throw new RuntimeException(e);
			}
		}
		return serviceInstance;
	}
}
