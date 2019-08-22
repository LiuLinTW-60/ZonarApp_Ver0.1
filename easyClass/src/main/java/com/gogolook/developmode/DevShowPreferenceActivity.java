
package com.gogolook.developmode;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Use
 * 
 * <pre>
 * ShowPreferenceActivity.SP_NAME
 * </pre>
 * 
 * to set name of shared preference, or it would crash.
 * 
 * @author JamesX
 */
public class DevShowPreferenceActivity extends Activity {

	public static final String SP_NAME = "shared_preference_name";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ScrollView sv = new ScrollView(this);
		TextView tv = new TextView(this);
		sv.addView(tv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		Map<String, ?> keys = getSharedPreferences(getSharedPreferenceName(), 0).getAll();
		String log = null;
		for (Map.Entry<String, ?> entry : keys.entrySet()) {
			if (log == null) {
				log = entry.getKey() + " : " + entry.getValue() + "\n";
			}
			else {
				log += entry.getKey() + " : " + entry.getValue() + "\n";
			}
		}
		tv.setText(log);
		this.setContentView(sv);

	}

	private String getSharedPreferenceName() {
		return getIntent().getStringExtra(SP_NAME);
	}
}
