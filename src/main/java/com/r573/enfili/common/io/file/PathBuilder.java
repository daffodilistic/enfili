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
package com.r573.enfili.common.io.file;

import java.io.File;
import java.util.LinkedList;

import org.apache.commons.io.FilenameUtils;

public class PathBuilder {
	private LinkedList<String> elements;
	private String separator;
	private String prefix;

	public PathBuilder(String initialPath) {
		init(initialPath);
	}
	
	public PathBuilder(File file) {
		init(file.getPath());
	}

	private void init(String initialPath){
		this.separator = File.separator;
		this.prefix = FilenameUtils.getPrefix(initialPath);
		
		this.elements = new LinkedList<String>();
		
		String pathWithoutPrefix = FilenameUtils.getPath(initialPath);
		String[] initialPathParts = pathWithoutPrefix.split("\\"+separator);
		for (int i = 0; i < initialPathParts.length; i++) {
			String thisPart = initialPathParts[i];
			if((thisPart != null) && (!thisPart.isEmpty())){
				elements.add(thisPart);	
			}
		}
		elements.add(FilenameUtils.getName(initialPath));
	}

	private String cleanElement(String element){
		if(element.startsWith(separator)){
			element = element.substring(1);
		}
		if(element.endsWith(separator)){
			element = element.substring(0,element.length()-1);
		}
		return element;
	}
	public PathBuilder append(String element){
		if(element == null) {
			return this;
		}
		element = cleanElement(element);
		elements.addLast(element);
		return this;
	}
	
	public PathBuilder prepend(String element){
		if(element == null) {
			return this;
		}
		element = cleanElement(element);
		elements.addFirst(element);
		return this;
	}
	
	public PathBuilder removeFront(int numElements){
		for(int i=0; i<numElements; i++){
			elements.removeFirst();
		}
		return this;
	}

	public PathBuilder removeBack(int numElements){
		for(int i=0; i<numElements; i++){
			elements.removeLast();
		}
		return this;
	}
	
	public PathBuilder removeFront(){
		return removeFront(1);
	}

	public PathBuilder removeBack(){
		return removeBack(1);
	}
	
	public PathBuilder removeAllFromFrontExcept(int numElementsToRetain){
		int numElements = elements.size();
		for(int i=numElements; i>numElementsToRetain; i--){
			elements.removeFirst();
		}
		return this;		
	}
	public PathBuilder removeAllFromBackExcept(int numElementsToRetain){
		int numElements = elements.size();
		for(int i=numElementsToRetain; i<numElements; i++){
			elements.removeLast();
		}
		return this;		
	}
	
	public String toPath(){
		String path = prefix;
		for (String element : elements) {
			path = FilenameUtils.concat(path, element);
		}
		return path;
	}
	
	public File toFile(){
		return new File(toPath());
	}
}
