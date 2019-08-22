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

package com.james.easyinternet;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;


/**
 * This class is deprecated. Please Use <code>EasyHttpRequest</code> Instead.
 * 
 * @author JamesX
 * @since 2012/05/17
 * @deprecated
 * @see EasyHttpRequest
 */
public class RequestHTTP {
	private final static String TAG = "RequestHTTP";
	

	public static class ResponseAndHeader{
		private int header;
		private String response;
		
		public void setHeader(int header){
			this.header = header;
		}
		
		public int getHeader(){
			return header;
		}
		
		public void setResponse(String response){
			this.response = response;
		}
		
		public String getResponse(){
			return response;
		}
	}
	
	public interface OnExceptionListener{
		public void onError(Exception e);
	}

	/**
	 * 取得URL的內容
	 * 
	 * @param url (String) URL網址
	 * @return (String) 回傳網址的內容
	 * @deprecated
	 */
	public static String requestContent(String url) {
		return httpGet(url, null);
	}
	
	/**
	 * 取得URL的內容
	 * 
	 * @param url (String) URL網址
	 * @return (String) 回傳網址的內容
	 */
	public static String httpGet(String url) {
		return httpGet(url, null);
	}
	
	/**
	 * 取得URL的內容
	 * 
	 * @param URL (String) URL網址
	 * @param onExceptionListener (OnExceptionListener) 可取得exception資料
	 * @return (String) 回傳網址的內容
	 */
	public static String httpGet(String url, OnExceptionListener onExceptionListener) {
		ResponseAndHeader responseAndHeader = new ResponseAndHeader();
		
		httpGet(responseAndHeader, url, onExceptionListener);

		return responseAndHeader.getResponse();
	}
	
	/**
	 * TODO
	 * @param responseAndHeader
	 * @param url
	 * @param onExceptionListener
	 * @return
	 */
	public static void httpGet(ResponseAndHeader responseAndHeader, String url, OnExceptionListener onExceptionListener) {
		HttpGet httpGet = new HttpGet(url);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
		
		try {
			HttpResponse response = httpClient.execute(httpGet);
			responseAndHeader.header = response.getStatusLine().getStatusCode();
			responseAndHeader.response = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		} catch (IOException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}
	}

	/**
	 * 利用post取得資料，建議使用 postData(String url, BasicNameValuePair...
	 * basicNameValuePairs)
	 * 
	 * @param url
	 *            (String) url
	 * @param params
	 *            (ArrayList<NameValuePair>)
	 * @return (String) url 回傳結果
	 * @see postData(String url, BasicNameValuePair... basicNameValuePairs)
	 * @deprecated
	 */
	public static String postData(String url, ArrayList<NameValuePair> params) {
		return httpPost(url, params, null);
	}
	
	/**
	 * 利用post取得資料，建議使用 postData(String url, BasicNameValuePair...
	 * basicNameValuePairs)
	 * 
	 * @param url (String) url
	 * @param params (ArrayList<NameValuePair>)
	 * @return (String) url 回傳結果
	 * @see postData(String url, BasicNameValuePair... basicNameValuePairs)
	 */
	public static String httpPost(String url, ArrayList<NameValuePair> params) {
		return httpPost(url, params, null);
	}
	
	/**
	 * 利用post取得資料，
	 * 
	 * @param url (String) url
	 * @param params (ArrayList<NameValuePair>)
	 * @param onExceptionListener (OnExceptionListener) 可取得exception資料
	 * @return (String) url 回傳結果
	 */
	public static String httpPost(String url, ArrayList<NameValuePair> params, OnExceptionListener onExceptionListener) {
		ResponseAndHeader responseAndHeader = new ResponseAndHeader();
		httpPost(responseAndHeader, url, params, onExceptionListener);
		
		return responseAndHeader.getResponse();
	}
	
	/**
	 * 利用post取得資料，
	 * 
	 * @param url (String) url
	 * @param params (ArrayList<NameValuePair>)
	 * @param onExceptionListener (OnExceptionListener) 可取得exception資料
	 * @return (String) url 回傳結果
	 */
	public static void httpPost(ResponseAndHeader responseAndHeader, String url, ArrayList<NameValuePair> params, OnExceptionListener onExceptionListener) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}

		try {
			HttpResponse response = httpClient.execute(httpPost);
			responseAndHeader.header = response.getStatusLine().getStatusCode();
			responseAndHeader.response = EntityUtils.toString(response.getEntity());

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		} catch (IOException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}
	}
	
	/**
	 * 用法為 RequestHTTP.postData(url, new BasicNameValuePair("123", "222"), new
	 * BasicNameValuePair("123", "222"), ...); BasicNameValuePair
	 * 欄位可以無限延伸，前一個參數是 url 的對應欄位，後一個參數是欄位輸入值
	 * 
	 * @param url (String) url
	 * @param basicNameValuePairs (BasicNameValuePair...) ex: new BasicNameValuePair("123", "222")
	 * @return
	 * @deprecated
	 */
	public static String postData(String url, BasicNameValuePair... basicNameValuePairs) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.clear();

		for (int i = 0; i < basicNameValuePairs.length; i++) {
			params.add(basicNameValuePairs[i]);
		}
		return httpPost(url, params);
	}
	
	/**
	 * 用法為 RequestHTTP.postData(url, new BasicNameValuePair("123", "222"), new
	 * BasicNameValuePair("123", "222"), ...); BasicNameValuePair
	 * 欄位可以無限延伸，前一個參數是 url 的對應欄位，後一個參數是欄位輸入值
	 * 
	 * @param url (String) url
	 * @param basicNameValuePairs (BasicNameValuePair...) ex: new BasicNameValuePair("123", "222")
	 * @return
	 */
	public static String httpPost(String url, BasicNameValuePair... basicNameValuePairs) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.clear();

		for (int i = 0; i < basicNameValuePairs.length; i++) {
			params.add(basicNameValuePairs[i]);
		}
		return httpPost(url, params);
	}

	/**
	 * 用法為 RequestHTTP.postData(url, new BasicNameValuePair("123", "222"), new
	 * BasicNameValuePair("123", "222"), ...); BasicNameValuePair
	 * 欄位可以無限延伸，前一個參數是 url 的對應欄位，後一個參數是欄位輸入值
	 * 
	 * @param url (String) url
	 * @param basicNameValuePairs (BasicNameValuePair...) ex: new BasicNameValuePair("123", "222")
	 * @return
	 */
	public static String httpPost(String url, OnExceptionListener onExceptionListener,
			BasicNameValuePair... basicNameValuePairs) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.clear();

		for (int i = 0; i < basicNameValuePairs.length; i++) {
			params.add(basicNameValuePairs[i]);
		}
		return httpPost(url, params, onExceptionListener);
	}
	
	/**
	 * <br>利用put取得資料，不偵測錯誤事件，若要偵測錯誤事件請使用
	 * <br>httpPut(String url, ArrayList<NameValuePair> params, OnExceptionListener onExceptionListener)
	 * @param url (String) url
	 * @param params (ArrayList<NameValuePair>)
	 * @return (String) url 回傳結果
	 */
	public static String httpPut(String url, ArrayList<NameValuePair> params){
		return httpPut(url, params, null);
	}
	
	/**
	 * <br>利用put取得資料，
	 * 
	 * @param url (String) url
	 * @param params (ArrayList<NameValuePair>)
	 * @param onExceptionListener (OnExceptionListener) 可取得exception資料
	 * @return (String) url 回傳結果
	 */
	public static String httpPut(String url, ArrayList<NameValuePair> params, OnExceptionListener onExceptionListener){
		HttpClient client = new DefaultHttpClient();
		HttpPut put= new HttpPut(url);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		try {
			put.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}

		try {
			HttpResponse response = client.execute(put);
			String strResult = EntityUtils.toString(response.getEntity());
			return strResult;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		} catch (IOException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}
		
		return null;
	}
	
	/**
	 * 利用post取得資料，
	 * 
	 * @param url (String) url
	 * @param params (ArrayList<NameValuePair>)
	 * @param onExceptionListener (OnExceptionListener) 可取得exception資料
	 * @return (String) url 回傳結果
	 */
	public static String httpDelete(String url, ArrayList<NameValuePair> params, OnExceptionListener onExceptionListener) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpDelete httpDelete = new HttpDelete(url);
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				timeoutConnection);
		int timeoutSocket = 10000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		try {
			HttpResponse response = httpClient.execute(httpDelete);
			String strResult = EntityUtils.toString(response.getEntity());
			return strResult;

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		} catch (IOException e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}

		return null;
	}

	/**
	 * 下載檔案
	 * @param urlString
	 * @param filePath
	 * @param fileName
	 * @throws IOException
	 */
	public static void downloadFile (String urlString, String filePath, String fileName){
		downloadFile (urlString, filePath, fileName, null);
	}
	
	/**
	 * 下載檔案
	 * @param urlString
	 * @param filePath
	 * @param fileName
	 * @throws IOException
	 */
	public static boolean downloadFile (String urlString, String filePath, String fileName, OnExceptionListener onExceptionListener){
		if(!filePath.endsWith("/")){
			filePath = filePath+"/";
		}
		File dir = new File(filePath);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		File file = new File(filePath, fileName);

		try{
			URL url = new URL(urlString);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setConnectTimeout(30000);
	        conn.setReadTimeout(30000);
	        conn.setInstanceFollowRedirects(true);
	        InputStream input = conn.getInputStream();
	        OutputStream output = new FileOutputStream(file);
	
	        copyStream(input, output, onExceptionListener);
	        output.close();
	        return true;
		}
		catch(IOException e){
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
	        return false;
		}
	}
	
	private static void copyStream(InputStream is, OutputStream os, OnExceptionListener onExceptionListener)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception e){
        	e.printStackTrace();
        	if(onExceptionListener != null) onExceptionListener.onError(e);
        }
    }

	public static String uploadFile(String uploadUrl, String srcPath) {
		return uploadFile(uploadUrl, srcPath, null);
	}
	/**
	 * 上傳檔案
	 * 
	 * @param uploadUrl
	 *            (String) 上傳的 Url
	 * @param srcPath
	 *            (String) 要上傳的檔案路徑
	 * @return (String) 上傳後，server 回傳訊息
	 */
	public static String uploadFile(String uploadUrl, String srcPath, OnExceptionListener onExceptionListener) {
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "******";
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			httpURLConnection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			DataOutputStream dos = new DataOutputStream(
					httpURLConnection.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
					+ srcPath.substring(srcPath.lastIndexOf("/") + 1)
					+ "\""
					+ end);
			dos.writeBytes(end);

			FileInputStream fis = new FileInputStream(srcPath);
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);

			}
			fis.close();

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			InputStream is = httpURLConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String result = br.readLine();

			Log.v(TAG, "result: " + result);
			dos.close();
			is.close();

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			if(onExceptionListener != null) onExceptionListener.onError(e);
		}

		return null;
	}
}
