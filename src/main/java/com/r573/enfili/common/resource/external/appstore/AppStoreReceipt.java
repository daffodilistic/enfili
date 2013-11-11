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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AppStoreReceipt {
	private int quantity;
	@JsonProperty("product_id")
	private String productId;
	@JsonProperty("transaction_id")
	private String transactionId;
	@JsonProperty("purchase_date")
	private String purchaseDate;
	@JsonProperty("original_transaction_id")
	private String originalTransactionId;
	@JsonProperty("original_purchase_date")
	private String originalPurchaseDate;
	@JsonProperty("app_item_id")
	private String appItemId;
	@JsonProperty("version_external_identifier")
	private String versionExternalIdentifier;
	private String bid;
	private String bvrs;
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getOriginalTransactionId() {
		return originalTransactionId;
	}
	public void setOriginalTransactionId(String originalTransactionId) {
		this.originalTransactionId = originalTransactionId;
	}
	public String getOriginalPurchaseDate() {
		return originalPurchaseDate;
	}
	public void setOriginalPurchaseDate(String originalPurchaseDate) {
		this.originalPurchaseDate = originalPurchaseDate;
	}
	public String getAppItemId() {
		return appItemId;
	}
	public void setAppItemId(String appItemId) {
		this.appItemId = appItemId;
	}
	public String getVersionExternalIdentifier() {
		return versionExternalIdentifier;
	}
	public void setVersionExternalIdentifier(String versionExternalIdentifier) {
		this.versionExternalIdentifier = versionExternalIdentifier;
	}
	public String getBid() {
		return bid;
	}
	public void setBid(String bid) {
		this.bid = bid;
	}
	public String getBvrs() {
		return bvrs;
	}
	public void setBvrs(String bvrs) {
		this.bvrs = bvrs;
	}
}
