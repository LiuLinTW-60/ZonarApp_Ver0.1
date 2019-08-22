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

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
 * @author JamesX
 *
 */
public class MonitorUtils {
	public static final int PIC_320 = 320;
	public static final int PIC_480 = 480;
	public static final int PIC_640 = 640;
	public static final int PIC_750 = 750;
	
	public static void keepScreenOn(Activity activity){
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	/**
	 * 要求全畫面
	 * @param activity (Activity)
	 */
	public static void requestFullScreen(Activity activity){
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	
	/**
	 * 要求無title
	 * @param activity (Activity)
	 */
	public static void requestNoTitle(Activity activity){
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 取得螢幕寬
	 * @param context (Context)
	 * @return (int) 螢幕寬
	 */
	public static int getMonitorWidth(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/**
	 * 取得螢幕高
	 * @param context (Context)
	 * @return (int) 螢幕高
	 */
	public static int getMonitorHeight(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getMonitorDisplayMetrics(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	
	/**
	 * 取得螢幕的對角線長度(inch, 吋)
	 * @param context
	 * @return (double) inch
	 */
	public static double getMonitorInch(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		double inch = Math.sqrt(Math.pow(dm.widthPixels/dm.xdpi, 2)+Math.pow(dm.heightPixels/dm.ydpi, 2));
		return inch;
	}
	
	/**
	 * 依照螢幕大小重新規畫物件的比例
	 * @param context (Context)
	 * @param length (int) 物件原始寬度(高度)
	 * @param picLength (int) art 出圖大小(PIC_320, PIC_480, PIC_640, PIC_750)
	 * @return
	 */
	public static int resizeByMonitor(Context context, int length, int picLength){
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels*length/picLength;
	}
	
	/**
	 * 依照螢幕大小重新規畫物件的比例
	 * @param context (Context)
	 * @param length (int) 物件原始寬度(高度)
	 * @return
	 */
	public static int resizeByMonitor(Context context, int length){
		return resizeByMonitor(context, length, PIC_750);
	}
	
	/**
	 *  dp轉px
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 *  sp轉px
	 * @param context
	 * @param sp
	 * @return
	 */
	public static float sp2px(Context context, Float sp) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return scaledDensity * sp;
	}
}
