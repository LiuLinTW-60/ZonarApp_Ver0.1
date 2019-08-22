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

import android.os.Debug;
import android.util.Log;

/**
 * 記憶體偵測類
 * 
 * @author JamesX
 */
public class MemoryManager {
	private static final String TAG = "MemoryManager";
	private static boolean enable = true;

	/**
	 * 是否開啟log功能
	 * 
	 * @param _enable
	 */
	public static void enableLog(boolean _enable) {
		enable = _enable;
	}

	/**
	 * 偵測圖片快取記憶體已使用量
	 * 
	 * @return (int) 記憶體使用量，單位是 MB
	 */
	public static int getUsedMemory() {
		int usedMegs = (int) (Debug.getNativeHeapAllocatedSize() / 1048576L);
		if (enable) {
			Log.i(TAG, "usedMemory: " + usedMegs + "MB");
		}
		// Log.i("tag", "usedMemory: "+Debug.getNativeHeapAllocatedSize());
		// Log.i("tag", "useMemory2: "+Debug.getNativeHeapSize()/ 1048576L);
		// Log.i("tag", "useMemory3: "+Debug.getGlobalAllocSize()/ 1048576L);
		return usedMegs;
	}

	/**
	 * 偵測圖片快取記憶體已使用量
	 * 
	 * @param tag (String) 可自己加入 tag 方便搜尋
	 * @return (int) 記憶體使用量，單位是 MB
	 */
	public static int getUsedMemory(String tag) {
		int usedMegs = (int) (Debug.getNativeHeapAllocatedSize() / 1048576L);
		if (enable) {
			Log.i(TAG, tag + " usedMemory: " + usedMegs + "MB");
		}
		return usedMegs;
	}

	/**
	 * 取得圖片快取記憶體總量 通常總量裡還會有一小部份被Native占走 所以總量不是參考值
	 * 
	 * @return (int) 記憶體總量，單位是 MB
	 */
	public static int getMemoryAmounts() {

		int AllMegs = (int) (Runtime.getRuntime().maxMemory() / 1048576L);
		if (enable) {
			Log.i(TAG, "MemoryAmounts: " + AllMegs);
		}
		return AllMegs;
	}

	/**
	 * 刪除進程 讓程式finish時不保留在Android進程中 好還原記憶體供下次使用
	 */
	public static void killProcess() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
