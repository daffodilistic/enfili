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
package com.r573.enfili.common.resource.cloud.aws.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.security.AWSCredentials;
import org.jets3t.service.security.ProviderCredentials;

import com.r573.enfili.common.resource.cloud.aws.AwsConstants;
import com.r573.enfili.common.singleton.SingletonManager;

/**
 *
 * @author ryanho
 */
public class S3Manager {
	
	private static SingletonManager<S3Manager> singletonManager = new SingletonManager<S3Manager>();;
	
    private RestS3Service s3Client;
    private String downloadDir;
    private String pathSeparator;
    
    public static void initFromEnv(String downloadDir, String tag){
    	initFromEnv(AwsConstants.DEFAULT_ENV_KEY_ACCESS_KEY,AwsConstants.DEFAULT_ENV_KEY_SECRET_KEY,downloadDir, tag);
    }
    public static void initFromEnv(String keyAccessKey, String keySecretKey, String downloadDir, String tag){
    	String awsAccessKey = System.getenv(keyAccessKey);
    	String awsSecretKey = System.getenv(keySecretKey);
        if((awsAccessKey == null) || (awsAccessKey.isEmpty())){
            throw new IllegalStateException("AWS_ACCESS_KEY environment variable not defined");
        }
        if((awsSecretKey == null) || (awsSecretKey.isEmpty())){
            throw new IllegalStateException("AWS_SECRET_KEY environment variable not defined");
        }

    	initWithCredentials(awsAccessKey,awsSecretKey,downloadDir, tag);
    }
    public static void initWithCredentials(String awsAccessKey, String awsSecretKey, String downloadDir, String tag){
    	ProviderCredentials awsCredentials = new AWSCredentials(awsAccessKey, awsSecretKey);
    	initWithCredentials(awsCredentials,downloadDir, tag);
    }
    public static void initWithCredentials(ProviderCredentials awsCredentials, String downloadDir, String tag){
    	S3Manager s3Manager = new S3Manager(awsCredentials, downloadDir);

    	singletonManager.addInstance(tag, s3Manager);
    }
    
    public static S3Manager getDefaultInstance(){
    	return singletonManager.getDefaultInstance();
    }
    public static S3Manager getInstance(String tag){
    	return singletonManager.getInstance(tag);
    }
    public static void removeInstance(String tag){
    	singletonManager.removeInstance(tag);
    }
    
    private S3Manager(ProviderCredentials awsCredentials, String downloadDir) {
        try {
            this.downloadDir = downloadDir;
            s3Client = new RestS3Service(awsCredentials);
            pathSeparator = System.getProperty("file.separator");
        }
        catch (ServiceException e) {
            // fatal if S3 Client can't be created
            throw new RuntimeException("Unable to initialize the S3Manager",e);
        }
    }
        
    public String[] getFileList(String bucketName){
        try {
            S3Object[] files = s3Client.listObjects(bucketName);
            return toFileNameArray(files);
        }
        catch (S3ServiceException e) {
            // TODO: handle this better
            throw new RuntimeException(e);
        }
    }
    public String[] getFileList(String bucketName, String prefix){
        try {
            S3Object[] files = s3Client.listObjects(bucketName, prefix, null);
            return toFileNameArray(files);
        }
        catch (S3ServiceException e) {
            // TODO: handle this better
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Filters out directories too
     * 
     * @param s3Objects
     * @return
     */
    private String[] toFileNameArray(S3Object[] s3Objects){
    	ArrayList<String> validFileNames = new ArrayList<String>();
        for(int i=0; i< s3Objects.length; i++){
        	String objectName = s3Objects[i].getName();
        	if(!objectName.endsWith(pathSeparator)) {
        		validFileNames.add(objectName);
        	}
        }
        String[] fileNames = validFileNames.toArray(new String[validFileNames.size()]);
        return fileNames;        
    }
     
    public void deleteMultipleFiles(String bucketName,List<String> filesToDelete){
        try {
            String[] sa = new String[filesToDelete.size()];
            s3Client.deleteMultipleObjects(bucketName, filesToDelete.toArray(sa));
        }
        catch (ServiceException ex) {
            // TODO: handle this better
            throw new RuntimeException(ex);
        }
    }
    
    public void renameFile(String srcBucket, String srcKey, String destKey){
        try {
            S3Object destObj = new S3Object(destKey);
            s3Client.renameObject(srcBucket, srcKey, destObj);
        }
        catch (ServiceException ex) {
            // TODO: handle this better
            throw new RuntimeException(ex);
        }
    }
    
    public InputStream streamFile(String bucketName, String objectKey) {
    	try {
            S3Object downloadedObject = s3Client.getObject(bucketName, objectKey);
            InputStream downloadInputStream = downloadedObject.getDataInputStream();
            return downloadInputStream;    		
    	}
        catch (ServiceException e) {
            e.printStackTrace();
            // TODO: handle this better
            throw new RuntimeException(e);
        }
    }
    
    public File downloadFile(String bucketName, String objectKey){
        try {
            S3Object downloadedObject = s3Client.getObject(bucketName, objectKey);
            InputStream downloadInputStream = downloadedObject.getDataInputStream();
            File downloadedFile = new File(downloadDir + pathSeparator + objectKey);
            
            // ensure directories are created before saving the file
            String filePath = downloadedFile.getPath();
            String parentDirPath = filePath.substring(0, filePath.lastIndexOf(pathSeparator));
            File parentDir = new File(parentDirPath);
            if(!parentDir.exists()) {
            	parentDir.mkdirs();
            }
            
            FileOutputStream outputFileStream = null;
            try {
                outputFileStream = new FileOutputStream(downloadedFile);
                int bytesRead = 0;
                byte[] b = new byte[1024];
                while((bytesRead = downloadInputStream.read(b)) != -1) {
                    outputFileStream.write(b, 0, bytesRead);                    
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                // TODO: handle this better
                throw new RuntimeException(e);
            }
            finally {
                try {
                    outputFileStream.close();
                    downloadedObject.closeDataInputStream();
                }
                catch (IOException ex) {
                    // do nothing here
                }
            }
            return downloadedFile;
        }
        catch (ServiceException e) {
            // fatal if S3 Client can't be created
            throw new RuntimeException(e);
        }        
    }
    
    public boolean isObjectInBucket(String bucketName, String objectKey){
        try {
            return s3Client.isObjectInBucket(bucketName, objectKey);
        }
        catch (ServiceException e) {
            // fatal if S3 Client can't be created
            throw new RuntimeException(e);
        }    	
    }
    
    public void uploadFile(String bucketName, String objectName, File file) {
    	try{
    		byte[] fileData = FileUtils.readFileToByteArray(file);
	    	StorageObject storageObject = new StorageObject(objectName, fileData);
	    	s3Client.putObject(bucketName, storageObject);
    	}
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }        
        catch (IOException e) {
            throw new RuntimeException(e);
        }        
        catch (ServiceException e) {
            throw new RuntimeException(e);
        }        
    }
    
    public long getFileSize(String bucketName, String objectName) {
        try {
            return s3Client.getObjectDetails(bucketName, objectName).getContentLength();
        }
        catch (ServiceException e) {
            // fatal if S3 Client can't be created
            throw new RuntimeException(e);
        }
    }
}
