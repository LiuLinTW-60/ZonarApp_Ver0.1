
package com.james.views.dialog;

import java.util.ArrayList;
import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.text.InputType;
import android.util.Pair;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.james.easyclass.R;
import com.james.easyclass.model.Res;

public class DialogBuilder {

	public interface OnInputListener {
		public void input(String text);
	}

	public interface OnDatePickListener {
		public void pick(Date date);
	}

	public static Dialog showCheckDialog(Activity activity, int resid, final DialogInterface.OnClickListener onClickListener) {
		String message = activity.getResources().getString(resid);
		return showCheckDialog(activity, message, onClickListener);
	}

	public static Dialog showCheckDialog(Activity activity, String message, final DialogInterface.OnClickListener onClickListener) {
		return showCheckDialog(activity, null, message, onClickListener);
	}

	public static Dialog showCheckDialog(Activity activity, String title, String message, final DialogInterface.OnClickListener onClickListener) {
		return showCheckDialog(activity, title, message, null, null, onClickListener);
	}

	public static Dialog showCheckDialog(Activity activity, String title, String message, String positiveButton, String negativeButton, final DialogInterface.OnClickListener onClickListener) {
		if (!activity.isFinishing()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			if (title != null)
				builder.setTitle(title);
			builder.setMessage(message);

			AlertDialog dialog = builder.create();

			String confirm = positiveButton == null ? "Confirm" : positiveButton;
			String cancel = negativeButton == null ? "Cancel" : negativeButton;
			if (onClickListener != null) {
				dialog.setButton(DialogInterface.BUTTON_NEGATIVE, cancel, onClickListener);
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, confirm, onClickListener);
			}

			dialog.show();
			return dialog;
		}
		return null;
	}

	public static Dialog showSimpleDialog(Activity activity, int resid) {
		String content = activity.getResources().getString(resid);
		return showSimpleDialog(activity, content, null);
	}

	public static Dialog showSimpleDialog(Activity activity, int resid, final DialogInterface.OnClickListener onClickListener) {
		String content = activity.getResources().getString(resid);
		return showSimpleDialog(activity, content, onClickListener);
	}

	public static Dialog showSimpleDialog(Activity activity, String message) {
		return showSimpleDialog(activity, message, null);
	}

	public static Dialog showSimpleDialog(Activity activity, String message, final DialogInterface.OnClickListener onClickListener) {
		return showSimpleDialog(activity, null, message, onClickListener);
	}

	public static Dialog showSimpleDialog(Activity activity, String title, String message, final DialogInterface.OnClickListener onClickListener) {
		return showSimpleDialog(activity, title, message, null, onClickListener);
	}

	public static Dialog showSimpleDialog(Activity activity, String title, String message, String positiveButton, final DialogInterface.OnClickListener onClickListener) {
		if (!activity.isFinishing()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			if (title != null)
				builder.setTitle(title);
			builder.setMessage(message);

			AlertDialog dialog = builder.create();

			String text = positiveButton == null ? "Confirm" : positiveButton;
			if (onClickListener != null) {
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, onClickListener);
			}
			else {
				dialog.setButton(DialogInterface.BUTTON_POSITIVE, text, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
			}

			dialog.show();

			return dialog;
		}

		return null;
	}

	public static Dialog showSingleSelectDialog(Activity activity, final String title, final ArrayList<Pair<String, String>> items,
			DialogInterface.OnClickListener onClickListener) {
		CharSequence[] names = new CharSequence[items.size()];
		for (Pair<String, String> item : items) {
			names[items.indexOf(item)] = item.first;
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(title).setItems(names, onClickListener);

		Dialog dialog = builder.create();
		dialog.show();

		return dialog;
	}

    public static Dialog showSingleSelectDialog(Activity activity, final String title, final String[] items,
			DialogInterface.OnClickListener onClickListener) {
        final AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setItems(items, onClickListener);

		Dialog dialog = builder.create();
		dialog.show();

		return dialog;
	}

	public static void showSelectDialog(Activity activity, final String title, final ArrayList<Pair<String, Object>> items,
			DialogInterface.OnClickListener onClickListener){
		CharSequence[] names = new CharSequence[items.size()];
		for(Pair<String, Object> item:items){
			names[items.indexOf(item)] = item.first;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    builder.setTitle(title)
	           .setItems(names, onClickListener);
	    builder.show();	
	}

	public static Dialog showInputDialog(Activity activity, String title, String message, final OnInputListener onInputListener) {
		return showInputDialog(activity, title, message, -1, onInputListener);
	}

    public static Dialog showInputDialog(Activity activity, String title, String message, final int inputType, final OnInputListener onInputListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);

        // Set an EditText view to get user input
        final EditText input = new EditText(activity);
        if(inputType != -1){
            input.setInputType(inputType);
        }
        builder.setView(input);

        builder.setPositiveButton(Res.string.dialog_confirm_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (onInputListener != null) {
                    onInputListener.input(value);
                }
            }
        });

        builder.setNegativeButton(Res.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        Dialog dialog = builder.create();
        dialog.show();

        return dialog;
    }

	public static Dialog showProgressDialog(Activity activity, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		builder.setMessage(message);

		final ProgressBar progressBar = new ProgressBar(activity);
		builder.setView(progressBar);

		Dialog dialog = builder.create();
		dialog.show();

		return dialog;
	}

	/**
	 * @deprecated
	 * @param activity
	 * @param datePick
	 * @param onDatePickListener
	 */
	public static void showDatePickerDialog(Activity activity, final Date datePick, final OnDatePickListener onDatePickListener) {
		final Date date = new Date();

		final TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minute) {
				datePick.setHours(hour);
				datePick.setMinutes(minute);
				if (onDatePickListener != null)
					onDatePickListener.pick(date);
			}
		}, 0, 0, true);

		final DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker arg0, int year, int month, int date) {
				timePickerDialog.show();
				datePick.setYear(year - 1900);
				datePick.setMonth(month);
				datePick.setDate(date);
			}
		}, date.getYear() + 1900, date.getMonth(), date.getDate());
		datePickerDialog.show();
	}

    public static void showDatePickerDialog(Activity activity, final OnDatePickListener onDatePickListener) {
        showDatePickerDialog(activity, false, onDatePickListener);
    }

    public static void showDatePickerDialog(Activity activity, final boolean onlyDate, final OnDatePickListener onDatePickListener) {
        final Date date = new Date();
        final Date datePick = new Date();

        final TimePickerDialog timePickerDialog = new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker arg0, int hour, int minute) {
                datePick.setHours(hour);
                datePick.setMinutes(minute);
                if (onDatePickListener != null)
                    onDatePickListener.pick(datePick);
            }
        }, date.getHours(), date.getMinutes(), true);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker arg0, int year, int month, int date) {
                datePick.setYear(year - 1900);
                datePick.setMonth(month);
                datePick.setDate(date);

                if(onlyDate){
                    if (onDatePickListener != null)
                        onDatePickListener.pick(datePick);
                }
                else {
                    timePickerDialog.show();
                }
            }
        }, date.getYear() + 1900, date.getMonth(), date.getDate());
        datePickerDialog.show();
    }
}
