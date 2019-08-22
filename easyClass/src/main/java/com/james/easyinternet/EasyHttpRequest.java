/*
 * Copyright 2014 Thinkermobile, Inc.
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.gogolook.developmode.BasicRageShake;
import com.james.easyclass.model.Res;
import com.james.easydatabase.EasySharedPreference;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

/**
 * <br>
 * A framework to connect with a URL by GET, POST, PUT and DELETE. <br>
 * Developers may choose a cache policy to decide how to load from URL. <br>
 * Call in an activity for example:
 * <p>
 * <pre>
 * new EasyHttpRequest(this, endPointUrl).init().setMethod(METHOD_POST)
 * 		.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK).addParams(key1, value1)
 * 		.addParams(key2, value2)
 * 		.executeInBackground(branch, onEasyApiCallbackListener);
 * </pre>
 * <p>
 * <br>
 * need to add following permisions, android.permission.INTERNET, android.permission.ACCESS_NETWORK_STATE
 *
 * @author JamesX
 * @see <a href="http://androidthinkermobile.blogspot.tw/">Introduction for EasyHttpRequest</a>
 * @since 2014/02/17
 */
public class EasyHttpRequest {
    private static final String TAG = EasyHttpRequest.class.getSimpleName();

    public static final String METHOD_GET = "get";
    public static final String METHOD_POST = "post";
    public static final String METHOD_PUT = "put";
    public static final String METHOD_DELETE = "delete";

    private static boolean sIsCustomSSL = false;

    private static HashMap<String, String> responseMap = new HashMap<String, String>();
    private EasySharedPreference easySharedPreference;

    private String mDoamin;
    private static Context context;

    private String method = METHOD_GET;
    private int mode = CachePolicy.NETWORK_ONLY;

    private ArrayList<Pair<String, String>> headers = new ArrayList<Pair<String, String>>();
    private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    private ArrayList<Pair<String, JSONObject>> params2 = new ArrayList<Pair<String, JSONObject>>();
    private ArrayList<String> ignoreKeys = new ArrayList<String>();

    public static boolean debug = true;

    private boolean encodeBodyAsJson = false;

    private boolean howErrorToast = true;

    private int maxCache = 100;
    private int durableCache = 50;

    public static class CachePolicy {
        public static final int NETWORK_ONLY = 1;
        public static final int TEMP_THEN_NETWORK = 2;
        public static final int CACHE_THEN_NETWORK = 3;
    }

    /**
     * <br>
     * A framework to connect with a URL by GET, POST, PUT and DELETE. <br>
     * Developers may choose a cache policy to decide how to load from URL. <br>
     * Call in an activity for example:
     * <p>
     * <pre>
     * new EasyHttpRequest(this, endPointUrl).init().setMethod(METHOD_POST)
     * 		.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK).addParams(key1, value1)
     * 		.addParams(key2, value2)
     * 		.executeInBackground(branch, onEasyApiCallbackListener);
     * </pre>
     *
     * @param context (Context)
     * @param domain  (String) domain of the target URL.
     * @see <a href="http://androidthinkermobile.blogspot.tw/">Introduction for EasyHttpRequest</a>
     */
    public EasyHttpRequest(Context context, String domain) {
        this.context = context;

        easySharedPreference = new EasySharedPreference(context,
                context.getPackageName());

        this.mDoamin = domain.endsWith("/") ? (String) domain.subSequence(0,
                domain.length() - 1) : domain;
    }

    public Context getContext() {
        return context;
    }

    public EasyHttpRequest encodeBodyAsJson(boolean encodeBodyAsJson) {
        this.encodeBodyAsJson = encodeBodyAsJson;
        return this;
    }

    public EasyHttpRequest setCacheSpace(int maxCache, int durableCache) {
        this.maxCache = maxCache;
        this.durableCache = durableCache;
        return this;
    }

    public static void setIsCustomSSL(boolean isCustomSSL) {
        sIsCustomSSL = isCustomSSL;
    }

    public static boolean isCustomSSL() {
        return sIsCustomSSL;
    }

    /**
     * init or reset method to METHOD_GET and cachePolicy to NETWORK_ONLY.
     *
     * @return
     */
    public EasyHttpRequest init() {
        method = METHOD_GET;
        mode = CachePolicy.NETWORK_ONLY;
        headers.clear();
        params.clear();
        params2.clear();
        ignoreKeys.clear();

        releaseCacheSpaceIfNecessary();
        return this;
    }

    /**
     * <br>
     * English Explanation: <br>
     * Add header parameters.
     *
     * @param headerKey   (String) key of parameter.
     * @param headerValue (String) value of parameter.
     * @return
     */
    public EasyHttpRequest addHeader(String headerKey, String headerValue) {
        HashMap<String, String> tmpMap = new HashMap<String, String>();
        for (Pair<String, String> pair : headers) {
            tmpMap.put(pair.first, pair.second);
        }
        if (tmpMap.containsKey(headerKey)) {
            tmpMap.put(headerKey, headerValue);
        } else {
            headers.add(new Pair<String, String>(headerKey, headerValue));
        }
        return this;
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 增加參數 <br>
     * <br>
     * English Explanation: <br>
     * Add parameters.
     *
     * @param key   (String) key of parameter.
     * @param value (String) value of parameter.
     * @return
     */
    public EasyHttpRequest addParams(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
        return this;
    }

    public EasyHttpRequest addParamsWithoutCacheKey(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
        ignoreKeys.add(key);
        return this;
    }

    public EasyHttpRequest addJSONParams(String key, JSONObject valueObject) {
        params2.add(new Pair<String, JSONObject>(key, valueObject));
        return this;
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 設定串接方式，可以選擇METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE <br>
     * 或是直接使用"get","post","put","delete"亦可 <br>
     * <br>
     * English Explanation: <br>
     * Set method as METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE <br>
     * or "get","post","put","delete" as well.
     *
     * @param method (String) METHOD_GET, METHOD_POST, METHOD_PUT, METHOD_DELETE
     * @return
     */
    public EasyHttpRequest setMethod(String method) {
        this.method = method.toLowerCase();
        return this;
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 可以選擇NETWORK_ONLY, TEMP_THEN_NETWORK, CACHE_THEN_NETWORK <br>
     * <br>
     * English Explanation: <br>
     * Set NETWORK_ONLY, TEMP_THEN_NETWORK, CACHE_THEN_NETWORK <br>
     * <br>
     * NETWORK_ONLY: only load network data <br>
     * TEMP_THEN_NETWORK: load data in memory first and refresh from network if necessary. <br>
     * CACHE_THEN_NETWORK: load data in cache first and refresh from network if necessary.
     *
     * @param mode (int) NETWORK_ONLY, TEMP_THEN_NETWORK, CACHE_THEN_NETWORK
     * @return
     */
    public EasyHttpRequest setCachePolicy(int mode) {
        this.mode = mode;
        return this;
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 建議使用executeInBackground(final String baseUrl, final OnEasyApiCallbackListener onEasyApiCallbackListener) <br>
     * <br>
     * English Explanation: <br>
     * Suggest to use executeInBackground(final String baseUrl, final OnEasyApiCallbackListener onEasyApiCallbackListener)
     *
     * @param baseUrl                   (String) 目標url的額外路徑
     * @param onEasyApiCallbackListener (OnEasyApiCallbackListener) 呼叫url後的callback
     */
    public void findInBackground(final String baseUrl,
                                 final OnEasyApiCallbackListener onEasyApiCallbackListener) {
        executeInBackground(baseUrl, onEasyApiCallbackListener);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 跟目標url要求回應資料 <br>
     * <br>
     * English Explanation: <br>
     * Start request URL in background.
     *
     * @param baseUrl                   (String) 目標url的額外路徑
     * @param onEasyApiCallbackListener (OnEasyApiCallbackListener) 呼叫url後的callback
     */
    public void executeInBackground(final String baseUrl,
                                    final OnEasyApiCallbackListener onEasyApiCallbackListener) {

        String url = (baseUrl.startsWith("/")) ? mDoamin + baseUrl : mDoamin + "/" + baseUrl;
        Log(TAG, "url: " + url);

        String keyString = null;
        for (int i = 0; i < params.size(); i++) {
            if (ignoreKeys.contains(params.get(i).getName()))
                continue;

            String pair = params.get(i).getName() + "=" + params.get(i).getValue();
            if (keyString == null) {
                keyString = pair;
            } else {
                keyString = keyString + "&" + params.get(i).getName() + "="
                        + params.get(i).getValue();
            }
        }

        String paramString = null;
        for (int i = 0; i < params.size(); i++) {
            String pair = params.get(i).getName() + "=" + params.get(i).getValue();
            if (paramString == null) {
                paramString = pair;
            } else {
                paramString = paramString + "&" + params.get(i).getName() + "=" + params.get(i).getValue();
            }
        }

        final EasyResponseObject response = new EasyResponseObject();
        String key = method + url + "?" + keyString;

        if (method.equalsIgnoreCase(METHOD_GET)) {
            if (paramString != null) {
                handleRequest(key, url + "?" + paramString, response, onEasyApiCallbackListener);
            } else {
                handleRequest(key, url, response, onEasyApiCallbackListener);
            }
        } else if (method.equalsIgnoreCase(METHOD_POST)) {
            handleRequest(key, url, response, onEasyApiCallbackListener);
        } else if (method.equalsIgnoreCase(METHOD_PUT)) {
            handleRequest(key, url, response, onEasyApiCallbackListener);
        } else if (method.equalsIgnoreCase(METHOD_DELETE)) {
            if (paramString != null) {
                handleRequest(key, url + "?" + paramString, response, onEasyApiCallbackListener);
            } else {
                handleRequest(key, url, response, onEasyApiCallbackListener);
            }
        }
    }

    @SuppressWarnings("rawtypes")
    @SuppressLint("NewApi")
    private void handleRequest(final String key, final String url,
                               final EasyResponseObject response,
                               final OnEasyApiCallbackListener onEasyApiCallbackListener) {
        // 取得memory暫存資料
        final String temp = responseMap.get(key);
        if (mode == CachePolicy.TEMP_THEN_NETWORK && temp != null) {
            Log(TAG, "from temp");
            try {
                onEasyApiCallbackListener.onDone(new EasyResponseObject(temp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 取得SharedPreference暫存資料
        final String cache = easySharedPreference.getString(encode(key), null);
        if (mode == CachePolicy.CACHE_THEN_NETWORK && cache != null) {
            Log(TAG, "from cache");
            try {
                onEasyApiCallbackListener.onDone(new EasyResponseObject(cache));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!hasNetwork(getContext())) {
            BasicRageShake.getInstance(context, null).putAPILog(method + ":\n" + "url: " + url + "\nexception: " + Res.string.toast_network_not_found);

            if (howErrorToast) {
                Toast.makeText(context, Res.string.toast_network_not_found, Toast.LENGTH_LONG).show();
            }
            return;
        }

        final OnEasyHttpExceptionListener onHttpExceptionListener = new OnEasyHttpExceptionListener() {

            @Override
            public void onError(final Exception exception) {
                BasicRageShake.getInstance(context, null).putAPILog(method + ":\n" + "url: " + url + "\nexception: " + exception.getMessage());

                if (context instanceof Activity) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (onEasyApiCallbackListener.onExceptionHandle(exception)) {
                                return;
                            }
                            if (exception instanceof TimeoutException) {
                                if (howErrorToast) {
                                    Toast.makeText(context, Res.string.toast_network_timeout, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                if (howErrorToast) {
                                    Toast.makeText(context, Res.string.toast_network_fail, Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                } else {
                    if (onEasyApiCallbackListener.onExceptionHandle(exception)) {
                        return;
                    }
                }
            }
        };

        AsyncTask<Void, Void, Boolean> asyncTask = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                BasicRageShake.getInstance(context, null).putAPILog(method + ":\n" + "url: " + url + "\nresponse header: " + response.getHeader() + "\nresponse body: " + response.getBody() + "\nstart: " + System.currentTimeMillis());
            }

            @Override
            protected Boolean doInBackground(Void... p) {
                boolean success = false;
                if (method.equalsIgnoreCase(METHOD_GET)) {
                    success = httpGet(response, url, headers, onHttpExceptionListener);
                } else if (method.equalsIgnoreCase(METHOD_POST)) {
                    success = httpPost(response, encodeBodyAsJson, url, headers, params, params2, onHttpExceptionListener);
                } else if (method.equalsIgnoreCase(METHOD_PUT)) {
                    success = httpPut(response, encodeBodyAsJson, url, headers, params, params2, onHttpExceptionListener);
                } else if (method.equalsIgnoreCase(METHOD_DELETE)) {
                    success = httpDelete(response, url, headers, params, onHttpExceptionListener);
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                if (success) {
                    if (mode == CachePolicy.NETWORK_ONLY) {
                        onEasyApiCallbackListener.onDone(response);
                        Log(TAG, "from network");
                    } else if (mode == CachePolicy.TEMP_THEN_NETWORK) {
                        try {
                            if (!new EasyResponseObject(temp).toString().equalsIgnoreCase(response.toString())) {
                                responseMap.put(key, response.toString());
                                onEasyApiCallbackListener.onDone(response);
                                Log(TAG, "from temp then network");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (mode == CachePolicy.CACHE_THEN_NETWORK) {
                        try {
                            if (!new EasyResponseObject(cache).toString().equalsIgnoreCase(response.toString())) {
                                easySharedPreference.putString(encode(key), response.toString());
                                onEasyApiCallbackListener.onDone(response);
                                Log(TAG, "from cache then network");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                init();
                BasicRageShake.getInstance(context, null).putAPILog(method + ":\n" + "url: " + url + "\nresponse header: " + response.getHeader() + "\nresponse body: " + response.getBody() + "\nend: " + System.currentTimeMillis());
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asyncTask.execute();
        }
    }

    private void releaseCacheSpaceIfNecessary() {
        //
        Map<String, String> map = (Map<String, String>) easySharedPreference.getSaver().getAll();
        //
        Set<String> keyset = map.keySet();
        Log(TAG, "keyset size: " + keyset.size());
        if (keyset.size() > maxCache) {
            Iterator<String> keys = keyset.iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                Log(TAG, "remove key: " + key);

                easySharedPreference.getEditor().remove(key).commit();

                if (easySharedPreference.getSaver().getAll().size() <= durableCache) {
                    break;
                }
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------

    /**
     * <br>
     * Chinese Explanation: <br>
     * 取得URL的內容 <br>
     * <br>
     * English Explanation: <br>
     * load data from URL
     *
     * @param url (String)
     * @return (String) Response from server side
     */
    public static String httpGet(String url) {
        return httpGet(url, null);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 取得URL的內容 <br>
     * <br>
     * English Explanation: <br>
     * load data from URL
     *
     * @param url                 (String)
     * @param onExceptionListener (OnExceptionListener)
     * @return (String) Response from server side
     */
    public static String httpGet(String url,
                                 OnEasyHttpExceptionListener onExceptionListener) {
        EasyResponseObject ecHttpResponse = new EasyResponseObject();
        httpGet(ecHttpResponse, url, onExceptionListener);
        return ecHttpResponse.getBody();
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 取得URL的內容 <br>
     * <br>
     * English Explanation: <br>
     * load data from URL
     *
     * @param ecHttpResponse      (EasyResponseObject) contains header and body.
     * @param url
     * @param onExceptionListener
     * @return
     */
    public static boolean httpGet(EasyResponseObject ecHttpResponse,
                                  String url, OnEasyHttpExceptionListener onExceptionListener) {
        return httpGet(ecHttpResponse, url, null, onExceptionListener);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 取得URL的內容 <br>
     * <br>
     * English Explanation: <br>
     * load data from URL
     *
     * @param responseObject      (EasyResponseObject) contains header and body.
     * @param url
     * @param onExceptionListener
     * @return
     */
    public static boolean httpGet(EasyResponseObject responseObject,
                                  String url, ArrayList<Pair<String, String>> headers,
                                  OnEasyHttpExceptionListener onExceptionListener) {
        Log(TAG, "get url: " + url);
        if (EasyHttpRequest.context != null) {
            BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("get url: " + url);
        }

        HttpGet httpGet = new HttpGet(url);

        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                httpGet.addHeader(headers.get(i).first, headers.get(i).second);
                Log(TAG, "get header -> " + headers.get(i).first + ": " + headers.get(i).second);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("get header -> " + headers.get(i).first + ": " + headers.get(i).second);
                }
            }
        }

        try {
            final DefaultHttpClient httpClient;
            if (sIsCustomSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams httpParams = new BasicHttpParams();
                HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);
                httpClient = new DefaultHttpClient(ccm, httpParams);
            } else {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);
                httpClient = new DefaultHttpClient(httpParams);
            }

            HttpResponse response = httpClient.execute(httpGet);
            responseObject.setHeader(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());
            responseObject.setBody(EntityUtils.toString(response.getEntity()));
            Log(TAG, "get response header: " + responseObject.getHeader());
            Log(TAG, "get response headers: " + (response.getAllHeaders() == null));
            Log(TAG, "get response body: " + responseObject.getBody());

            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        }
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用POST處理 <br>
     * <br>
     * English Explanation: <br>
     * POST data to server side.
     *
     * @param url    (String) url
     * @param params (ArrayList<NameValuePair>)
     * @return (String) url 回傳結果
     */
    public static String httpPost(boolean encodeBodyAsJson, String url,
                                  ArrayList<NameValuePair> params) {
        return httpPost(encodeBodyAsJson, url, params, null);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用POST處理 <br>
     * <br>
     * English Explanation: <br>
     * POST data to server side.
     *
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     * @return (String) url 回傳結果
     */
    public static String httpPost(boolean encodeBodyAsJson, String url,
                                  ArrayList<NameValuePair> params,
                                  OnEasyHttpExceptionListener onExceptionListener) {
        EasyResponseObject ecHttpResponse = new EasyResponseObject();
        httpPost(ecHttpResponse, encodeBodyAsJson, url, params,
                onExceptionListener);
        return ecHttpResponse.getBody();
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用POST處理 <br>
     * <br>
     * English Explanation: <br>
     * POST data to server side.
     *
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     */
    public static boolean httpPost(EasyResponseObject ecHttpResponse,
                                   boolean encodeBodyAsJson, String url,
                                   ArrayList<NameValuePair> params,
                                   OnEasyHttpExceptionListener onExceptionListener) {
        return httpPost(ecHttpResponse, encodeBodyAsJson, url, null, params,
                null, onExceptionListener);
    }

    public static boolean httpPost(EasyResponseObject ecHttpResponse,
                                   boolean encodeBodyAsJson, String url,
                                   ArrayList<NameValuePair> params,
                                   ArrayList<Pair<String, JSONObject>> params2,
                                   OnEasyHttpExceptionListener onExceptionListener) {
        return httpPost(ecHttpResponse, encodeBodyAsJson, url, null, params,
                params2, onExceptionListener);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用POST處理 <br>
     * <br>
     * English Explanation: <br>
     * POST data to server side.
     *
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     * @return (String) response 回傳結果
     */
    public static boolean httpPost(EasyResponseObject responseObject,
                                   boolean encodeBodyAsJson, String url,
                                   ArrayList<Pair<String, String>> headers,
                                   ArrayList<NameValuePair> params,
                                   ArrayList<Pair<String, JSONObject>> params2,
                                   OnEasyHttpExceptionListener onExceptionListener) {
        Log(EasyHttpRequest.class.getSimpleName(), "post url: " + url);
        if (EasyHttpRequest.context != null) {
            BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("post url: " + url);
        }

        HttpPost httpPost = new HttpPost(url);

        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                httpPost.addHeader(headers.get(i).first, headers.get(i).second);
                Log(TAG, "post header -> " + headers.get(i).first + ": " + headers.get(i).second);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("post header -> " + headers.get(i).first + ": " + headers.get(i).second);
                }
            }
        }

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 60000;
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        int timeoutSocket = 60000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        try {
            if (encodeBodyAsJson) {
                httpPost.addHeader("Content-type", "application/json; charset=UTF-8");

                JSONObject obj = new JSONObject();
                for (NameValuePair param : params) {
                    obj.put(param.getName(), param.getValue());
                    Log(TAG, "get body -> " + param.getName() + ": " + param.getValue());
                }
                if (params2 != null) {
                    for (Pair<String, JSONObject> param2 : params2) {
                        obj.put(param2.first, param2.second);
                        Log(TAG, "get body -> " + param2.first + ": " + param2.second.toString());
                    }
                }
                Log(TAG, "post json body: " + obj.toString());
                StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
                entity.setContentType("application/json; charset=utf-8");

                httpPost.setEntity(entity);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("post params -> " + entity.toString());
                }
            } else {
                for (NameValuePair param : params) {
                    Log(TAG, "get body -> " + param.getName() + ": " + param.getValue());
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                httpPost.setEntity(entity);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("post params -> " + entity.toString());
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try {
            final DefaultHttpClient httpClient;
            if (sIsCustomSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams httpParams = new BasicHttpParams();
                HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
                HttpConnectionParams.setSoTimeout(httpParams, 60000);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);
                httpClient = new DefaultHttpClient(ccm, httpParams);
            } else {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
                HttpConnectionParams.setSoTimeout(httpParams, 60000);
                httpClient = new DefaultHttpClient(httpParams);
            }

            HttpResponse response = httpClient.execute(httpPost);
            responseObject.setHeader(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());
            responseObject.setBody(EntityUtils.toString(response.getEntity()));
            Log(TAG, "get response header: " + responseObject.getHeader());
            Log(TAG, "get response body: " + responseObject.getBody());
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        }
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用PUT處理 <br>
     * <br>
     * English Explanation: <br>
     * PUT data to server side. <br>
     * <br>
     * To listene to exception, please use <br>
     * <code> httpPut(String url, ArrayList<NameValuePair> params, OnExceptionListener onExceptionListener)
     *
     * @param url    (String) url
     * @param params (ArrayList<NameValuePair>)
     * @return (String) response 回傳結果
     */
    public static String httpPut(boolean encodeBodyAsJson, String url,
                                 ArrayList<NameValuePair> params) {
        return httpPut(encodeBodyAsJson, url, params, null);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用PUT處理 <br>
     * <br>
     * English Explanation: <br>
     * PUT data to server side.
     *
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     * @return (String) response 回傳結果
     */
    public static String httpPut(boolean encodeBodyAsJson, String url,
                                 ArrayList<NameValuePair> params,
                                 OnEasyHttpExceptionListener onExceptionListener) {
        EasyResponseObject ecHttpResponse = new EasyResponseObject();
        httpPut(ecHttpResponse, encodeBodyAsJson, url, params,
                onExceptionListener);
        return ecHttpResponse.getBody();
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用PUT處理 <br>
     * <br>
     * English Explanation: <br>
     * PUT data to server side.
     *
     * @param ecHttpResponse
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     */
    public static boolean httpPut(EasyResponseObject ecHttpResponse,
                                  boolean encodeBodyAsJson, String url,
                                  ArrayList<NameValuePair> params,
                                  OnEasyHttpExceptionListener onExceptionListener) {
        return httpPut(ecHttpResponse, encodeBodyAsJson, url, null, params,
                null, onExceptionListener);
    }

    public static boolean httpPut(EasyResponseObject ecHttpResponse,
                                  boolean encodeBodyAsJson, String url,
                                  ArrayList<NameValuePair> params,
                                  ArrayList<Pair<String, JSONObject>> params2,
                                  OnEasyHttpExceptionListener onExceptionListener) {
        return httpPut(ecHttpResponse, encodeBodyAsJson, url, null, params,
                params2, onExceptionListener);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用PUT處理 <br>
     * <br>
     * English Explanation: <br>
     * PUT data to server side.
     *
     * @param responseObject
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     */
    public static boolean httpPut(EasyResponseObject responseObject,
                                  boolean encodeBodyAsJson, String url,
                                  ArrayList<Pair<String, String>> headers,
                                  ArrayList<NameValuePair> params,
                                  ArrayList<Pair<String, JSONObject>> params2,
                                  OnEasyHttpExceptionListener onExceptionListener) {
        Log(TAG, "put url: " + url);
        if (EasyHttpRequest.context != null) {
            BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("put url: " + url);
        }
        HttpPut httpPut = new HttpPut(url);

        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                httpPut.addHeader(headers.get(i).first, headers.get(i).second);
                Log(TAG, "put header -> " + headers.get(i).first + ": " + headers.get(i).second);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("put header -> " + headers.get(i).first + ": " + headers.get(i).second);
                }
            }
        }

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        try {
            if (encodeBodyAsJson) {
                JSONObject obj = new JSONObject();
                for (NameValuePair param : params) {
                    obj.put(param.getName(), param.getValue());
                    Log(TAG, "get body -> " + param.getName() + ": " + param.getValue());
                }
                if (params2 != null) {
                    for (Pair<String, JSONObject> param2 : params2) {
                        obj.put(param2.first, param2.second);
                        Log(TAG, "get body -> " + param2.first + ": " + param2.second.toString());
                    }
                }
                Log(TAG, "put json body: " + obj.toString());
                StringEntity entity = new StringEntity(obj.toString(), HTTP.UTF_8);
                entity.setContentType("application/json; charset=utf-8");

                httpPut.setEntity(entity);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("put params -> " + entity.toString());
                }
            } else {
                for (NameValuePair param : params) {
                    Log(TAG, "get body -> " + param.getName() + ": " + param.getValue());
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                httpPut.setEntity(entity);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("put params -> " + entity.toString());
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        try {
            final DefaultHttpClient httpClient;
            if (sIsCustomSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams httpParams = new BasicHttpParams();
                HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);
                httpClient = new DefaultHttpClient(ccm, httpParams);
            } else {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);
                httpClient = new DefaultHttpClient(httpParams);
            }

            HttpResponse response = httpClient.execute(httpPut);
            responseObject.setHeader(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());
            responseObject.setBody(EntityUtils.toString(response.getEntity()));
            Log(TAG, "get response header: " + responseObject.getHeader());
            Log(TAG, "get response body: " + responseObject.getBody());
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        }
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用DELETE處理 <br>
     * <br>
     * English Explanation: <br>
     * DELETE data on server side.
     *
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     * @return (String) response 回傳結果
     */
    public static String httpDelete(String url,
                                    ArrayList<NameValuePair> params,
                                    OnEasyHttpExceptionListener onExceptionListener) {
        EasyResponseObject ecHttpResponse = new EasyResponseObject();
        httpDelete(ecHttpResponse, url, params, onExceptionListener);
        return ecHttpResponse.getBody();
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用DELETE處理 <br>
     * <br>
     * English Explanation: <br>
     * DELETE data on server side.
     *
     * @param ecHttpResponse
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     */
    public static boolean httpDelete(EasyResponseObject ecHttpResponse,
                                     String url, ArrayList<NameValuePair> params,
                                     OnEasyHttpExceptionListener onExceptionListener) {
        return httpDelete(ecHttpResponse, url, null, params,
                onExceptionListener);
    }

    /**
     * <br>
     * Chinese Explanation: <br>
     * 用DELETE處理 <br>
     * <br>
     * English Explanation: <br>
     * DELETE data on server side.
     *
     * @param responseObject
     * @param url                 (String) url
     * @param params              (ArrayList<NameValuePair>)
     * @param onExceptionListener (OnExceptionListener) 可取得exception資料
     */
    public static boolean httpDelete(EasyResponseObject responseObject,
                                     String url, ArrayList<Pair<String, String>> headers,
                                     ArrayList<NameValuePair> params,
                                     OnEasyHttpExceptionListener onExceptionListener) {
        Log(TAG, "delet url: " + url);
        if (EasyHttpRequest.context != null) {
            BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("delet url: " + url);
        }
        HttpDelete httpDelete = new HttpDelete(url);

        if (headers != null) {
            for (int i = 0; i < headers.size(); i++) {
                httpDelete.addHeader(headers.get(i).first, headers.get(i).second);
                Log(TAG, "get header -> " + headers.get(i).first + ": " + headers.get(i).second);
                if (EasyHttpRequest.context != null) {
                    BasicRageShake.getInstance(EasyHttpRequest.context, null).putAPILog("delet header -> " + headers.get(i).first + ": " + headers.get(i).second);
                }
            }
        }

        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        try {
            final DefaultHttpClient httpClient;
            if (sIsCustomSSL) {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                trustStore.load(null, null);

                SSLSocketFactory sf = new SSLSocketFactoryEx(trustStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams httpParams = new BasicHttpParams();
                HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(httpParams, HTTP.UTF_8);
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, registry);
                httpClient = new DefaultHttpClient(ccm, httpParams);
            } else {
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpConnectionParams.setSoTimeout(httpParams, 10000);
                httpClient = new DefaultHttpClient(httpParams);
            }

            HttpResponse response = httpClient.execute(httpDelete);
            responseObject.setHeader(response.getStatusLine().getStatusCode());
            responseObject.setHeaders(response.getAllHeaders());
            responseObject.setBody(EntityUtils.toString(response.getEntity()));
            Log(TAG, "get response header: " + responseObject.getHeader());
            Log(TAG, "get response body: " + responseObject.getBody());
            return true;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (CertificateException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyStoreException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        } catch (KeyManagementException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        }
    }

    /**
     * Download file
     *
     * @param urlString
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static void downloadFile(String urlString, String filePath,
                                    String fileName) {
        downloadFile(urlString, filePath, fileName, null);
    }

    /**
     * Download file
     *
     * @param urlString
     * @param filePath
     * @param fileName
     * @throws IOException
     */
    public static boolean downloadFile(String urlString, String filePath,
                                       String fileName, OnEasyHttpExceptionListener onExceptionListener) {
        if (!filePath.endsWith("/")) {
            filePath = filePath + "/";
        }
        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(filePath, fileName);

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream input = conn.getInputStream();
            OutputStream output = new FileOutputStream(file);

            copyStream(input, output, onExceptionListener);
            output.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
            return false;
        }
    }

    private static void copyStream(InputStream is, OutputStream os,
                                   OnEasyHttpExceptionListener onExceptionListener) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1) {
                    break;
                }
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
        }
    }

    /**
     * Upload file
     *
     * @param uploadUrl (String) 上傳的 Url
     * @param srcPath   (String) 要上傳的檔案路徑
     * @return (String) 上傳後，server 回傳訊息
     */
    public static String uploadFile(String uploadUrl, String srcPath) {
        return uploadFile(uploadUrl, srcPath, null);
    }

    /**
     * Upload file
     *
     * @param uploadUrl           (String) 上傳的 Url
     * @param srcPath             (String) 要上傳的檔案路徑
     * @param onExceptionListener (OnEasyHttpExceptionListener) 例外狀況的監聽
     * @return (String) 上傳後，server 回傳訊息
     */
    public static String uploadFile(String uploadUrl, String srcPath,
                                    OnEasyHttpExceptionListener onExceptionListener) {
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

            dos.close();
            is.close();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (onExceptionListener != null) {
                onExceptionListener.onError(e);
            }
        }

        return null;
    }

    private boolean hasNetwork(Context _context) {
        ConnectivityManager cm = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean hasNetwork = (cm.getActiveNetworkInfo() != null && cm
                .getActiveNetworkInfo().isAvailable());
        return hasNetwork;
    }

    private static void Log(String tag, String msg) {
        if (debug) {
            Log.i(tag, msg);
        }
    }

    private String encode(String decoded) {
        return String.valueOf(decoded.hashCode());
    }

    public void setShowErrorToast(boolean showErrorToast) {
        this.howErrorToast = howErrorToast;
    }

    public boolean howErrorToast() {
        return howErrorToast;
    }
}
