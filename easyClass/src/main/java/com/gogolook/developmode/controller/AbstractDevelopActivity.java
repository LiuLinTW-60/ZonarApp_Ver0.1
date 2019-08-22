
package com.gogolook.developmode.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class AbstractDevelopActivity extends Activity {

	private DevelopLayout developLayout;

	private HashMap<String, View> mHashMap = new HashMap<String, View>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setLayout();

		setPreferences();

		setListener();
	}

	private void setLayout() {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		developLayout = new DevelopLayout(this);
		setContentView(developLayout);
	}

	public void setActionBarBackground(int color) {
		developLayout.topLayout.setBackgroundColor(color);
	}

	public void setTitle(String title) {
		developLayout.titleText.setText(Html.fromHtml("<b>" + title + "</b>"));
	}

	public void setSubTitle(String subTitle) {
		developLayout.subTitleText.setText(Html.fromHtml(subTitle));
		if (TextUtils.isEmpty(subTitle)) {
			developLayout.subTitleText.setVisibility(View.GONE);
		} else {
			developLayout.subTitleText.setVisibility(View.VISIBLE);
		}
	}

	public Button getActionBarButton() {
		return developLayout.nextButton;
	}

	public void addPreferenceTitle(String title) {
		if (developLayout.itemLayout.getChildCount() != 0) {
			View offset = new View(this);
			offset.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 40));
			developLayout.itemLayout.addView(offset);
		}

		TextView preferenceTitleText = new TextView(this);
		preferenceTitleText.setText(Html.fromHtml("<b>" + title + "</b>"));
		preferenceTitleText.setTextColor(0xff818181);
		preferenceTitleText.setTextSize(resizeFontSize(16));
		preferenceTitleText.setPadding(5, 5, 5, 5);
		developLayout.itemLayout.addView(preferenceTitleText);

		View line = new View(this);
		line.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 3));
		line.setBackgroundColor(0xff444444);
		developLayout.itemLayout.addView(line);
	}

	public void addPreferenceButton(String preference_key, String title) {
		Object value = PreferenceManager.getDefaultSharedPreferences(this)
				.getAll().get(preference_key);
		addPreferenceButton(preference_key, title,
				value == null ? "" : value.toString());
	}

	public void addPreferenceButton(String preference_key, String title, String title2) {
		RelativeLayout preferenceLayout = new RelativeLayout(this);
		preferenceLayout.setPadding(5, 22, 5, 22);
		developLayout.itemLayout.addView(preferenceLayout);

		TextView preferenceText = new TextView(this);
		preferenceText.setId(0x1001);
		preferenceText.setText(Html.fromHtml(title));
		preferenceText.setTextColor(0xffdfdfdf);
		preferenceText.setTextSize(resizeFontSize(20));
		preferenceLayout.addView(preferenceText);
		mHashMap.put(new StringBuilder(preference_key).append("_1").toString(), preferenceText);

		TextView preferenceText2 = new TextView(this);
		preferenceText2.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) preferenceText2.getLayoutParams())
				.addRule(RelativeLayout.BELOW, preferenceText.getId());
		preferenceText2.setTextColor(0xffc1c1c1);
		preferenceText2.setTextSize(resizeFontSize(16));
		preferenceLayout.addView(preferenceText2);
		if (TextUtils.isEmpty(title2)) {
			preferenceText2.setVisibility(View.GONE);
			preferenceText2.setText(null);
		} else {
			preferenceText2.setVisibility(View.VISIBLE);
			preferenceText2.setText(Html.fromHtml(title2));
		}
		mHashMap.put(new StringBuilder(preference_key).append("_2").toString(), preferenceText2);

		View line = new View(this);
		line.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		line.setBackgroundColor(0xff444444);
		developLayout.itemLayout.addView(line);

		preferenceLayout.setOnClickListener(new OnPreferenceButtonClickListener(this,
				OnPreferenceButtonClickListener.PREFERENCE_BUTTON,
				preference_key, preferenceText2));
	}

	public void setPreferenceButton(String preference_key, String title, String title2) {
		TextView preferenceText = (TextView) mHashMap.get(new StringBuilder(preference_key).append("_1").toString());
		TextView preferenceText2 = (TextView) mHashMap.get(new StringBuilder(preference_key).append("_2").toString());

		if (preferenceText != null) {
			preferenceText.setText(Html.fromHtml(title));
		}

		if (preferenceText2 != null) {
			if (TextUtils.isEmpty(title2)) {
				preferenceText2.setText(null);
				preferenceText2.setVisibility(View.GONE);

			}
			else {
				preferenceText2.setText(Html.fromHtml(title2));
				preferenceText2.setVisibility(View.VISIBLE);
			}
		}

	}

	public void addPreferenceCheckButton(String preference_key, String title) {
		addPreferenceCheckButton(preference_key, title, null, false);
	}

	public void addPreferenceCheckButton(String preference_key, String title, String title2) {
		addPreferenceCheckButton(preference_key, title, title2, false);
	}

	public void addPreferenceCheckButton(String preference_key, String title, boolean defaultValue) {
		addPreferenceCheckButton(preference_key, title, null, defaultValue);
	}

	public void addPreferenceCheckButton(String preference_key, String title, String title2, boolean defaultValue) {
		RelativeLayout checkLayout = new RelativeLayout(this);
		checkLayout.setPadding(5, 22, 5, 22);
		developLayout.itemLayout.addView(checkLayout);

		TextView preferenceText = new TextView(this);
		preferenceText.setId(0x1001);
		preferenceText.setText(Html.fromHtml(title));
		preferenceText.setTextColor(0xffdfdfdf);
		preferenceText.setTextSize(resizeFontSize(20));
		checkLayout.addView(preferenceText);
		mHashMap.put(new StringBuilder(preference_key).append("_1").toString(), preferenceText);

		TextView preferenceText2 = new TextView(this);
		preferenceText2.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) preferenceText2.getLayoutParams())
				.addRule(RelativeLayout.BELOW, preferenceText.getId());
		preferenceText2.setTextColor(0xffc1c1c1);
		preferenceText2.setTextSize(resizeFontSize(16));
		preferenceText2.setTag(new StringBuilder(preference_key).append("_2").toString());
		checkLayout.addView(preferenceText2);
		if (TextUtils.isEmpty(title2)) {
			preferenceText2.setVisibility(View.GONE);
			preferenceText2.setText(null);
		} else {
			preferenceText2.setVisibility(View.VISIBLE);
			preferenceText2.setText(Html.fromHtml(title2));
		}
		mHashMap.put(new StringBuilder(preference_key).append("_2").toString(), preferenceText2);

		CheckBox preferenceCheckBox = new CheckBox(this);
		preferenceCheckBox.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) preferenceCheckBox.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		((RelativeLayout.LayoutParams) preferenceCheckBox.getLayoutParams()).addRule(RelativeLayout.CENTER_VERTICAL);
		preferenceCheckBox.setClickable(false);
		preferenceCheckBox.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(preference_key, defaultValue));
		checkLayout.addView(preferenceCheckBox);
		mHashMap.put(new StringBuilder(preference_key).append("_check").toString(), preferenceCheckBox);

		View line = new View(this);
		line.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		line.setBackgroundColor(0xff444444);
		developLayout.itemLayout.addView(line);

		checkLayout.setOnClickListener(new OnPreferenceButtonClickListener(
				this, OnPreferenceButtonClickListener.PREFERENCE_CHECKBOX,
				preference_key, preferenceText2));
	}

	public void setPreferenceCheckButton(String preference_key, String title, String title2, boolean defaultValue) {
		TextView preferenceText = (TextView) mHashMap.get(new StringBuilder(preference_key).append("_1").toString());
		TextView preferenceText2 = (TextView) mHashMap.get(new StringBuilder(preference_key).append("_2").toString());
		CheckBox preferenceCheckBox = (CheckBox) mHashMap.get(new StringBuilder(preference_key).append("_check").toString());

		if (preferenceText != null) {
			preferenceText.setText(Html.fromHtml(title));
		}

		if (preferenceText2 != null) {
			if (TextUtils.isEmpty(title2)) {
				preferenceText2.setText(null);
				preferenceText2.setVisibility(View.GONE);
			}
			else {
				preferenceText2.setText(Html.fromHtml(title2));
				preferenceText2.setVisibility(View.VISIBLE);
			}
		}

		if (preferenceCheckBox != null) {
			preferenceCheckBox.setChecked(defaultValue);
		}
	}

	public void addPreferenceList(String preference_key, String title,
			String[] selects) {
		addPreferenceList(preference_key, title, null, selects);
	}

	public void addPreferenceList(String preference_key, String title,
			String title2, String[] selects) {
		ArrayList<Pair<String, Object>> list = new ArrayList<Pair<String, Object>>();
		for (String select : selects) {
			list.add(new Pair<String, Object>(select, select));
		}

		addPreferenceList(preference_key, title, title2, list);
	}

	public void addPreferenceList(String preference_key, String title,
			Pair<String, Object>[] selects) {
		ArrayList<Pair<String, Object>> list = new ArrayList<Pair<String, Object>>(
				Arrays.asList(selects));
		addPreferenceList(preference_key, title, null, list);
	}

	public void addPreferenceList(String preference_key, String title,
			ArrayList<Pair<String, Object>> selects) {
		addPreferenceList(preference_key, title, null, selects);
	}

	public void addPreferenceList(String preference_key, String title,
			String title2, Pair<String, Object>[] selects) {
		ArrayList<Pair<String, Object>> list = new ArrayList<Pair<String, Object>>(
				Arrays.asList(selects));
		addPreferenceList(preference_key, title, title2, list);
	}

	public void addPreferenceList(String preference_key, String title,
			String title2, ArrayList<Pair<String, Object>> selects) {
		RelativeLayout preferenceLayout = new RelativeLayout(this);
		preferenceLayout.setPadding(5, 22, 5, 22);
		developLayout.itemLayout.addView(preferenceLayout);

		TextView preferenceText = new TextView(this);
		preferenceText.setId(0x1001);
		preferenceText.setText(Html.fromHtml(title));
		preferenceText.setTextColor(0xffdfdfdf);
		preferenceText.setTextSize(resizeFontSize(20));
		preferenceLayout.addView(preferenceText);

		Object value = PreferenceManager.getDefaultSharedPreferences(this)
				.getAll().get(preference_key);
		if (title2 == null && value != null) {
			for (Pair<String, Object> pairs : selects) {
				if (pairs.second.equals(value)) {
					title2 = pairs.first;
				}
			}
		}

		TextView preferenceText2 = new TextView(this);
		preferenceText2.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) preferenceText2.getLayoutParams())
				.addRule(RelativeLayout.BELOW, preferenceText.getId());
		preferenceText2.setTextColor(0xffc1c1c1);
		preferenceText2.setTextSize(resizeFontSize(16));
		preferenceLayout.addView(preferenceText2);
		if (TextUtils.isEmpty(title2)) {
			preferenceText2.setVisibility(View.GONE);
			preferenceText2.setText(null);
		} else {
			preferenceText2.setVisibility(View.VISIBLE);
			preferenceText2.setText(Html.fromHtml(title2));
		}

		View line = new View(this);
		line.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		line.setBackgroundColor(0xff444444);
		developLayout.itemLayout.addView(line);

		preferenceLayout
				.setOnClickListener(new OnPreferenceButtonClickListener(this,
						OnPreferenceButtonClickListener.PREFERENCE_LIST,
						preference_key, selects, preferenceText2));
	}

	private class OnPreferenceButtonClickListener implements
			View.OnClickListener {
		public static final int PREFERENCE_BUTTON = 1;
		public static final int PREFERENCE_CHECKBOX = 2;
		public static final int PREFERENCE_LIST = 3;

		private Context mContext;
		private int mPreferenceType;
		private String mPreferenceKey;
		private ArrayList<Pair<String, Object>> mSelects;
		private TextView mValueText;

		private Animation clickAnimation;

		private OnPreferenceButtonClickListener(Context context,
				int preference_type, String preference_key, TextView valueText) {
			init(context, preference_type, preference_key, valueText);
		}

		private OnPreferenceButtonClickListener(Context context,
				int preference_type, String preference_key,
				ArrayList<Pair<String, Object>> selects, TextView valueText) {
			init(context, preference_type, preference_key, valueText);

			mSelects = selects;
		}

		private void init(Context context, int preference_type,
				String preference_key, TextView valueText) {
			mContext = context;
			mPreferenceType = preference_type;
			mPreferenceKey = preference_key;
			mValueText = valueText;

			clickAnimation = new AlphaAnimation(1.0f, 0.3f);
			clickAnimation.setDuration(100);
		}

		@Override
		public void onClick(View v) {
			v.startAnimation(clickAnimation);

			if (mPreferenceType == PREFERENCE_BUTTON) {
				onPreferenceClick(mPreferenceKey, null);
			} else if (mPreferenceType == PREFERENCE_CHECKBOX) {
				handleCheckbox(v);
			} else if (mPreferenceType == PREFERENCE_LIST) {
				handleSelect(v);
			}
		}

		private void handleCheckbox(View v) {
			RelativeLayout layout = (RelativeLayout) v;
			CheckBox checkBox = null;
			for (int i = 0; i < layout.getChildCount(); i++) {
				if (layout.getChildAt(i) instanceof CheckBox) {
					checkBox = (CheckBox) layout.getChildAt(i);
				}
			}
			checkBox.setChecked(!checkBox.isChecked());
			putDebugPreference(mPreferenceKey, checkBox.isChecked());
			if (!onPreferenceClick(mPreferenceKey, checkBox.isChecked())) {
				removeDebugPreference(mPreferenceKey);
			}
		}

		private void handleSelect(View v) {
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(
					mContext);
			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					mContext, android.R.layout.select_dialog_singlechoice);
			for (int i = 0; i < mSelects.size(); i++) {
				arrayAdapter.add(mSelects.get(i).first);
			}

			builderSingle.setAdapter(arrayAdapter,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Object value = mSelects.get(which).second;
							//
							String name = mSelects.get(which).first;
							mValueText.setText(name);
							mValueText.setVisibility(View.VISIBLE);
							putDebugPreference(mPreferenceKey, value);
							if (!onPreferenceClick(mPreferenceKey, value)) {
								removeDebugPreference(mPreferenceKey);
								mValueText.setText(null);
								mValueText.setVisibility(View.GONE);
							}
						}
					});
			builderSingle.show();
		}
	}

	private void setListener() {
		developLayout.titleText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onHomeClick();
			}
		});
		developLayout.nextButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onNextClick();
			}
		});
	}

	protected void onHomeClick() {
		finish();
	}

	protected void onNextClick() {

	}

	public void putDebugPreference(String key, Object value) {
		if (value instanceof Boolean) {
			putBooleanPreference(key, (Boolean) value);
		} else if (value instanceof Integer) {
			putIntPreference(key, (Integer) value);
		} else if (value instanceof Float) {
			putFloatPreference(key, (Float) value);
		} else if (value instanceof String) {
			putStringPreference(key, (String) value);
		} else if (value instanceof Long) {
			putLongPreference(key, (Long) value);
		}
	}

	public void putBooleanPreference(String key, boolean value) {
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putBoolean(key, value).commit();
	}

	public void putStringPreference(String key, String value) {
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putString(key, value).commit();
	}

	public void putIntPreference(String key, int value) {
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putInt(key, value).commit();
	}

	public void putFloatPreference(String key, float value) {
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putFloat(key, value).commit();
	}

	public void putLongPreference(String key, long value) {
		PreferenceManager.getDefaultSharedPreferences(this).edit()
				.putLong(key, value).commit();
	}

	public void removeDebugPreference(String key) {
		PreferenceManager.getDefaultSharedPreferences(this).edit().remove(key)
				.commit();
	}

	public float resizeFontSize(float size) {
		return size;
	}

	/**
	 * User
	 */
	protected abstract void setPreferences();

	/**
	 * A callback function to notify user preference clicked. <br>
	 * Return true, if save the new preference value, <br>
	 * otherwise, returning false will abandon the new value.
	 * 
	 * @param preference_key (String) key for saving preference
	 * @param newValue (Object) new selected value.
	 * @return true, if save the new preference value. false will ignore the new value.
	 */
	protected abstract boolean onPreferenceClick(String preference_key,
			Object newValue);
}
