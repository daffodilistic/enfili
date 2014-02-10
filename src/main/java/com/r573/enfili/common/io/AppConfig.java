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
package com.r573.enfili.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads a config file from the classpath (e.g. file in src/main/resources compiled into the JAR under the Maven directly structure)
 * To load from the root of the classpath, remember to prepend paths with "/"
 * Used as Singleton
 * 
 * @author ryanho
 *
 */
public class AppConfig {
	private static final String CONFIG_FILE_PATH = "/config.properties";

	private static AppConfig instance;
	private Properties config;

	private AppConfig(String configFilePath) {
		try {
			config = new Properties();
			InputStream is = this.getClass().getResourceAsStream(configFilePath);
			config.load(is);
		} catch (IOException e) {
			throw new RuntimeException("Unable to load config file " + configFilePath);
		}
	}

	public String getConfig(String key) {
		return config.getProperty(key);
	}
	public int getConfigAsInt(String key) {
		return Integer.parseInt(config.getProperty(key));
	}
	public long getConfigAsLong(String key) {
		return Long.parseLong(config.getProperty(key));
	}
	public boolean getConfigAsBoolean(String key) {
		return Boolean.parseBoolean(config.getProperty(key));
	}
	public static AppConfig getInstance() {
		if(instance == null) {
			throw new IllegalStateException("AppConfig needs to be initialized before use by calling one of the init methods");
		}
		return instance;
	}
	
	/**
	 * Initializes AppConfig using the default config path "/config.properties"
	 */
	public static void init(){
		init(CONFIG_FILE_PATH);
	}
	public static void init(String configFilePath){
		instance = new AppConfig(configFilePath);
	}
}
