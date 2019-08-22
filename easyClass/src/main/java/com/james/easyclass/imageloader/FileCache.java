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

package com.james.easyclass.imageloader;

import java.io.File;

import android.content.Context;

public class FileCache {

	private File cacheDir;

	public FileCache(Context context) {
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			try {
				cacheDir = new File(context.getExternalCacheDir().getAbsolutePath(), "/ImageLoader");
			} catch (Exception e) {
				cacheDir = context.getCacheDir();
			}
		}
		else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
	}

	public File getFile(String url) {
		// I identify images by hashcode. Not a perfect solution, good for the demo.
		String filename = String.valueOf(url.hashCode());
		// Another possible solution (thanks to grantland)
		// String filename = URLEncoder.encode(url);
		File f = new File(cacheDir, filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;

		for (File f : files) {
			f.delete();
		}
	}

	public void remove(String url) {
		if (url == null)
			return;
		//
		String filename = String.valueOf(url.hashCode());
		//
		File f = new File(cacheDir, filename);
		f.delete();
	}

}
