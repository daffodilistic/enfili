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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

public class MorphiaDbManager {
	private static Logger log = LoggerFactory.getLogger(MorphiaDbManager.class);
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
		MongoClient mongo;
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
		catch (Exception e) {
			// TODO: throw RuntimeException until we actually have a plan for handling this
			throw new RuntimeException(e);
		}
	}
	
	public Datastore getDataStore(){
		return ds;
	}
	
	public <T extends BaseMongoObject>T save(T obj){
		ds.save(obj);
		setId(obj);
		return obj;
	}

	public <T extends BaseMongoObject>T update(T obj){
		obj.setObjectId(new ObjectId(obj.getId()));
		obj.setId(null);
		ds.save(obj);
		setId(obj);
		return obj;
	}

	public <T extends BaseMongoObject>T get(Class<T> clazz,String collection, String id){
		T obj = ds.getByKey(clazz, makeKey(clazz, collection, id));
		if(obj!=null){
			setId(obj);			
		}
		return obj;
	}
	
	public <T extends BaseMongoObject> void updateField(Class<T> clazz, String collection, String id, String fieldName, Object fieldData) {
		log.debug("updateField " + fieldName + " for type "+clazz.getName()+" id "+id+" with fieldData " + fieldData.toString());
		UpdateOperations<T> ops = ds.createUpdateOperations(clazz).set(fieldName, fieldData);
		UpdateResults results = ds.update(makeKey(clazz, collection, id), ops);
		log.debug("UpdatedCount " + results.getUpdatedCount());
		//if(results.getHadError()){
			//throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, results.getError());
		//}
	}
	
	public <T extends BaseMongoObject> void deleteField(Class<T> clazz, String collection, String id, String fieldName) {
		log.debug("deleteField " + fieldName + " for type "+clazz.getName()+" id "+id);
		UpdateOperations<T> ops = ds.createUpdateOperations(clazz).unset(fieldName);
		UpdateResults results = ds.update(makeKey(clazz, collection, id), ops);
		log.debug("UpdatedCount " + results.getUpdatedCount());
		//if(results.getHadError()){
			//throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, results.getError());
		//}		
	}
	
	public <T extends BaseMongoObject> void addToArray(Class<T> clazz, String collection, String id, String fieldName, Object item) {
		log.debug("addToArray " + fieldName + " for type "+clazz.getName()+" id "+id+" with item " + item.toString());
		UpdateOperations<T> ops = ds.createUpdateOperations(clazz).add(fieldName, item);
		UpdateResults results = ds.update(makeKey(clazz, collection, id), ops);
		log.debug("UpdatedCount " + results.getUpdatedCount());
		//if(results.getHadError()){
			//throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, results.getError());
		//}
	}
	public <T extends BaseMongoObject> void removeFromArray(Class<T> clazz, String collection, String id, String fieldName, Object item) {
		log.debug("removeFromArray " + fieldName + " for type "+clazz.getName()+" id "+id+" with item " + item.toString());
		UpdateOperations<T> ops = ds.createUpdateOperations(clazz).removeAll(fieldName, item);
		UpdateResults results = ds.update(makeKey(clazz, collection, id), ops);
		log.debug("UpdatedCount " + results.getUpdatedCount());
		//if(results.getHadError()){
			//throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, results.getError());
		//}
	}
	public <T extends BaseMongoObject> void replaceItemInArray(Class<T> clazz, String collection, String id, String fieldName, Object oldItem, Object newItem) {
		log.debug("replaceItemInArray " + fieldName + " for type "+clazz.getName()+" id "+id+" with oldItem " + oldItem.toString() + " newItem " + newItem.toString());
		UpdateOperations<T> ops = ds.createUpdateOperations(clazz).removeAll(fieldName, oldItem);
		UpdateResults results = ds.update(makeKey(clazz, collection, id), ops);
		log.debug("UpdatedCount " + results.getUpdatedCount());
		//if(results.getHadError()){
			//throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, results.getError());
		//}
		//else{
			UpdateOperations<T> ops2 = ds.createUpdateOperations(clazz).add(fieldName, newItem);
			UpdateResults results2 = ds.update(makeKey(clazz, collection, id), ops2);
			log.debug("UpdatedCount2 " + results2.getUpdatedCount());
			//if(results2.getHadError()){
				//throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, results2.getError());
			//}
		//}
	}
	
	public <T extends BaseMongoObject,V extends BaseMongoObject> void mergeIntoArray(Class<T> clazz, String collection, String id, String fieldName, List<V> originalArray, List<V> mergeArray) {
		for (V mergeItem : mergeArray) {
			if(mergeItem.getId() == null){
				mergeItem.setId(generateObjectIdString());
				originalArray.add(mergeItem);
			}
			else{
				int index = -1;
				for (int i=0; i<originalArray.size(); i++) {
					if(mergeItem.getId().equals(originalArray.get(i).getId())){
						index = i;
						break;
					}
				}
				if(index != -1){
					originalArray.set(index, mergeItem);
				}
			}
		}
		updateField(clazz, collection, id, fieldName, originalArray);
	}
	public <T extends BaseMongoObject> List<T> findAndRetrieveField(Class<T> clazz,String queryField, String queryValue, String field){
		log.debug("findAndRetrieveField for type "+clazz.getName()+" queryField " + queryField + " searchTerm " + queryValue + " retrieve field " + field);
		Query<T> query = ds.find(clazz,queryField,queryValue).retrievedFields(true, field);
		return find(query);
	}
	public <T extends BaseMongoObject> T findOneAndRetrieveField(Class<T> clazz,String queryField, String queryValue, String field){
		log.debug("findAndRetrieveField for type "+clazz.getName()+" queryField " + queryField + " searchTerm " + queryValue + " retrieve field " + field);
		Query<T> query = ds.find(clazz,queryField,queryValue).retrievedFields(true, field);
		return findOne(query);
	}
	public <T extends BaseMongoObject> T getAndRetrieveField(Class<T> clazz,String id, String field){
		log.debug("getAndRetrieveField for type "+clazz.getName()+" id " + id + " retrieve field " + field);
		Query<T> query = ds.find(clazz,"_id",new ObjectId(id)).retrievedFields(true, field);
		return findOne(query);
	}
	public <T extends BaseMongoObject> T getAndRetrieveFields(Class<T> clazz,String id, ArrayList<String> fieldList){
		log.debug("getAndRetrieveField for type "+clazz.getName()+" id " + id + " retrieve fieldList size " + fieldList.size());
		Query<T> query = ds.find(clazz,"_id",new ObjectId(id)).retrievedFields(true, fieldList.toArray(new String[fieldList.size()]));
		return findOne(query);
	}
	public <T extends BaseMongoObject> List<T> find(Class<T> clazz,String queryField, String queryValue){
		log.debug("find for type "+clazz.getName()+" queryField " + queryField + " searchTerm " + queryValue);
		Query<T> query = ds.find(clazz,queryField,queryValue);
		return find(query);
	}
	public <T extends BaseMongoObject> List<T> find(Query<T> query){
		List<T> queryResults = query.asList();
		for (T obj : queryResults) {
			setId(obj);
		}
		
		return queryResults;
	}
	public <T extends BaseMongoObject> T findOne(Query<T> query){
		List<T> queryResults = find(query);
		if((queryResults != null) && (queryResults.size()>0)){
			return queryResults.get(0);
		}
		else{
			return null;
		}		
	}
	public <T extends BaseMongoObject> T findOne(Class<T> clazz,String queryField, String queryValue){
		List<T> queryResults = find(clazz,queryField,queryValue);
		if((queryResults != null) && (queryResults.size()>0)){
			return queryResults.get(0);
		}
		else{
			return null;
		}
	}
	public <T extends BaseMongoObject> List<T> find(Class<T> clazz,String queryField,Pattern regex){
		log.debug("find for type "+clazz.getName()+" queryField " + queryField + " regex " + regex.pattern());
		Query<T> query = ds.find(clazz,queryField,regex);
		return find(query);
	}
	public <T extends BaseMongoObject> List<T> findAndRetrieveFields(Class<T> clazz,String queryField,Pattern regex, ArrayList<String> fieldList){
		log.debug("find for type "+clazz.getName()+" queryField " + queryField + " regex " + regex.pattern() + " with fields");
		Query<T> query = ds.find(clazz,queryField,regex).retrievedFields(true, fieldList.toArray(new String[fieldList.size()]));
		return find(query);
	}
	public <T extends BaseMongoObject> List<T> findAll(Class<T> clazz){
		log.debug("findAll for type "+clazz.getName());
		Query<T> query = ds.find(clazz);
		return find(query);
	}	
	public <T extends BaseMongoObject> List<T> findAllAndRetrieveField(Class<T> clazz, String field){
		log.debug("findAll for type "+clazz.getName() + " retrieve field " + field);
		Query<T> query = ds.find(clazz).retrievedFields(true, field);
		return find(query);
	}
	public <T extends BaseMongoObject> List<T> findAllAndRetrieveFields(Class<T> clazz, ArrayList<String> fieldList){
		log.debug("findAll for type "+clazz.getName() + " retrieve fieldList size " + fieldList.size());
		Query<T> query = ds.find(clazz).retrievedFields(true, fieldList.toArray(new String[fieldList.size()]));
		return find(query);
	}
	
	public <T extends BaseMongoObject> T findOne(Class<T> clazz,String queryField,Pattern regex){
		List<T> queryResults = find(clazz,queryField,regex);
		if((queryResults != null) && (queryResults.size()>0)){
			return queryResults.get(0);
		}
		else{
			return null;
		}
	}
	public <T extends BaseMongoObject> void delete(Class<T> clazz, String id) {
		BaseMongoObject obj;
		try {
			obj = clazz.newInstance();
			obj.setObjectId(new ObjectId(id));
			ds.delete(obj);
		} catch (InstantiationException e) {
			throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, "Error instantiating object for class " + clazz.getName());
		} catch (IllegalAccessException e) {
			throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, "Error instantiating object for class " + clazz.getName());
		}
	}
	public <T extends BaseMongoObject> void findAndDelete(Class<T> clazz, String queryField, String queryValue) {
		log.debug("findAndDelete for type "+clazz.getName()+" queryField " + queryField + " searchTerm " + queryValue);
		Query<T> query = ds.find(clazz,queryField,queryValue);
		WriteResult result = ds.delete(query);
		if (!result.wasAcknowledged()) {
			throw new MongoRuntimeException(ERR_DB_WRITE_FAILURE, "Error, write was not acknowleged for delete query: " + query.toString());
		}
	}
	public <T extends BaseMongoObject> int count(Query<T> query){
		return (int)ds.getCount(query);
	}
	private <T extends BaseMongoObject> void setId(T obj){
		obj.setId(obj.getObjectId().toString());
	}
	
	private <T extends BaseMongoObject> Key<T> makeKey(Class<T> clazz, String collectionName, String id) {
		Key<T> key = new Key<T>(clazz,collectionName,new ObjectId(id));
		return key;
	}
	
	/**
	 * Generate an Object ID (String only, to be used when the full BSON ID is not needed) 
	 * @return
	 */
	public static String generateObjectIdString(){
		return (new ObjectId()).toString();
	}
}
