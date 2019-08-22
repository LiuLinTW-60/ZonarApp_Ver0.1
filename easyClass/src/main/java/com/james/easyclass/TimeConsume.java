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

import android.content.Context;
import android.util.Log;

/**
 * 用來監測時間花費
 * @author JamesX
 * @since 2012/05/17
 */
public class TimeConsume {
	private static final String TAG = "TimeConsume";
	public static boolean visual = true;
	public static long start_time=0L;
	public static long end_time=0L;
	public static String event_name="";
	
	
	/**開始時間*/
	public static void start(String evenet){
		start_time = System.currentTimeMillis();
		event_name= evenet;
	}
	
	/**結束時間*/
	public static void stop(Context context){
		end_time = System.currentTimeMillis();

		
		if(visual){
//			Log.i("tag", "start: "+start_time);
//			Log.i("tag", "end: "+end_time);
			Log.d(TAG, event_name+" "+(Double)((end_time-start_time)/1000D)+"s");
//			Toast.makeText(context, event_name+" "+(Double)((end_time-start_time)/1000D)+"s", Toast.LENGTH_SHORT).show();
		}
		start_time=0L;
		end_time=0L;
		event_name="";
	}
}

