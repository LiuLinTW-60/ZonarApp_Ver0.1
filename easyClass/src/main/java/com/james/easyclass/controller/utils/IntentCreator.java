package com.james.easyclass.controller.utils;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class IntentCreator {
	private static String TAG = IntentCreator.class.getSimpleName();
	private static HashMap<String, Class<?>[]> intentMap;
	
	/**
	 * <br> Please add this line in onCreate().
	 * <br> Make sure classes contains all Activity/Service, in order to trace app flow easily.
	 * @param packageContext (Context)
	 * @param classes (Class<?>...) ex: Activity1.class, Activity2.class
	 */
	public static void index(Context packageContext, Class<?>... classes){
		Log.v(TAG, "At "+packageContext.getClass().getSimpleName()+" now.");
		
		if(intentMap == null) intentMap = new HashMap<String, Class<?>[]>();
		
		intentMap.put(packageContext.getClass().getSimpleName(), classes);
	}
	
	/**
	 * <br> use this function as
	 * <pre>
	 * new Intent(packageContext, nextClass);
	 * </pre>
	 * @param packageContext (Context)
	 * @param nextClass (Class<?>) Next Activity/Service to start
	 * @return
	 */
	public static Intent newIntent(Context packageContext, Class<?> nextClass){
		checkMap(packageContext.getClass().getSimpleName(), nextClass);
		
		Intent intent = new Intent(packageContext, nextClass);
		return intent;
	}
	
	/**
	 * <br> use this function as
	 * <pre>
	 * new Intent(action, uri, packageContext, nextClass);
	 * </pre>
	 * @param action (String)
	 * @param uri (Uri)
	 * @param packageContext (Context)
	 * @param nextClass (Class<?>) Next Activity/Service to start
	 * @return
	 */
	public static Intent newIntent(String action, Uri uri, Context packageContext, Class<?> nextClass){
		checkMap(packageContext.getClass().getSimpleName(), nextClass);
		
		Intent intent = new Intent(action, uri, packageContext, nextClass);
		return intent;
	}
	
	private static void checkMap(String nowClassName, Class<?> nextClass){
		if(intentMap == null) intentMap = new HashMap<String, Class<?>[]>();
		Class<?>[] classes = intentMap.get(nowClassName);
		if(classes != null){
			boolean contain = false;
			for(Class<?> c:classes){
				if(c.equals(nextClass)){
					contain = true;
					break;
				}
			}
			if(!contain){
				// 
				Log.w(TAG, nextClass.getSimpleName()+" is not found in map of intent, please check IntentCreator.put(this, new Class<?>[]{...}) if "+nextClass.getSimpleName()+" is missing.");
			}
		}
		else{
			// 
			Log.e(TAG, "Please add this line in onCreate() method: IntentCreator.put(this, new Class<?>[]{...})");
		}
		Log.v(TAG, "startActivity from "+nowClassName+" to "+nextClass.getSimpleName());
	}
}
