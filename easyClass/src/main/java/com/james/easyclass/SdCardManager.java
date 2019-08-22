/*
 * Copyright 2012 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.easyclass;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import android.util.Log;
import android.content.res.AssetManager;
import android.os.Environment;

/**
 * SD card 管理，功能還未完全，目前只有刪除檔案、資料夾，更改命名，取得所有資料夾下所有檔案
 * 
 * @author JamesX
 * @since 2012/05/17
 */
public class SdCardManager {
	private static final String TAG = SdCardManager.class.getSimpleName();
	public static final String SD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	
	/**
	 * 
	 * @param dir
	 * @param filename
	 * @return
	 */
	public static boolean exsitsFile(String dir, String filename){
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		if (!dir.startsWith(SD_PATH)) {
			if (!dir.startsWith("/")){
				dir = SD_PATH+"/"+dir;
			}
			else{
				dir = SD_PATH+dir;
			}
		}
		
		File file = new File(dir, filename);
		
		return file.exists();
	}
	
	public static boolean exsitsDir(String dir){
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		if (!dir.startsWith(SD_PATH)) {
			if (!dir.startsWith("/")){
				dir = SD_PATH+"/"+dir;
			}
			else{
				dir = SD_PATH+dir;
			}
		}
		
		File file = new File(dir);
		
		return file.exists();
	}

	/**
	 * 更改 sdcard 中檔案的名稱
	 * 
	 * @param dir
	 *            (String) 資料夾路徑名稱
	 * @param fromFilename
	 *            (String) 原始檔案名稱
	 * @param toFilename
	 *            (String) 目標檔案名稱
	 */
	public static boolean renameFile(String dir, String fromFilename,
			String toFilename) {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		if (!dir.startsWith(SD_PATH)) {
			if (!dir.startsWith("/")){
				dir = SD_PATH+"/"+dir;
			}
			else{
				dir = SD_PATH+dir;
			}
		}
		
		File path = new File(dir);

		File from = new File(path, fromFilename);
		File to = new File(path, toFilename);
		Log.v(TAG, "renameFile from: " + from.toString());
		Log.v(TAG, "renameFile to: " + to.toString());

		return from.renameTo(to);
	}

	/**
	 * 回傳資料夾裡面所有檔案的檔名
	 * 
	 * @return String[]
	 */
	public static String[] getFileNames(String dir) {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		if (!dir.startsWith(SD_PATH)) {
			if (!dir.startsWith("/")){
				dir = SD_PATH+"/"+dir;
			}
			else{
				dir = SD_PATH+dir;
			}
		}
		
		File file = new File(dir);
		Log.d(TAG, "saveBitmapToSD " + dir);
		if (!file.exists()) {
			return null;
		}
		return file.list();
	}

	/**
	 * 刪除資料夾中的檔案(只刪除一個)
	 * 
	 * @param dir
	 *            (String) 資料夾路徑名稱
	 * @param filename
	 *            (String) 檔案名稱
	 * @return (boolean)
	 */
	public static boolean deleteFile(String dir, String filename) {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		if (!dir.startsWith(SD_PATH)) {
			if (!dir.startsWith("/")){
				dir = SD_PATH+"/"+dir;
			}
			else{
				dir = SD_PATH+dir;
			}
		}
		
		File path = new File(dir);
		File file = new File(path, filename);
		Log.v(TAG, "deleteFile: " + filename.toString());

		return file.delete();
	}

	/**
	 * delete directory within its files and sub-directories
	 * 
	 * @param stringPath
	 *            (String) 資料夾路徑
	 * @return
	 */
	public static boolean deleteDirectory(String dir) {
		if (!dir.endsWith("/")) {
			dir = dir + "/";
		}
		if (!dir.startsWith(SD_PATH)) {
			if (!dir.startsWith("/")){
				dir = SD_PATH+"/"+dir;
			}
			else{
				dir = SD_PATH+dir;
			}
		}
		
		File path = new File(dir);
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * delete directory within its files and sub-directories
	 * 
	 * @param path
	 *            (File) 資料夾
	 * @return
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	/**
	 * 
	 * @param sourceDir
	 * @param zipFileName
	 * @param targetDir
	 */
	public static void unZip(String sourceDir, String zipFileName, String targetDir, boolean isOverwrite) {
		if (!targetDir.endsWith("/")) {
			targetDir = targetDir + "/";
		}
		if (!targetDir.startsWith(SD_PATH)) {
			if (!targetDir.startsWith("/")){
				targetDir = SD_PATH+"/"+targetDir;
			}
			else{
				targetDir = SD_PATH+targetDir;
			}
		}
		
		FileOutputStream fileOut = null;
		ZipInputStream zipIn = null;
		ZipEntry zipEntry = null;
		File file = new File(targetDir);
		if(!isOverwrite && file.exists()){
			return;
		}
		
		int readedBytes = 0;
		byte buf[] = new byte[4096];
		try {
			zipIn = new ZipInputStream(new BufferedInputStream(
					new FileInputStream(new File(sourceDir, zipFileName))));
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				file = new File(targetDir + zipEntry.getName());
				if (zipEntry.isDirectory()) {
					file.mkdirs();
				} else {
					// 如果指定文件的目录不存在,则创建之.
					File parent = file.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					Log.v(TAG, "unzip file "+file.getName());
					fileOut = new FileOutputStream(file);
					while ((readedBytes = zipIn.read(buf)) > 0) {
						fileOut.write(buf, 0, readedBytes);
					}
					fileOut.close();
				}
				zipIn.closeEntry();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param assetManager
	 * @param targetDir (String)
	 */
	public static void copyAssets2Sd(AssetManager assetManager, String assestDir, String targetDir) {
		//
		if (!targetDir.endsWith("/")) {
			targetDir = targetDir + "/";
		}
		//
		if (!targetDir.startsWith(SD_PATH)) {
			if (!targetDir.startsWith("/")){
				targetDir = SD_PATH+"/"+targetDir;
			}
			else{
				targetDir = SD_PATH+targetDir;
			}
		}
		//
		File targetFile = new File(targetDir);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
		
		//
	    String[] files = null; 
	    try { 
	        files = assetManager.list(assestDir);
	        for(int i=0; i<files.length; i++){
	        	Log.v(TAG, "files: "+files[i]);
	        }
	    } catch (IOException e) { 
	        Log.e(TAG, e.getMessage()); 
	    }

	    for(String filename : files) { 
	        InputStream in = null; 
	        OutputStream out = null; 
	        try { 
	          in = assetManager.open(assestDir+"/"+filename); 
	          out = new FileOutputStream(targetDir+filename); 
	          copyFile(in, out); 
	          in.close(); 
	          in = null; 
	          out.flush(); 
	          out.close(); 
	          out = null; 
	        } catch(Exception e) { 
	            Log.e(TAG, e.getMessage()); 
	        }        
	    } 
	}
	
	public static void copyFilesFromDirToDir(String originalDir, String targetDir){
		// origin
		if (!originalDir.endsWith("/")) {
			originalDir = originalDir + "/";
		}
		//
		if (!originalDir.startsWith(SD_PATH)) {
			if (!originalDir.startsWith("/")){
				originalDir = SD_PATH+"/"+originalDir;
			}
			else{
				originalDir = SD_PATH+originalDir;
			}
		}
		//
		File originalFile = new File(originalDir);
		if(!originalFile.exists()){
			Log.w(TAG, originalFile.getAbsolutePath()+" does not exist.");
			return;
		}
		
		// target
		if (!targetDir.endsWith("/")) {
			targetDir = targetDir + "/";
		}
		//
		if (!targetDir.startsWith(SD_PATH)) {
			if (!targetDir.startsWith("/")){
				targetDir = SD_PATH+"/"+targetDir;
			}
			else{
				targetDir = SD_PATH+targetDir;
			}
		}
		//
		File targetFile = new File(targetDir);
		if(!targetFile.exists()){
			targetFile.mkdirs();
		}
		
		//
	    String[] files = null; 
	    files = originalFile.list();
        for(int i=0; i<files.length; i++){
        	Log.v(TAG, "files: "+files[i]);
        }

	    for(String filename : files) { 
	        InputStream in = null; 
	        OutputStream out = null; 
	        try { 
	          in = new FileInputStream(originalDir+filename); 
	          out = new FileOutputStream(targetDir+filename); 
	          copyFile(in, out); 
	          in.close(); 
	          in = null; 
	          out.flush(); 
	          out.close(); 
	          out = null; 
	        } catch(Exception e) { 
	            Log.e(TAG, e.getMessage()); 
	        }        
	    } 
	}
	
	public static void copyFile(InputStream in, OutputStream out) throws IOException { 
	    byte[] buffer = new byte[1024]; 
	    int read; 
	    while((read = in.read(buffer)) != -1){ 
	      out.write(buffer, 0, read); 
	    } 
	}
}
