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

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author JamesX
 * @since 2012/10/15
 */
public class LogUtils {

    private static Context sContext;
    private static TextView debugTextView;
    private static boolean sIsShowing = false;

    private static ArrayList<String> logs = new ArrayList<String>();

    private static boolean show = true;

    public static void enable() {
        show = true;
    }

    public static void disable() {
        show = false;
    }

    public static void init(Context context) {
        sContext = context;
    }

    public static void v(String tag, String msg) {
        logs.add(tag + " -> " + msg);

        if (logs.size() > 15) {
            logs.remove(0);
        }
        if (show) {
            Log.v(tag, msg);

            if (sContext != null) {
                updateLogText();
            }
        }
    }

    public static void w(String tag, String msg) {
        if (show) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (show) {
            Log.e(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (show) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (show) {
            Log.d(tag, msg);
        }
    }

    public static boolean isShowing() {
        return sIsShowing;
    }

    public static void showDebugView(boolean flag) {
        if (sContext == null) {
            return;
        }
        sIsShowing = flag;

        if (!flag) {
            if (debugTextView != null) {
                WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
                windowManager.removeView(debugTextView);
                debugTextView = null;
            }
            return;
        }

        try {
            WindowManager windowManager = (WindowManager) sContext.getSystemService(Context.WINDOW_SERVICE);
            if (debugTextView == null) {
                debugTextView = new TextView(sContext);
                debugTextView.setTextColor(0xff00ff00);
                debugTextView.setBackgroundColor(0x99333333);
                debugTextView.setTextSize(10);
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                layoutParams.format = PixelFormat.RGBA_8888;

                /*
                 * 下面的flags屬性的效果形同"鎖定"。懸浮窗不可觸摸，不接受任何事件,同時不影響後面的事件響應。 wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
                 */
                layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
                // set relative position
                layoutParams.gravity = Gravity.BOTTOM;
                //
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                windowManager.addView(debugTextView, layoutParams);
            }

            updateLogText();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateLogText() {
        if (debugTextView == null || !debugTextView.isShown()) {
            return;
        }

        try {
            String log = "";
            for (String string : logs) {
                if (TextUtils.isEmpty(log)) {
                    log = string;
                } else {
                    log = log + "\n" + string;
                }
            }

            debugTextView.setText(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
