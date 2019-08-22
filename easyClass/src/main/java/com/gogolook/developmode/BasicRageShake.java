
package com.gogolook.developmode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * To use rage shake debug mode, please add permission
 * 
 * <pre>
 * android.permission.SYSTEM_ALERT_WINDOW
 * </pre>
 * 
 * and add two activity in manifest
 * 
 * <pre>
 * com.gogolook.developmode.DevShowLogsActivity &amp; com.gogolook.developmode.DevShowPreferenceActivity
 * </pre>
 * 
 * @author JamesX
 */
public class BasicRageShake {
	//
	private MySensorEventListener mMySensorEventListener;
	private static BasicRageShake instance;
	//
	protected Context mContext;
	private BasicDevelopMode mBasicDevelopMode;
	private String mTitle;
	private ArrayList<Pair<String, String>> mButtons = new ArrayList<Pair<String, String>>();
	private SharedPreferences sharedPreference;
	public final static String IS_RAGESHAKE_ENABLE_KEY = "develop_is_rage_shake_enable";
	private Stack<Context> mContextStack;
	private WeakReference<View> mView;
	private String mMainPath;
	protected String mScreenshotPath;
	protected String mDialogScreenshotPath;
	protected String mLogcatPath;
	private final static int MAX_LOG = 16;
	private static ArrayBlockingQueue<String> mLogQueue = new ArrayBlockingQueue<String>(MAX_LOG, false);

	public BasicRageShake(Context context, BasicDevelopMode basicDevelopMode) {
		mContext = context;
		mContextStack = new Stack<Context>();
		mLogQueue.clear();
		if (basicDevelopMode == null) {
			mBasicDevelopMode = BasicDevelopMode.getInstance();
		}
		else {
			mBasicDevelopMode = basicDevelopMode;
		}

		mMainPath = new File(context.getCacheDir().getAbsolutePath(), "/rageshake").getAbsolutePath();
		File f = new File(mMainPath);
		if (!f.exists()) {
			f.mkdir();
		}

		String[] children = f.list();
		if (children != null) {
			for (String s : children) {
				(new File(f, s)).delete();
			}
		}

		sharedPreference = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public synchronized void putAPILog(String s) {
		if (mLogQueue.size() == MAX_LOG) {
			mLogQueue.poll();
		}
		s = s.replace("Get", "#Get").replace("GET", "#Get").replace("get", "#Get");
		s = s.replace("Put", "#Put").replace("PUT", "#Put").replace("put", "#Put");
		s = s.replace("Post", "#Post").replace("POST", "#Post").replace("post", "#Post");
		s = s.replace("Delete", "#Delete").replace("DELETE", "#Delete").replace("delete", "#Delete");
		mLogQueue.add(s);
	}

	public ArrayBlockingQueue<String> getAPILogQueue() {
		return mLogQueue;
	}

	public static BasicRageShake getInstance(Context context, BasicDevelopMode basicDevelopMode) {
		if (instance == null) {
			instance = new BasicRageShake(context, basicDevelopMode);
		}
		if (!instance.mContext.equals(context)) {
			instance.mContext = context;
		}

		return instance;

	}

	public Context getCurrentContext() {
		return mContextStack.peek();
	}

	public boolean isRageShakeEnable() {
		if (DevConfig.IS_RC) {
			return false;
		}
		return sharedPreference.getBoolean(IS_RAGESHAKE_ENABLE_KEY, true);
	}

	public void enableRageShake() {
		sharedPreference.edit().putBoolean(IS_RAGESHAKE_ENABLE_KEY, true).commit();
		try {
			if (mMySensorEventListener == null) {
				mMySensorEventListener = new MySensorEventListener();

				SensorManager sensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
				sensorMgr.registerListener(mMySensorEventListener,
						sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL);
			}
		} catch (Exception e) {

		}
	}

	public void disableRageShake() {
		sharedPreference.edit().putBoolean(IS_RAGESHAKE_ENABLE_KEY, false).commit();
		try {
			SensorManager sensorMgr = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
			sensorMgr.unregisterListener(mMySensorEventListener);
			mMySensorEventListener = null;
		} catch (Exception e) {

		}
	}

	class MySensorEventListener implements SensorEventListener {
		long lastUpdate = 0;
		float x;
		float y;
		float z;
		float last_x;
		float last_y;
		float last_z;

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (!isRageShakeEnable()) {
				return;
			}

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				long curTime = System.currentTimeMillis();
				// only allow one update every 100ms.
				if ((curTime - lastUpdate) > 60) {
					long diffTime = (curTime - lastUpdate);
					lastUpdate = curTime;

					x = event.values[SensorManager.DATA_X];
					y = event.values[SensorManager.DATA_Y];
					z = event.values[SensorManager.DATA_Z];

					float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
					if (speed > 900) {
						popup();
					}
					last_x = x;
					last_y = y;
					last_z = z;
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

	};

	boolean isShow = false;

	private void logcat() {
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
		File file;

		mLogcatPath = mMainPath + File.separator + "rageshake_log.txt";

		file = new File(mLogcatPath);

		if (file.exists()) {
			file.delete();
		}

		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FileOutputStream outputStream;

		try {
			outputStream = new FileOutputStream(file.getAbsolutePath());
			outputStream.write(mailstring.getBytes());
			outputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Activity getTopActivity() {
		for (int i = mContextStack.size() - 1; i >= 0; i--) {
			if (mContextStack.get(i) instanceof Activity) {
				return (Activity) mContextStack.get(i);
			}
		}
		return null;
	}

	private void screenshot() {
		Activity a = getTopActivity();

		if (a != null) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy_M_dd_HH_mm_ss");
			String now = formatter.format(+System.currentTimeMillis());
			mScreenshotPath = mMainPath + File.separator + "rageshake_screenshot_" + now + ".jpg";
			Bitmap bitmap = null;

			View view = a.getWindow().getDecorView().getRootView();
			view.setDrawingCacheEnabled(true);
			Bitmap drawingBitmap = view.getDrawingCache();
			if (drawingBitmap != null) {
				bitmap = Bitmap.createBitmap(drawingBitmap);
			}
			view.setDrawingCacheEnabled(false);
			if (bitmap != null) {
				OutputStream fout = null;
				File imageFile = new File(mScreenshotPath);
				if (imageFile.exists()) {
					imageFile.delete();
				}
				try {
					imageFile.createNewFile();
				} catch (IOException e1) {

				}
				try {
					fout = new FileOutputStream(imageFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
					fout.flush();
					fout.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			mScreenshotPath = null;
		}
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public void addMoreButtons(String button_key, String button_text) {
		mButtons.add(new Pair<String, String>(button_key, button_text));
	}

	public void onButtonClickListener(String button_key) {

	}

	private void popup() {
		if (!isShow) {
			screenshot();
			screenshotDialog();
			logcat();

			if (mContextStack.isEmpty()) {
				return;
			}

			final Context context = mContextStack.peek();

			final Dialog dialog = new Dialog(context);

			RageShakeView rageShakeView = new RageShakeView(context);
			TextView titleText = rageShakeView.titleText;
			CheckBox rageshakeCheckBox = rageShakeView.rageshakeCheckBox;
			TextView reportText = rageShakeView.reportText;
			TextView shareText = rageShakeView.shareText;
			TextView logcatText = rageShakeView.logcatText;
			TextView apiLogcatText = rageShakeView.apiLogcatText;
			TextView monitorNetworkText = rageShakeView.monitorNetworkText;

			for (final Pair<String, String> button : mButtons) {
				View view = rageShakeView.addMoreButton(button.first, button.second);
				view.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						onButtonClickListener(button.first);
						dialog.dismiss();
					}
				});
			}

			if (TextUtils.isEmpty(mTitle)) {
				String currentPage = mContextStack.peek().getClass().getName();
				String[] splits = currentPage.split("\\.");
				if (splits.length > 0) {
					currentPage = splits[splits.length - 1];
				}
				String title = "Current Page:\n" + currentPage + ".java";
				titleText.setText(title);
			}
			else {
				titleText.setText(Html.fromHtml(mTitle));
			}

			titleText.setSingleLine(false);

			rageshakeCheckBox.setChecked(isRageShakeEnable());
			rageshakeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						enableRageShake();
					}
					else {
						disableRageShake();
						Toast.makeText(context, "To enable RageSake, change setting in Debug Mode, please.", Toast.LENGTH_LONG).show();
					}
				}
			});

			reportText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					File file1 = null;
					File file2;
					File file3 = null;

					if (mScreenshotPath != null) {
						file1 = new File(mScreenshotPath);
					}
					file2 = new File(mLogcatPath);
					if (mDialogScreenshotPath != null) {
						file3 = new File(mDialogScreenshotPath);
					}

					Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
					emailIntent.setType("plain/text");
					emailIntent.putExtra(Intent.EXTRA_EMAIL, DevConfig.RECIEVERS);
					emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Rage Report][" + context.getApplicationInfo().loadLabel(context.getPackageManager()) + "][" + Build.MODEL + " - " + Build.MANUFACTURER + "]");

					ArrayList<Uri> uris = new ArrayList<Uri>();
					if (file1 != null) {
						uris.add(Uri.parse("file://" + file1.getAbsolutePath()));
					}
					uris.add(Uri.parse("file://" + file2.getAbsolutePath()));
					if (file3 != null) {
						uris.add(Uri.parse("file://" + file3.getAbsolutePath()));
					}

					emailIntent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, uris);
					emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(emailIntent);
					dialog.dismiss();

				}
			});

			if (mScreenshotPath != null) {
				shareText.setVisibility(View.VISIBLE);
				shareText.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent share = new Intent(Intent.ACTION_SEND);
						File file1 = new File(mScreenshotPath);
						share.setType("image/jpeg");
						share.putExtra(Intent.EXTRA_STREAM,
								Uri.parse("file://" + file1.getAbsolutePath()));
						Intent i = Intent.createChooser(share, "Share Screenshot");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);

						dialog.dismiss();
					}
				});
			} else {
				shareText.setVisibility(View.GONE);
			}

			if (mDialogScreenshotPath != null) {
				shareText.setVisibility(View.VISIBLE);
				shareText.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent share = new Intent(Intent.ACTION_SEND);
						File file3 = new File(mDialogScreenshotPath);

						share.setType("image/jpeg");

						share.putExtra(Intent.EXTRA_STREAM,
								Uri.parse("file://" + file3.getAbsolutePath()));
						Intent i = Intent.createChooser(share, "Share Screenshot");
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(i);

						dialog.dismiss();

					}
				});
			} else {
				shareText.setVisibility(View.GONE);
			}

			logcatText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(context, DevShowLogsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("logcat", true);
					context.startActivity(intent);
					dialog.dismiss();
				}
			});

			apiLogcatText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(context, DevShowLogsActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("logcat", false);
					context.startActivity(intent);
					dialog.dismiss();
				}
			});

			monitorNetworkText.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!mBasicDevelopMode.isNetworkMonitoring()) {
						mBasicDevelopMode.startNetworkMonitor();
					}
					else {
						mBasicDevelopMode.stopNetworkMonitor();
					}
					dialog.dismiss();
				}
			});

			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(rageShakeView);

			dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ERROR));
			dialog.show();
			dialog.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					isShow = false;
				}
			});

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					try {
						dialog.dismiss();
					}
					catch (IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}, 10 * 1000);
			isShow = true;
		}

	}

	public void setView(View w) {
		mView = new WeakReference<View>(w);
	}

	private void screenshotDialog() {

		if (mView != null && mView.get() != null) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy_M_dd_HH_mm_ss");
				String now = formatter.format(+System.currentTimeMillis());
				mDialogScreenshotPath = mMainPath + "rageshake_dialog_screenshot_" + now + ".jpg";
				Bitmap bitmap;

				View v1 = mView.get().getRootView();
				v1.setDrawingCacheEnabled(true);
				bitmap = Bitmap.createBitmap(v1.getDrawingCache());
				v1.setDrawingCacheEnabled(false);

				OutputStream fout = null;
				File imageFile = new File(mDialogScreenshotPath);
				if (imageFile.exists()) {
					imageFile.delete();
				}
				try {
					imageFile.createNewFile();
				} catch (IOException e1) {

				}
				try {
					fout = new FileOutputStream(imageFile);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
					fout.flush();
					fout.close();

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (Exception e) {
				mDialogScreenshotPath = null;
			}

		} else {
			mDialogScreenshotPath = null;
		}
	}

	public void start(Context context) {
		if (isRageShakeEnable()) {
			mContextStack.push(context);
			if (mMySensorEventListener == null) {
				mMySensorEventListener = new MySensorEventListener();

				SensorManager sensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
				sensorMgr.registerListener(mMySensorEventListener,
						sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
						SensorManager.SENSOR_DELAY_NORMAL);
			}
		}
	}

	public void stop(Context context) {
		mContextStack.remove(context);
		if (mContextStack.isEmpty()) {
			SensorManager sensorMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			sensorMgr.unregisterListener(mMySensorEventListener);
			mMySensorEventListener = null;
		}
	}

	// UI
	private static class RageShakeView extends LinearLayout {
		public TextView titleText;
		public CheckBox rageshakeCheckBox;
		public TextView reportText;
		public TextView shareText;
		public TextView logcatText;
		public TextView apiLogcatText;
		public TextView monitorNetworkText;

		public RageShakeView(Context context) {
			super(context);
			this.setPadding(15, 15, 15, 15);
			this.setBackgroundColor(Color.WHITE);
			this.setOrientation(LinearLayout.VERTICAL);

			titleText = new TextView(context);
			titleText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			titleText.setBackgroundColor(0xff2a2a32);
			titleText.setTextColor(Color.WHITE);
			titleText.setGravity(Gravity.CENTER);
			titleText.setTextSize(15);
			titleText.setPadding(0, 5, 0, 5);
			addView(titleText);

			View line = new View(context);
			line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line.setBackgroundColor(Color.GRAY);
			addView(line);

			rageshakeCheckBox = new CheckBox(context);
			rageshakeCheckBox.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			rageshakeCheckBox.setTextColor(Color.BLACK);
			rageshakeCheckBox.setGravity(Gravity.CENTER);
			rageshakeCheckBox.setTextSize(20);
			rageshakeCheckBox.setText("Enable Rage Shake");
			rageshakeCheckBox.setPadding(0, 5, 0, 5);
			addView(rageshakeCheckBox);

			View line2 = new View(context);
			line2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line2.setBackgroundColor(Color.GRAY);
			addView(line2);

			reportText = new TextView(context);
			reportText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			reportText.setTextColor(Color.BLACK);
			reportText.setGravity(Gravity.CENTER);
			reportText.setTextSize(20);
			reportText.setText("Report");
			reportText.setBackgroundColor(0x55662121);
			reportText.setPadding(0, 5, 0, 5);
			addView(reportText);

			View line3 = new View(context);
			line3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line3.setBackgroundColor(Color.GRAY);
			addView(line3);

			shareText = new TextView(context);
			shareText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			shareText.setTextColor(Color.BLACK);
			shareText.setGravity(Gravity.CENTER);
			shareText.setTextSize(20);
			shareText.setText("Share Screenshot");
			shareText.setBackgroundColor(0x55662121);
			shareText.setPadding(0, 5, 0, 5);
			addView(shareText);

			View line4 = new View(context);
			line4.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line4.setBackgroundColor(Color.GRAY);
			addView(line4);

			logcatText = new TextView(context);
			logcatText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			logcatText.setTextColor(Color.BLACK);
			logcatText.setGravity(Gravity.CENTER);
			logcatText.setTextSize(20);
			logcatText.setText("Show Logcat");
			logcatText.setBackgroundColor(0x55662121);
			logcatText.setPadding(0, 5, 0, 5);
			addView(logcatText);

			View line5 = new View(context);
			line5.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line5.setBackgroundColor(Color.GRAY);
			addView(line5);

			apiLogcatText = new TextView(context);
			apiLogcatText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			apiLogcatText.setTextColor(Color.BLACK);
			apiLogcatText.setGravity(Gravity.CENTER);
			apiLogcatText.setTextSize(20);
			apiLogcatText.setText("Show API Logcat");
			apiLogcatText.setBackgroundColor(0x55662121);
			apiLogcatText.setPadding(0, 5, 0, 5);
			addView(apiLogcatText);

			View line6 = new View(context);
			line6.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line6.setBackgroundColor(Color.GRAY);
			addView(line6);

			monitorNetworkText = new TextView(context);
			monitorNetworkText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			monitorNetworkText.setTextColor(Color.BLACK);
			monitorNetworkText.setGravity(Gravity.CENTER);
			monitorNetworkText.setTextSize(20);
			monitorNetworkText.setText("Show Network Monitor");
			monitorNetworkText.setBackgroundColor(0x55662121);
			monitorNetworkText.setPadding(0, 5, 0, 5);
			addView(monitorNetworkText);

		}

		public View addMoreButton(String button_key, String button_text) {
			View line = new View(getContext());
			line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line.setBackgroundColor(Color.GRAY);
			addView(line);

			TextView textView = new TextView(getContext());
			textView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textView.setTextColor(Color.BLACK);
			textView.setGravity(Gravity.CENTER);
			textView.setTextSize(20);
			textView.setText(button_text);
			textView.setBackgroundColor(0x55662121);
			textView.setPadding(0, 5, 0, 5);
			addView(textView);

			return textView;
		}
	}

	public static CheckBox getRageShakeButton(final Context context) {
		return getRageShakeButton(context, null);
	}

	public static CheckBox getRageShakeButton(final Context context, final BasicDevelopMode basicDevelopMode) {
		CheckBox rageshakeCheckBox = new CheckBox(context);
		rageshakeCheckBox.setTextColor(Color.BLACK);
		rageshakeCheckBox.setGravity(Gravity.CENTER);
		rageshakeCheckBox.setTextSize(20);
		rageshakeCheckBox.setText("Enable Rage Shake");
		rageshakeCheckBox.setChecked(BasicRageShake.getInstance(context, basicDevelopMode).isRageShakeEnable());
		if (DevConfig.IS_RC) {
			rageshakeCheckBox.setVisibility(View.GONE);
		}
		else {
			rageshakeCheckBox.setVisibility(View.VISIBLE);
		}

		rageshakeCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					BasicRageShake.getInstance(context, basicDevelopMode).enableRageShake();
				}
				else {
					BasicRageShake.getInstance(context, basicDevelopMode).disableRageShake();
				}
			}
		});

		return rageshakeCheckBox;
	}

}
