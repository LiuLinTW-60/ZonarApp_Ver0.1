
package com.gogolook.developmode;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

/**
 * init DevUncaughtExceptionHandler in Application
 * 
 * @author JamesX
 */
public class DevUncaughtExceptionHandler implements UncaughtExceptionHandler {
	private Context mContext;
	private UncaughtExceptionHandler mUncaughtExceptionHandler;

	/**
	 * init DevUncaughtExceptionHandler in Application
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		Thread.setDefaultUncaughtExceptionHandler(new DevUncaughtExceptionHandler(context, Thread.getDefaultUncaughtExceptionHandler()));
	}

	public DevUncaughtExceptionHandler(Context context, UncaughtExceptionHandler defaultHandler) {
		mContext = context;
		mUncaughtExceptionHandler = defaultHandler;
	}

	private void showDialog(final Throwable ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		final String error = sw.toString();

		Log.e(mContext.getPackageName(), error);

		Toast.makeText(mContext, "Unfortunately, application has stopped.\n\nPlease press \"Report\" to send crash information to developers.", Toast.LENGTH_LONG).show();
		sendErrorMail(mContext, error);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}

		// final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		// Thread t = new Thread() {
		// public void run() {
		// Looper.prepare();
		// builder.setPositiveButton("Report",
		// new DialogInterface.OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// sendErrorMail(mContext, error);
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		//
		// }
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(0);
		// }
		// });
		//
		// AlertDialog dialog = builder.create();
		// dialog.setOnDismissListener(new OnDismissListener() {
		//
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		// try {
		// Thread.sleep(1000);
		// } catch (InterruptedException e) {
		//
		// }
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(0);
		//
		// }
		// });
		// dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ERROR));
		// LayoutParams lp = dialog.getWindow().getAttributes();
		// lp.alpha = 0.7f;
		// dialog.getWindow().setAttributes(lp);
		// dialog.setMessage("Unfortunately, application has stopped.\n\nPlease press \"Report\" to send crash information to developers.");
		// dialog.show();
		//
		// Looper.loop();
		// }
		// };
		// t.start();
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		if (!DevConfig.IS_RC) {
			showDialog(ex);
		} else {
			mUncaughtExceptionHandler.uncaughtException(thread, ex);
		}

	}

	private void sendErrorMail(Context context, String errorContent) {
		Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
		emailIntent.setType("plain/text");
		emailIntent.putExtra(Intent.EXTRA_EMAIL, DevConfig.RECIEVERS);
		if (errorContent.split("\n").length > 0) {
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Crash Report][" + context.getApplicationInfo().loadLabel(context.getPackageManager()) + "][" + getCurrentVersionName(context) + "]" + "[" + errorContent.split("\n")[0] + "]");
		}
		else {
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "[Crash Report][" + context.getApplicationInfo().loadLabel(context.getPackageManager()) + "][" + getCurrentVersionName(context) + "]");
		}
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
				"Model: " + Build.MODEL + "\n\n\n" +
						"Brand: " + Build.BRAND + "\n\n\n" +
						errorContent + "\n\n\n" +
						"**Please attach the screenshot**\nDescribe your steps:\n");
		emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(emailIntent);
	}

	/**
	 * Get current app version
	 * 
	 * @param context
	 * @return
	 */
	private static String getCurrentVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		String packageName = context.getPackageName();

		try {
			PackageInfo info = packageManager.getPackageInfo(packageName, 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

}
