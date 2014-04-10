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

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Very simple in-memory cache meant for small data sets
 * 
 * @author ryanho
 *
 */
public class SimpleInMemoryCache<T> implements Runnable {
	private static Logger log = LoggerFactory.getLogger(SimpleInMemoryCache.class);
	private static final long CLEANUP_INTERVAL = 300000L; // run the cleanup task once every 5 minutes
	private static final long DEFAULT_LIFESPAN = 15L * 60000L; // Default lifespan of 15 minutes
	
	private String name;
	private Thread t;
	private Hashtable<String,CacheItem<T>> cache;
	private long defaultLifespan;
	private boolean stop;
	private boolean started;
	private long numGetRequests;
	private long numCacheHits;
	private long numCacheMisses;
	
	public SimpleInMemoryCache(String name){
		this.name = name;
		cache = new Hashtable<String, CacheItem<T>>();
		this.defaultLifespan = DEFAULT_LIFESPAN;
		stop = false;
		started = false;
		numGetRequests = 0;
		numCacheHits = 0;
		numCacheMisses = 0;
		t = new Thread(this);
	}
	
	public void start(){
		log.info("SimpleInMemoryCache "+name+" started");
		t.start();
	}
	
	public void run(){
		started = true;
		while(!stop){
			try {
				Thread.sleep(CLEANUP_INTERVAL);
				cleanup();
			} catch (InterruptedException e) {
				stop = true;
			}
		}
	}
	
	private synchronized void cleanup(){
		log.debug("Cache cleanup");
		List<String> removeList = new ArrayList<String>();
		Set<String> keys = cache.keySet();
		for(String key : keys){
			CacheItem<T> item = cache.get(key);
			if(item.hasExpired()){
				removeList.add(key);
			}
		}
		for(String removeKey : removeList){
			cache.remove(removeKey);
		}
		log.debug("Removed " + removeList.size() + " expired entries from cache");
	}
	
	public void stop(){
		log.info("SimpleInMemoryCache "+name+" stopping");
		stop = true;
		t.interrupt();
		boolean stopped = false;
		while(!stopped){
			try {
				Thread.sleep(500);
				if(!t.isAlive()){
					log.info("SimpleInMemoryCache "+name+" stopped");
					stopped = true;
					t = null; // prevent circular references
				}
				else{
					log.info("Waiting for SimpleInMemoryCache "+name+" to stop");
				}
			} catch (InterruptedException e) {
				stopped = true;
			}
		}
	}
	
	public synchronized void put(String key, T obj){
		put(key,obj,defaultLifespan);
	}
	
	public synchronized void put(String key, T obj, long lifespan){
		checkStarted();
		CacheItem<T> item = new CacheItem<T>(obj, lifespan);
		cache.put(key, item);		
	}
	public synchronized void put(String key, T obj, Date expiry){
		checkStarted();
		CacheItem<T> item = new CacheItem<T>(obj, expiry);
		cache.put(key, item);		
	}
	
	public synchronized T get(String key){
		numGetRequests++;
		checkStarted();
		CacheItem<T> item = cache.get(key);
		if(item == null){
			numCacheMisses++;
			return null;
		}
		else{
			if(item.hasExpired()){
				cache.remove(key);
				numCacheMisses++;
				return null;
			}
			else{numCacheHits++;
				return item.getCachedObject();
			}
		}
	}
	
	private void checkStarted(){
		if(!started){
			log.warn("Cache "+name+" has not been started. It will still work but stale entries will not be cleaned up, leading to possible memory leaks.");
		}
	}

	public long getDefaultLifespan() {
		return defaultLifespan;
	}

	public void setDefaultLifespan(long defaultLifespan) {
		this.defaultLifespan = defaultLifespan;
	}

	public long getNumGetRequests() {
		return numGetRequests;
	}

	public long getNumCacheHits() {
		return numCacheHits;
	}

	public long getNumCacheMisses() {
		return numCacheMisses;
	}
}
