
package com.gogolook.developmode;

import java.util.ArrayList;
import java.util.List;

public class DevConfig {
	public static boolean IS_RC = false;

	//
	public static final String DEVELOP_PREF_KEY = "DevelopMode";
	// 設定國別語系
	public static final String COUNTRY_PREF_KEY = "develop_country_preference";
	public static final String LANGUAGE_PREF_KEY = "develop_language_preference";
	public static final String ISFULLSCREEN = "develop_isfullscreen_preference";
	//
	public static final String DB_PREF_KEY = "develop_db_preference";
	public static final String RECORD_CONTENT_PROVIDER_KEY = "develop_calllog";
	public static final String SHOW_PREF_KEY = "develop_show_preference";

	public static String[] RECIEVERS = new String[] {
		"jetthsieh@gogolook.com", "ericliu@gogolook.com", "momokotao@gogolook.com", "terryhwang@gogolook.com", "pochihuang@gogolook.com"
	};

	public static List<String> getCountries() {
		List<String> list = new ArrayList<String>();

		list.add("default");
		list.add("tw");
		list.add("hk");
		list.add("sa");
		list.add("jp");
		list.add("kr");
		list.add("br");
		list.add("id");
		list.add("in");
		list.add("us");
		list.add("ru");
		list.add("kw");
		list.add("eg");
		list.add("sg");
		list.add("my");
		list.add("th");
		list.add("ae");
		list.add("jo");
		list.add("lb");
		list.add("bh");
		list.add("om");
		list.add("qa");
		list.add("vn");

		return list;
	}

	public static List<String> getLanguages() {
		List<String> list = new ArrayList<String>();

		list.add("default");
		list.add("en-rUS");
		list.add("zh-rCN");
		list.add("zh-rTW");
		list.add("zh-rHK");
		list.add("ja-rJP");
		list.add("ar");
		list.add("ko-rKR");
		list.add("ru");
		list.add("th");
		list.add("in");
		list.add("vi");
		list.add("fr");
		list.add("id");
		list.add("it");
		list.add("ms");
		list.add("de");
		list.add("es");
		list.add("tr");
		list.add("pt-rBR");
		list.add("pt-rPT");
		list.add("sr");

		return list;
	}

	public static List<String> getSMSList() {
		List<String> list = new ArrayList<String>();
		list.add("content://call_log/calls");
		list.add("content://sms");
		list.add("content://sms/inbox");
		list.add("content://sms/sent");
		list.add("content://sms/draft");
		list.add("content://sms/outbox");
		list.add("content://sms/conversations");
		list.add("content://mms");
		list.add("content://mms/inbox");
		list.add("content://mms/outbox");
		list.add("content://mms/part");
		list.add("content://mms-sms/conversations");
		list.add("content://mms-sms/draft");
		list.add("content://mms-sms/locked");
		list.add("content://mms-sms/search");

		return list;
	}
}
