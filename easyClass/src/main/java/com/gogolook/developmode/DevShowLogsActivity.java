
package com.gogolook.developmode;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class DevShowLogsActivity extends Activity {
	private String mString;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		final ScrollView sv = new ScrollView(this);
		TextView tv = new TextView(this);
		if (getIntent().getBooleanExtra("logcat", false)) {
			String mailstring = "logcat";
			Process logcat;
			final StringBuilder log = new StringBuilder();
			try {
				logcat = Runtime.getRuntime().exec(new String[] {
					"logcat", "-v", "time", "-d"
				});
				BufferedReader br = new BufferedReader(new InputStreamReader(logcat.getInputStream()), 4 * 1024);
				String line;
				String separator = System.getProperty("line.separator");
				while ((line = br.readLine()) != null) {
					log.append(line);
					log.append(separator);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			mailstring = log.toString();
			sv.addView(tv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			sv.setScrollbarFadingEnabled(false);
			sv.post(new Runnable() {

				@Override
				public void run() {
					sv.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			mString = mailstring;
			tv.setText(mString);

		} else {
			try {
				tv.setTextIsSelectable(true);
			} catch (Exception e) {

			}
			ArrayBlockingQueue<String> q = BasicRageShake.getInstance(this, null).getAPILogQueue();

			String log = "";
			for (Iterator<String> iter = q.iterator(); iter.hasNext();) {
				log += iter.next() + "<br>";
			}
			sv.addView(tv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			sv.setScrollbarFadingEnabled(false);
			sv.post(new Runnable() {

				@Override
				public void run() {
					sv.fullScroll(ScrollView.FOCUS_DOWN);
				}
			});
			mString = log;
			mString = mString.replace(" ", "&nbsp;").replace("#", "<font color=\"green\">#</font>").replace("\n", "<br>");
			tv.setText(Html.fromHtml(mString));

		}

		tv.setTextSize(11);
		this.setContentView(sv);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add("Share");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getTitle().equals("Share")) {
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, mString);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
}
