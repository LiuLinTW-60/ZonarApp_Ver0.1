
package com.gogolook.developmode.controller;

import android.os.Bundle;

import com.gogolook.developmode.BasicDevelopMode;
import com.gogolook.developmode.DevConfig;

/**
 * Add com.gogolook.developmode.controller.DemoDevelopActivity in Manifest. <br>
 * implement onPreferenceClick() to set event listener.
 * 
 * @author JamesX
 */
public class DemoDevelopActivity extends AbstractDevelopActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		BasicDevelopMode.getInstance().init(this);

		setTitle("Develop Activity");
		setSubTitle("Test");
	}

	@Override
	protected void setPreferences() {
		//
		addPreferenceTitle("COUNTRY AND LANGUAGE");
		addPreferenceButton(DevConfig.COUNTRY_PREF_KEY, "Country");
		addPreferenceButton(DevConfig.LANGUAGE_PREF_KEY, "Language");
		addPreferenceTitle("SYSTEM");
		addPreferenceButton(DevConfig.DB_PREF_KEY, "Record Content Provider");
		addPreferenceButton(DevConfig.RECORD_CONTENT_PROVIDER_KEY, "Lookup Database");
		addPreferenceButton(DevConfig.SHOW_PREF_KEY, "Show Preferences");
	}

	/**
	 * Implement this function to set preference change listening.
	 */
	@Override
	protected boolean onPreferenceClick(String preference_key, Object value) {
		if (preference_key.equalsIgnoreCase(DevConfig.COUNTRY_PREF_KEY)) {
			BasicDevelopMode.getInstance().selectCountry(this);
		}
		else if (preference_key.equalsIgnoreCase(DevConfig.LANGUAGE_PREF_KEY)) {
			BasicDevelopMode.getInstance().selectLanguage(this);
		}
		else if (preference_key.equalsIgnoreCase(DevConfig.DB_PREF_KEY)) {
			BasicDevelopMode.getInstance().recordContentProvider(this);
		}

		return true;
	}

}
