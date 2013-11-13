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
package com.r573.enfili.common.resource.db.mongo;

import java.net.UnknownHostException;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

public class MorphiaDbManager {
	public static final String ERR_DB_WRITE_FAILURE = "MD-001";
	
	private static MorphiaDbManager instance;
		
	private Datastore ds;
	
	public static void initInstance(String serverUrl, String serverName, List<Class<? extends BaseMongoObject>> mappedClasses){
		if(instance != null){
			throw new IllegalStateException("MorphiaDbManager already initialized");
		}
		instance = new MorphiaDbManager(serverUrl,serverName,mappedClasses);
	}
	
	public static MorphiaDbManager getInstance(){
		return instance;
	}
	
	private MorphiaDbManager(String serverUrl, String serverName, List<Class<? extends BaseMongoObject>> mappedClasses){
		Mongo mongo;
		Morphia morphia;
		try {
			if(serverUrl == null) {
				serverUrl = "127.0.0.1";
			}
			mongo = new MongoClient(serverUrl);
			morphia = new Morphia();
			ds = morphia.createDatastore(mongo, serverName);
			for (Class<? extends BaseMongoObject> mappedClass : mappedClasses) {
				morphia.map(mappedClass);
			}
		}
		catch (UnknownHostException e) {
			// TODO: throw RuntimeException until we actually have a plan for handling this
			throw new RuntimeException(e);
		}
	}
	
	public <T extends BaseMongoObject>T insert(T obj){
		ds.save(obj);
		obj.setId(obj.getObjectId().toString());
		return obj;
	}
}
