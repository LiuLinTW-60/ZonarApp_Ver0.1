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

package com.james.easydatabase;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class EasySharedPreference {
	private SharedPreferences sharedPreferences;

	public EasySharedPreference(Context context) {
		sharedPreferences = context.getSharedPreferences("share_pref", Context.MODE_PRIVATE);
	}

	public EasySharedPreference(Context context, String name) {
		sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
	}

	public EasySharedPreference(Context context, String name, int mode) {
		sharedPreferences = context.getSharedPreferences(name, mode);
	}

	public SharedPreferences getSaver() {
		return sharedPreferences;
	}

	public SharedPreferences.Editor getEditor() {
		return sharedPreferences.edit();
	}

	public boolean contains(String key) {
		return sharedPreferences.contains(key);
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defValue) {
		return sharedPreferences.getBoolean(key, defValue);
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public String getString(String key, String defValue) {
		return sharedPreferences.getString(key, defValue);
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public int getInt(String key, int defValue) {
		return sharedPreferences.getInt(key, defValue);
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public long getLong(String key, long defValue) {
		return sharedPreferences.getLong(key, defValue);
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public float getFloat(String key, float defValue) {
		return sharedPreferences.getFloat(key, defValue);
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public void putBoolean(String key, boolean defValue) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			sharedPreferences.edit().putBoolean(key, defValue).apply();
		}
		else {
			sharedPreferences.edit().putBoolean(key, defValue).commit();
		}
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public void putString(String key, String defValue) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			sharedPreferences.edit().putString(key, defValue).apply();
		}
		else {
			sharedPreferences.edit().putString(key, defValue).commit();
		}
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public void putInt(String key, int defValue) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			sharedPreferences.edit().putInt(key, defValue).apply();
		}
		else {
			sharedPreferences.edit().putInt(key, defValue).commit();
		}
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public void putLong(String key, long defValue) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			sharedPreferences.edit().putLong(key, defValue).apply();
		}
		else {
			sharedPreferences.edit().putLong(key, defValue).commit();
		}
	}

	/**
	 * @param key
	 * @param defValue
	 * @return
	 */
	public void putFloat(String key, float defValue) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			sharedPreferences.edit().putFloat(key, defValue).apply();
		}
		else {
			sharedPreferences.edit().putFloat(key, defValue).commit();
		}
	}

	/**
	 * @param key
	 */
	public void remove(String key) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
			sharedPreferences.edit().remove(key).apply();
		}
		else {
			sharedPreferences.edit().remove(key).commit();
		}
	}
}
