
package com.gogolook.developmode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Pair;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Call init at the first place, or an exception would occur.
 * 
 * @author JamesX
 */
public class BasicDevelopMode {
	protected Context mContext;
	protected SharedPreferences mSharedPreference;
	protected static final String SP_NAME = "share_pref";
	public static String DEVELOP_PASSWORD = "123#321#123";
	//
	public final static String DEFAULT = "default";

	//
	protected boolean mIsEnable = false;
	protected boolean mIsInit = false;

	//
	protected DiagramView mDiagramView;
	protected Timer mNetworkTimer;
	protected Stack<Pair<Date, Long>> mByteCountStack = new Stack<Pair<Date, Long>>();
	protected int mMaxNetworkTraffic = 800;

	private static BasicDevelopMode sDevelopMode;

	public interface OnSettingCompleteListener {
		public void onComplete();
	}

	public void init(Context context) {
		mContext = context;
		mSharedPreference = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		mIsInit = true;
		mIsEnable = mSharedPreference.getBoolean(DevConfig.DEVELOP_PREF_KEY, false);

		if (!DevConfig.IS_RC) {
			mIsEnable = true;

			// monitor network
			final Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					if (mDiagramView != null) {
						mDiagramView.postInvalidate();
					}

					if (getMaxByteCount().second != null && getMaxByteCount().second / 1024L / 1024L > mMaxNetworkTraffic) {
						// 取得Notification服務
						NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
						// 設定當按下這個通知之後要執行的activity
						Intent notifyIntent = new Intent(mContext, DevShowLogsActivity.class);
						notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						PendingIntent appIntent = PendingIntent.getActivity(mContext, 0, notifyIntent, 0);
						Notification notification = new Notification();
						// 設定出現在狀態列的圖示
						notification.icon = android.R.drawable.ic_dialog_alert;
						// 顯示在狀態列的文字
						notification.tickerText = "Warning! Network Traffic exceed " + mMaxNetworkTraffic + " mb !";
						// 會有通知預設的鈴聲、振動、light
						notification.defaults = Notification.DEFAULT_ALL;
						// 設定通知的標題、內容
//						notification.setLatestEventInfo(mContext, "Warning", notification.tickerText, appIntent);
						// 送出Notification
						notificationManager.notify(0x5797, notification);
					}
				}
			};

			mNetworkTimer = new Timer(true);
			mNetworkTimer.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					Date date = new Date();
					long bytes = TrafficStats.getUidRxBytes(mContext.getApplicationInfo().uid);
					if (mByteCountStack.size() > 100) {
						mByteCountStack.remove(0);
					}
					mByteCountStack.push(new Pair<Date, Long>(date, bytes));

					handler.sendEmptyMessage(0);
				}
			}, 1000, 5 * 1000);
		}

	}

	public void checkIsInit() {
		if (!mIsInit)
			throw new IllegalStateException();
	}

	public static BasicDevelopMode getInstance() {
		if (sDevelopMode == null) {
			sDevelopMode = new BasicDevelopMode();
		}

		return sDevelopMode;
	}

	public boolean isDefaultEnable() {
		if (DevConfig.IS_RC) {
			return false;
		} else {
			enable();
			return true;
		}
	}

	public void applyCurrentLanguage() {
		checkIsInit();

		final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		String selectLanguage = pref.getString(DevConfig.LANGUAGE_PREF_KEY, DEFAULT);
		Locale locale = null;
		if (selectLanguage.equals("ar")) {
			locale = new Locale("ar");
		} else if (selectLanguage.equals("ja-rJP")) {
			locale = new Locale("ja", "JP");
		} else if (selectLanguage.equals("ko-rKR")) {
			locale = new Locale("ko", "KR");
		} else if (selectLanguage.equals("ru")) {
			locale = new Locale("ru", "RU");
		} else if (selectLanguage.equals("zh-rCN")) {
			locale = new Locale("zh", "CN");
		} else if (selectLanguage.equals("zh-rHK")) {
			locale = new Locale("zh", "HK");
		} else if (selectLanguage.equals("zh-rTW")) {
			locale = new Locale("zh", "TW");
		} else if (selectLanguage.equals("en-rUS")) {
			locale = new Locale("en", "US");
		} else if (selectLanguage.equals("th")) {
			locale = new Locale("th");
		} else if (selectLanguage.equals("in")) {
			locale = new Locale("in");
		} else if (selectLanguage.equals("vi")) {
			locale = new Locale("vi");
		} else if (selectLanguage.equals("fr")) {
			locale = new Locale("fr");
		} else if (selectLanguage.equals("id")) {
			locale = new Locale("id");
		} else if (selectLanguage.equals("it")) {
			locale = new Locale("it");
		} else if (selectLanguage.equals("ms")) {
			locale = new Locale("ms");
		} else if (selectLanguage.equals("pt")) {
			locale = new Locale("pt");
		} else if (selectLanguage.equals("de")) {
			locale = new Locale("de");
		} else if (selectLanguage.equals("es")) {
			locale = new Locale("es");
		} else if (selectLanguage.equals("tr")) {
			locale = new Locale("tr");

		} else if (selectLanguage.equals("pt-rBR")) {
			locale = new Locale("pt", "BR");

		} else if (selectLanguage.equals("pt-rPT")) {
			locale = new Locale("pt", "PT");

		} else if (selectLanguage.equals("sr")) {
			locale = new Locale("sr");

		} else {
			locale = Resources.getSystem().getConfiguration().locale;
		}

		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
	}

	/**
	 * used to open debug mode
	 * 
	 * @param et
	 * @param activity
	 * @return
	 */
	public boolean checkEnableByEditText(EditText et, Class<?> activity) {
		checkIsInit();
		if (et.getText().toString().equals(DEVELOP_PASSWORD) && !mIsEnable) {
			enable();
			restartApplication(activity);
			return true;
		} else if (et.getText().toString().equals(DEVELOP_PASSWORD) && mIsEnable) {
			disable();
			restartApplication(activity);
			return true;
		}
		return false;
	}

	/**
	 * Restart from a certain activity.
	 * 
	 * @param activity
	 */
	public void restartApplication(Class<?> activity) {
		checkIsInit();

		Intent intent = new Intent(mContext, activity);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		AlarmManager alm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		alm.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, PendingIntent.getActivity(mContext, 0, intent, 0));

		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * Enable develop mode.
	 */
	public void enable() {
		checkIsInit();
		mIsEnable = true;
		mSharedPreference.edit().putBoolean(DevConfig.DEVELOP_PREF_KEY, true).commit();
	}

	/**
	 * Disable develop mode.
	 */
	public void disable() {
		checkIsInit();
		mIsEnable = false;
		mSharedPreference.edit().remove(DevConfig.DEVELOP_PREF_KEY).commit();
	}

	public boolean isEnable() {
		checkIsInit();
		return mIsEnable;
	}

	/**
	 * Get region/country code of device
	 * 
	 * @param context
	 * @return
	 */
	public String getCountry() {
		checkIsInit();

		if (isEnable()) {
			final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
			String select = pref.getString(DevConfig.COUNTRY_PREF_KEY, DEFAULT);
			if (!select.equals(DEFAULT)) {
				return select;
			}
		}
		TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

		return telephony.getNetworkCountryIso().length() > 0 ? telephony.getNetworkCountryIso().toLowerCase() : Locale.getDefault().getCountry().toLowerCase();
	}

	/**
	 * Call this function to set country. <br>
	 * If you need a callback function, please use
	 * 
	 * <pre>
	 * selectCountry(OnSettingCompleteListener onSettingCompleteListener)
	 * </pre>
	 * 
	 * instead
	 */
	public void selectCountry(final Context context) {
		selectCountry(context, null);
	}

	/**
	 * Call this function to set country
	 * 
	 * @param onSettingCompleteListener (OnSettingCompleteListener) a callback invoked after selectCountry finished.
	 */
	public void selectCountry(final Context context, final OnSettingCompleteListener onSettingCompleteListener) {
		checkIsInit();

		List<String> list = DevConfig.getCountries();
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);
		for (int i = 0; i < list.size(); i++) {
			arrayAdapter.add(list.get(i));
		}

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String country = arrayAdapter.getItem(which);
						PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString(DevConfig.COUNTRY_PREF_KEY, country).commit();

						if (onSettingCompleteListener != null) {
							onSettingCompleteListener.onComplete();
						}
					}
				});
		builderSingle.show();
	}

	/**
	 * Call this function to set language. <br>
	 * If you need a callback function, please use
	 * 
	 * <pre>
	 * selectLanguage(OnSettingCompleteListener onSettingCompleteListener)
	 * </pre>
	 * 
	 * instead
	 */
	public void selectLanguage(final Context context) {
		selectLanguage(context, null);
	}

	/**
	 * call this function to set language
	 * 
	 * @param onSettingCompleteListener (OnSettingCompleteListener) a callback invoked after selectCountry finished.
	 */
	public void selectLanguage(final Context context, final OnSettingCompleteListener onSettingCompleteListener) {
		checkIsInit();

		List<String> list = DevConfig.getLanguages();
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
		for (int i = 0; i < list.size(); i++) {
			arrayAdapter.add(list.get(i));
		}

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String language = arrayAdapter.getItem(which);
						PreferenceManager.getDefaultSharedPreferences(context).edit().putString(DevConfig.LANGUAGE_PREF_KEY, language).commit();

						applyCurrentLanguage();

						if (onSettingCompleteListener != null) {
							onSettingCompleteListener.onComplete();
						}
					}
				});
		builderSingle.show();
	}

	/**
	 * require following permission
	 * 
	 * <pre>
	 * android.permission.READ_SMS or android.permission.WRITE_SMS
	 * </pre>
	 */
	public void recordContentProvider(final Context context) {
		checkIsInit();

		List<String> list = DevConfig.getSMSList();
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_singlechoice);
		for (int i = 0; i < list.size(); i++) {
			arrayAdapter.add(list.get(i));
		}

		builderSingle.setAdapter(arrayAdapter,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String strName = arrayAdapter.getItem(which);

						exportData(context, strName);
					}
				});
		builderSingle.show();
	}

	private void exportData(final Context context, String name) {
		AsyncTask<String, Void, File> asyncTask = new AsyncTask<String, Void, File>() {

			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				progressDialog = new ProgressDialog(context);
				progressDialog.setMessage("Exporting...");
				progressDialog.show();
			}

			@Override
			protected File doInBackground(String... params) {
				String strName = params[0];
				String mailstring = "";
				Uri uri = Uri.parse(strName);
				Cursor c = context.getContentResolver().query(uri, null, null, null, null);
				try {
					c = context.getContentResolver().query(uri, null, null, null, "date desc");
				} catch (Exception e) {

				}
				if (c != null) {
					String[] ss = c.getColumnNames();
					for (int i = 0; i < ss.length; i++) {
						mailstring += ss[i] + ",";
					}
					mailstring += "\n";

					for (int i = 0; i < c.getCount() && i < 300; i++) {
						c.moveToPosition(i);
						for (int j = 0; j < c.getColumnCount(); j++) {
							mailstring += c.getString(j) + ",";
						}
						mailstring += "\n";
					}
					c.close();
				}
				File file;
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					file = new File(context.getExternalCacheDir().getAbsolutePath(), "/db_" + Build.MODEL + "_" + Build.MANUFACTURER + ".csv");
				}
				else {
					file = new File(context.getCacheDir().getAbsolutePath(), "/db_" + Build.MODEL + "_" + Build.MANUFACTURER + ".csv");
				}

				if (file.exists()) {
					file.delete();
				}

				try {
					file.createNewFile();
				} catch (IOException e1) {

				}

				FileOutputStream outputStream;
				try {
					outputStream = new FileOutputStream(file.getAbsolutePath());
					outputStream.write(mailstring.getBytes());
					outputStream.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

				return file;
			}

			@Override
			protected void onPostExecute(File file) {
				progressDialog.dismiss();

				Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(Intent.EXTRA_EMAIL, DevConfig.RECIEVERS);
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[CallLog Report][" + context.getApplicationInfo().loadLabel(context.getPackageManager()) + "][" + Build.MODEL + " - " + Build.MANUFACTURER + "]");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, getReportString(mContext));
				ArrayList<Uri> uris = new ArrayList<Uri>();
				uris.add(Uri.parse("file://" + file.getAbsolutePath()));
				emailIntent.putParcelableArrayListExtra(android.content.Intent.EXTRA_STREAM, uris);
				emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(emailIntent);
			}
		};

		asyncTask.execute(name);
	}

	public boolean isNetworkMonitoring() {
		return mDiagramView != null;
	}

	/**
	 * Start monitor network usage <br>
	 * Default max network traffic is 800 mb to send notification.
	 */
	public void startNetworkMonitor() {
		startNetworkMonitor(mMaxNetworkTraffic);
	}

	/**
	 * Start monitor network usage
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void startNetworkMonitor(int max) {
		checkIsInit();

		mMaxNetworkTraffic = max;
		if (mDiagramView == null) {
			mDiagramView = createMonitorView();
		}
	}

	/**
	 * Stop monitor network usage
	 */
	public void stopNetworkMonitor() {
		if (mDiagramView != null) {
			WindowManager wm = (WindowManager) mContext.getSystemService("window");
			wm.removeView(mDiagramView);
			mDiagramView = null;
		}
	}

	private DiagramView createMonitorView() {
		DiagramView diagramView = new DiagramView(mContext);
		// get WindowManager
		WindowManager windowManager = (WindowManager) mContext.getSystemService("window");
		// set LayoutParams
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
		layoutParams.format = PixelFormat.RGBA_8888;

		/*
		 * 下面的flags屬性的效果形同"鎖定"。懸浮窗不可觸摸，不接受任何事件,同時不影響後面的事件響應。 wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_NOT_TOUCHABLE;
		 */
		layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// set relative position
		layoutParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		//
		layoutParams.width = mContext.getResources().getDisplayMetrics().widthPixels / 2;
		layoutParams.height = mContext.getResources().getDisplayMetrics().heightPixels / 10;
		//
		windowManager.addView(diagramView, layoutParams);

		return diagramView;
	}

	private class DiagramView extends RelativeLayout {

		private Paint paint;
		private TextView minText;
		private TextView maxText;

		public DiagramView(Context context) {
			super(context);
			this.setBackgroundColor(0xaa000000);

			minText = new TextView(context);
			minText.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			((RelativeLayout.LayoutParams) minText.getLayoutParams()).addRule(ALIGN_PARENT_BOTTOM);
			((RelativeLayout.LayoutParams) minText.getLayoutParams()).addRule(ALIGN_PARENT_LEFT);
			minText.setTextColor(Color.GRAY);
			minText.setTextSize(7);
			minText.setGravity(Gravity.LEFT);
			addView(minText);

			maxText = new TextView(context);
			maxText.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			((RelativeLayout.LayoutParams) maxText.getLayoutParams()).addRule(ALIGN_PARENT_BOTTOM);
			((RelativeLayout.LayoutParams) maxText.getLayoutParams()).addRule(ALIGN_PARENT_RIGHT);
			maxText.setTextColor(Color.GRAY);
			maxText.setTextSize(7);
			maxText.setGravity(Gravity.RIGHT);
			addView(maxText);

			paint = new Paint();
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			paint.reset();
			paint.setStyle(Style.FILL_AND_STROKE);
			paint.setColor(0x9900ff00);
			Pair<Date, Long> max = getMaxByteCount();
			Pair<Date, Long> min = getMinByteCount();
			if (max == null || min == null || max.second == 0) {
				return;
			}

			minText.setText("min\n" + String.valueOf(min.second / 1024L) + " bytes\n" + min.first.toLocaleString().replace(" ", "\n"));
			maxText.setText("max\n" + String.valueOf(max.second / 1024L) + " bytes\n" + max.first.toLocaleString().replace(" ", "\n"));

			for (int i = 0; i < mByteCountStack.size(); i++) {
				Pair<Date, Long> pair = mByteCountStack.get(i);
				int left = getWidth() / mByteCountStack.size() * i;
				int bottom = getHeight();
				int width = getWidth() / mByteCountStack.size();
				int height = (int) (pair.second / 1024L / 1024L / 10);
				if (pair.second / 1024L / 1024L > mMaxNetworkTraffic) {
					paint.setColor(0x99ff0000);
				}
				else {
					paint.setColor(0x9900ff00);
				}
				Rect rect = new Rect(left, bottom - height, left + width, bottom);
				canvas.drawRect(rect, paint);
			}
		}
	}

	private Pair<Date, Long> getMaxByteCount() {
		if (mByteCountStack.size() == 0) {
			return null;
		}

		ArrayList<Pair<Date, Long>> byteCounts = new ArrayList<Pair<Date, Long>>();
		byteCounts.addAll(mByteCountStack);
		Collections.sort(byteCounts, new Comparator<Pair<Date, Long>>() {

			@Override
			public int compare(Pair<Date, Long> lhs, Pair<Date, Long> rhs) {
				if (lhs.second > rhs.second) {
					return -1;
				}
				else if (lhs.second < rhs.second) {
					return 1;
				}
				return 0;
			}
		});

		return byteCounts.get(0);
	}

	private Pair<Date, Long> getMinByteCount() {
		if (mByteCountStack.size() == 0) {
			return null;
		}

		ArrayList<Pair<Date, Long>> byteCounts = new ArrayList<Pair<Date, Long>>();
		byteCounts.addAll(mByteCountStack);
		Collections.sort(byteCounts, new Comparator<Pair<Date, Long>>() {

			@Override
			public int compare(Pair<Date, Long> lhs, Pair<Date, Long> rhs) {
				if (lhs.second > rhs.second) {
					return 1;
				}
				else if (lhs.second < rhs.second) {
					return -1;
				}
				return 0;
			}
		});

		return byteCounts.get(0);
	}

	/**
	 * 取得版本名稱
	 * 
	 * @param context
	 * @return
	 */
	private static String getVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	/**
	 * @return
	 */
	private static String getReportString(Context context) {
		return "Device: " + Build.MODEL + "\nDisplay: " + Build.DISPLAY
				+ "\nVersion: " + getVersionName(context)
				+ "\nAndroid API: " + VERSION.SDK_INT;
	}
}
