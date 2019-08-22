
package com.gogolook.system;

import java.io.File;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class SystemUtils {

	/**
	 * 取得版本名稱
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	/**
	 * 取得版本號碼
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			return 0;
		}
	}

	public static void clearApplicationData(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File appDir = new File(context.getExternalCacheDir().getParent());
			if (appDir.exists()) {
				String[] children = appDir.list();
				for (String s : children) {
					if (!s.equals("lib")) {
						deleteDir(new File(appDir, s));
					}
				}
			}
		}
		//
		File appDir = new File(context.getCacheDir().getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));
				}
			}
		}

		// clear databases in sdcard
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			String filepath = null;
			try {
				if (android.os.Build.VERSION.SDK_INT > 7) {
					filepath = context.getExternalFilesDir(null).getPath() + "/db/";
				} else {
					filepath = Environment.getExternalStorageDirectory().getPath() + "/" + context.getApplicationInfo().loadLabel(context.getPackageManager()) + "/db/";
				}
			} catch (Exception ex) {
				filepath = Environment.getExternalStorageDirectory().getPath() + "/" + context.getApplicationInfo().loadLabel(context.getPackageManager()) + "/db/";
			}

			if (filepath != null) {
				File cacheDir = new File(filepath);
				if (cacheDir.exists()) {
					String[] children = cacheDir.list();
					for (String s : children) {
						deleteDir(new File(cacheDir, s));
					}
				}
			}
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
}
