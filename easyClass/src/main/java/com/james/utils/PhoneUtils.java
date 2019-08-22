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

package com.james.utils;

import java.security.MessageDigest;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @author JamesX
 */
public class PhoneUtils {

	/**
	 * 取得手機UDID
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceID(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String id = tm.getDeviceId();
		if (id == null || id.length() == 0) {
			id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		}
		return id;
	}

	/**
	 * 取得目前app版本
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurrentVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();

		try {
			PackageInfo info = packageManager.getPackageInfo(packageName, 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			return -1;
		}
	}

	/**
	 * 取得目前app版本名稱
	 * 
	 * @param context
	 * @return
	 */
	public static String getCurrentVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();

		try {
			PackageInfo info = packageManager.getPackageInfo(packageName, 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * 是否有網路
	 * 
	 * @param _context
	 * @return
	 */
	public static boolean hasNetwork(Context _context) {
		ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean hasNetwork = (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable());
		return hasNetwork;
	}

	/**
	 * 是否有wifi
	 * 
	 * @param _context
	 * @return
	 */
	public static boolean hasWiFi(Context _context) {
		WifiManager wifimgr = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);
		boolean hasNetwork = wifimgr.isWifiEnabled();
		return hasNetwork;
	}

	// MD5加密，32位
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
