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
package com.r573.enfili.common.text;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class StringHelper {
	public static String listToString(List<?> list){
		StringBuilder sb = new StringBuilder();
		int c = 1;
		for(Object item : list) {
			sb.append(item);
			if(c < list.size()) {
				sb.append(",");
			}
			c++;
		}
		return sb.toString();
	}
	public static String nullToEmptyString(String str) {
		if(str == null) {
			return "";
		}
		else {
			return str;
		}
	}
	
	public static String stackTraceToString(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		t.printStackTrace(pw);
		return sw.toString();
	}
	
	public static String paddedTwoDigitStringFromInt(int i){
		if(i < 10){
			return "0"+i;
		}
		else{
			return String.valueOf(i);
		}
	}
}
