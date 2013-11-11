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
package com.r573.enfili.common.resource.cloud.aws.dynamodb;

import java.util.ArrayList;
import java.util.Iterator;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodb.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodb.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodb.datamodeling.PaginatedScanList;
import com.r573.enfili.common.resource.cloud.aws.AwsConstants;
import com.r573.enfili.common.singleton.SingletonManager;

public class DynamoDbManager {
	private static SingletonManager<DynamoDbManager> singletonManager = new SingletonManager<DynamoDbManager>();;

	private AmazonDynamoDBClient client;
	private DynamoDBMapper mapper;

	public static void initFromEnv(String region,String tag) {
		initFromEnv(AwsConstants.DEFAULT_ENV_KEY_ACCESS_KEY, AwsConstants.DEFAULT_ENV_KEY_SECRET_KEY, region,tag);
	}

	public static void initFromEnv(String keyAccessKey, String keySecretKey, String region, String tag) {
		String awsAccessKey = System.getenv(keyAccessKey);
		String awsSecretKey = System.getenv(keySecretKey);
		if ((awsAccessKey == null) || (awsAccessKey.isEmpty())) {
			throw new IllegalStateException("AWS_ACCESS_KEY environment variable not defined");
		}
		if ((awsSecretKey == null) || (awsSecretKey.isEmpty())) {
			throw new IllegalStateException("AWS_SECRET_KEY environment variable not defined");
		}

		initWithCredentials(awsAccessKey, awsSecretKey, region, tag);
	}

	public static void initWithCredentials(String awsAccessKey, String awsSecretKey, String region, String tag) {
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		initWithCredentials(awsCredentials, region, tag);
	}

	public static void initWithCredentials(AWSCredentials awsCredentials, String region, String tag) {
		DynamoDbManager manager = new DynamoDbManager(awsCredentials, region);

		singletonManager.addInstance(tag, manager);
	}

	public static DynamoDbManager getDefaultInstance() {
		return singletonManager.getDefaultInstance();
	}

	public static DynamoDbManager getInstance(String tag) {
		return singletonManager.getInstance(tag);
	}

	public static void removeInstance(String tag) {
		singletonManager.removeInstance(tag);
	}

	private DynamoDbManager(AWSCredentials awsCredentials, String region) {
		client = new AmazonDynamoDBClient(awsCredentials);
		client.setEndpoint("https://dynamodb."+region+".amazonaws.com");
		mapper = new DynamoDBMapper(client);
	}
	
	public <T> T getItem(Class<T> clazz, Object hashKey){
		return mapper.load(clazz, hashKey);
	}
	
	public <T> ArrayList<T> getItemsByScan(Class<T> clazz) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withLimit(40);
		PaginatedScanList<T> scanList = mapper.scan(clazz, scanExpression);
		
		ArrayList<T> results = new ArrayList<T>();
		Iterator<T> it = scanList.iterator();
		while(it.hasNext()) {
			T element = it.next();
			results.add(element);
		}
		return results;
	}
	public void saveItem(Object item){
		mapper.save(item);
	}
}
