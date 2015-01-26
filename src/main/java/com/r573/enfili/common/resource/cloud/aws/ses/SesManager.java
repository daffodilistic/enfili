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
package com.r573.enfili.common.resource.cloud.aws.ses;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.r573.enfili.common.resource.cloud.aws.AwsConstants;
import com.r573.enfili.common.singleton.SingletonManager;

public class SesManager {
	private static SingletonManager<SesManager> singletonManager = new SingletonManager<SesManager>();
	
	private AmazonSimpleEmailServiceClient client;
	private String defaultSender;
		
	public static void initFromEnv(String defaultSender, String tag) {
		initFromEnv(AwsConstants.DEFAULT_ENV_KEY_ACCESS_KEY, AwsConstants.DEFAULT_ENV_KEY_SECRET_KEY, defaultSender, tag);
	}

	public static void initFromEnv(String keyAccessKey, String keySecretKey, String sender, String tag) {
		String awsAccessKey = System.getenv(keyAccessKey);
		String awsSecretKey = System.getenv(keySecretKey);
		if ((awsAccessKey == null) || (awsAccessKey.isEmpty())) {
			throw new IllegalStateException("AWS_ACCESS_KEY environment variable not defined");
		}
		if ((awsSecretKey == null) || (awsSecretKey.isEmpty())) {
			throw new IllegalStateException("AWS_SECRET_KEY environment variable not defined");
		}

		initWithCredentials(awsAccessKey, awsSecretKey, sender, tag);
	}

	public static void initWithCredentials(String awsAccessKey, String awsSecretKey, String defaultSender, String tag) {
		AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
		initWithCredentials(awsCredentials, defaultSender, tag);
	}

	public static void initWithCredentials(AWSCredentials awsCredentials, String defaultSender, String tag) {
		SesManager manager = new SesManager(awsCredentials, defaultSender);

		singletonManager.addInstance(tag, manager);
	}

	public static SesManager getDefaultInstance() {
		return singletonManager.getDefaultInstance();
	}

	public static SesManager getInstance(String tag) {
		return singletonManager.getInstance(tag);
	}

	public static void removeInstance(String tag) {
		singletonManager.removeInstance(tag);
	}

	private SesManager(AWSCredentials awsCredentials, String defaultSender) {
		client = new AmazonSimpleEmailServiceClient(awsCredentials);

		this.defaultSender = defaultSender;
	}
	public void sendEmailToSingleRecipient(String recipient, String subject, String emailContentText, String emailContentHtml, String sender){
		SendEmailRequest request = new SendEmailRequest().withSource(sender);

		List<String> toAddresses = new ArrayList<String>();
		toAddresses.add(recipient);
		Destination dest = new Destination().withToAddresses(recipient);
		request.setDestination(dest);

		Content subjectContent = new Content().withData(subject);
		Message msg = new Message();
		msg.setSubject(subjectContent);

		Body body = new Body();
		if(emailContentText!=null){
			Content textContent = new Content().withData(emailContentText);
			body.withText(textContent);
		}
		if(emailContentHtml!=null){
			Content htmlContent = new Content().withData(emailContentHtml);
			body.withHtml(htmlContent);
		}
		
		msg.setBody(body);

		request.setMessage(msg);
		
		client.sendEmail(request);
	}
	public void sendEmailToSingleRecipient(String recipient, String subject, String emailContentText, String emailContentHtml){
		sendEmailToSingleRecipient(recipient,subject,emailContentText,emailContentHtml,defaultSender);
	}
}
