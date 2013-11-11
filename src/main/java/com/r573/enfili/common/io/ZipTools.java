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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.r573.enfili.common.io.file.FileHelper;

public class ZipTools {
	private static final int BUFFER_SIZE = 1024;
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ZipTools.class);

	public static void makeZip(File sourceDir, File targetFile, String pathPrefix) {
		BufferedInputStream bis = null;
		ZipOutputStream zos = null;
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
			zos = new ZipOutputStream(bos);
			zos.setLevel(9);

			log.debug("sourceDir " + sourceDir);
			Collection<File> fileList = FileHelper.getAllFilesRecursively(sourceDir);
			
			// Work out the length of the source dir path
			// This is used for working out the path of each entry within the zip archive
			// It is the part after the "/" in the sourced dir path
			// i.e. the source dir path is omitted
			String sourceDirPath = sourceDir.getPath();
			int sourceDirPathLength = sourceDirPath.length();
			int zipPathStartPos = sourceDirPathLength + 1;
			
			for (File file : fileList) {
				// create a String representing the path of the file in the zip archive
				String sourceFilePath = file.getPath();
				String zipFilePath = sourceFilePath.substring(zipPathStartPos);
				
				if(pathPrefix != null){
					zipFilePath = FileHelper.combinePath(pathPrefix,zipFilePath);
				}
				
				log.debug("Adding zipEntry " + zipFilePath);
				
				ZipEntry zipEntry = new ZipEntry(zipFilePath);
				zos.putNextEntry(zipEntry);

				FileInputStream fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis, BUFFER_SIZE);
				
				byte[] buf = new byte[BUFFER_SIZE];
				int bytesRead = 0;
				while ((bytesRead = bis.read(buf)) != -1) {
					if (bytesRead < BUFFER_SIZE) {
						byte[] buf2 = new byte[bytesRead];
						System.arraycopy(buf, 0, buf2, 0, bytesRead);
						buf = buf2;
					}
					zos.write(buf);
					buf = new byte[BUFFER_SIZE];
				}
				zos.closeEntry();
				bis.close();
			}
			zos.finish();			
			
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (zos != null) {
					zos.close();
				}
			} catch (IOException e) {
				// nothing needs to be done here
			}
		}
	}
}
