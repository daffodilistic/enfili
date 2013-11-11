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
package com.r573.enfili.common.resource.external.appstore;

import org.apache.commons.codec.binary.Base64;

import com.r573.enfili.common.resource.rest.ResponseWrapper;
import com.r573.enfili.common.resource.rest.RestClient;
import com.r573.enfili.common.singleton.SingletonManager;

public class AppStoreManager {
	private static final String ITUNES_API_BASEURL = "https://buy.itunes.apple.com";
	private static final String ITUNES_API_SANDBOX_BASEURL = "https://sandbox.itunes.apple.com";
	private static SingletonManager<AppStoreManager> singletonManager = new SingletonManager<AppStoreManager>();
	
	public static void init(boolean useSandbox){
		singletonManager.addInstance(null, new AppStoreManager(useSandbox));
	}
	
	public static AppStoreManager getInstance(){
		AppStoreManager instance = singletonManager.getDefaultInstance();
		return instance;
	}
	
	private boolean useSandbox;
	private AppStoreManager(boolean useSandbox){
		this.useSandbox = useSandbox;
	}
	
	public VerifyReceiptResponse verifyReceipt(String receiptId) throws AppStoreException {
		try {
			String receiptEncoded = Base64.encodeBase64String(receiptId.getBytes("UTF-8"));
			String urlToUse = ITUNES_API_BASEURL;
			if(useSandbox) {
				urlToUse = ITUNES_API_SANDBOX_BASEURL;
			}
			RestClient restClient = new RestClient(urlToUse);
			VerifyReceiptRequest request = new VerifyReceiptRequest();
			request.setReceiptData(receiptEncoded);
			ResponseWrapper<VerifyReceiptResponse> result = restClient.post("/verifyReceipt", request, VerifyReceiptResponse.class);
			return result.getResponse();			
		}
		catch(Exception e) {
			throw new AppStoreException("Unable to verify receipt with AppStore", e);
		}
	}

	public boolean isUseSandbox() {
		return useSandbox;
	}

	public void setUseSandbox(boolean useSandbox) {
		this.useSandbox = useSandbox;
	}
}
