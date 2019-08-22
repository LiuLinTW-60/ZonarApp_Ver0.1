
package com.james.easyclass.model;

import java.util.Locale;

public class Res {

	public static class string {
		public static String dialog_confirm_button = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "確認" : "Confirm");
		public static String dialog_skip_button = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "略過" : "Skip");
		public static String dialog_cancel_button = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "取消" : "Cancel");
		public static String dialog_internet_title = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "提示" : "Alert");
		public static String dialog_internet_message = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "須開啟網路設備" : "WIFI/3G must be turned on");
		public static String dialog_exit = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "確定離開應用程式?" : "Exit App?");
		public static String dialog_gps_title = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "提示" : "Alert");
		public static String dialog_gps_message = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "建議您\n★室內開啟Wi-Fi輔助定位系統\n★室外開啟GPS定位系統" : "Suggestion:\n★Turn on WIFI indoor\n★Turn on GPS outdoor");
		public static String dialog_clear_cache = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "是否清除暫存資料" : "Clear Cache?");

		public static String toast_gps_fail = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "定位失敗，請重試" : "Locate Position Fail.\nPlease retry later.");
		public static String toast_network_fail = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "網路連線失敗，請重試" : "Connection Fail.\nPlease retry later.");
		public static String toast_network_not_found = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "無偵測到網路" : "No Network Detected.");
		public static String toast_network_timeout = ((Locale.getDefault().equals(Locale.TAIWAN) || Locale.getDefault().equals(Locale.CHINA)) ? "網路連線超時" : "Connection Timeout.");
	}
}
