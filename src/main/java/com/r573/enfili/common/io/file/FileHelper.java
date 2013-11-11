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
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileHelper {

	/**
	 * 0: dir not created because it already exists 1: dir created -1: dir needs
	 * to be created, but failed
	 * 
	 * @param dir
	 * @return
	 */
	public static int mkdirIfNotExists(File dir) {
		if (!dir.exists()) {
			if (dir.mkdirs()) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 0;
		}
	}

	public static void copyAllFilesInFolder(File srcFolder, File destFolder) {
		try {
			if (!srcFolder.exists()) {
				// TODO: ignore for now. Figure out the appropriate error
				// handling for this later
				return;
			}
			Collection<File> files = FileUtils.listFiles(srcFolder, null, true);
			for (File file : files) {
				File destFile = new PathBuilder(destFolder.getPath()).append(file.getName()).toFile();
				FileUtils.copyFile(file, destFile);					
			}
		} catch (IOException e) {
			throw new FileOpException(e);
		}
	}

	public static void deleteDirectory(File dir) {
		try {
			FileUtils.deleteDirectory(dir);
		}
		catch (IOException e) {
			throw new FileOpException(e);
		}
	}
	
	public static Collection<File> getAllFilesRecursively(File dir) {
		Collection<File> files = FileUtils.listFiles(dir, null, true);
		return files;
	}
	
	public static String readTextFromStream(InputStream stream) {
		try {
			List<String> lines = IOUtils.readLines(stream);
			StringBuilder sb = new StringBuilder();
			for(String line : lines){
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		}
		catch(IOException e) {
			throw new FileOpException(e);
		}
	}
	
	public static void writeTextFile(File file, String content) {
		try {
			FileUtils.write(file, content, "UTF-8");
		}
		catch(IOException e) {
			throw new FileOpException(e);
		}
	}
	
	public static void writeTextFile(String filePath, String content) {
		writeTextFile(new File(filePath), content);
	}
	
	public static String combinePath(String parent, String child){
		return new PathBuilder(parent).append(child).toPath();
	}
}
