package com.james.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.james.easyclass.R;
import com.james.views.FreeLayout;
import com.james.views.FreeTextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by spring60569 on 15/11/11.
 */
public class EasyDatePickDialog extends Dialog implements View.OnClickListener {

    private Activity activity;

    private static EasyDatePickDialog dialog;
    private FreeLayout dialogLayout;
    private FreeLayout dateLayout;
    private FreeTextView defaultDateText;
    private FreeTextView yearText;
    private FreeTextView dateText;
    private FreeTextView monthText;
    private ImageView previousMonthIcon;
    private ImageView nextMonthIcon;
    private FreeTextView sundayText;
    private FreeTextView mondayText;
    private FreeTextView tuesdayText;
    private FreeTextView wednesdayText;
    private FreeTextView thursdayText;
    private FreeTextView fridayText;
    private FreeTextView saturdayText;
    private FreeLayout daysLayout;
    private ArrayList<DateTextView> daysText = new ArrayList<DateTextView>();
    private FreeTextView doneText;
    private FreeTextView cancelText;
    private ArrayList<View> dismissViews = new ArrayList<View>();

    private Animation showAnimation;
    private Animation dismissAnimation;

    boolean canceledOnTouchOutside = true;

    private boolean isHour = true;

    private DecimalFormat decimalFormat = new DecimalFormat("00");

    private Date today = new Date();
    private Date pickDate;
    private Date monthDate;
    private Date defaultDate;

    private OnDatePickListener mOnDatePickListener;

    public static interface OnDatePickListener {
        public void onTimePick(Date date);
    }

    public EasyDatePickDialog(Context context, Date pickDate) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        activity = (Activity) context;

        if (pickDate == null) {
            this.pickDate = new Date();
        } else {
            this.pickDate = new Date(pickDate.getTime());
        }

        init(context);
    }

    private void init(Context context) {
        FreeLayout baseLayout = new FreeLayout(context);
        baseLayout.setFreeLayoutFF();
        baseLayout.setBackgroundColor(0x66313131);
        baseLayout.setPicSize(640);

        dialogLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(context),
                500, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_IN_PARENT});
        dialogLayout.setPicSize(640);
        dialogLayout.setBackgroundResource(R.drawable.round_white_background);
        dialogLayout.setPadding(dialogLayout, 0, 0, 0, 5);

        dateLayout = (FreeLayout) dialogLayout.addFreeView(new FreeLayout(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dateLayout.setPicSize(640);
        dateLayout.setBackgroundResource(R.drawable.round_top_red_background);
        dialogLayout.setPadding(dateLayout, 15, 15, 10, 15);

        defaultDateText = (FreeTextView) dateLayout.addFreeView(new FreeTextView(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.ALIGN_PARENT_RIGHT});
        defaultDateText.setTextColor(Color.WHITE);
        defaultDateText.setTextSizeFitResolution(35);
        defaultDateText.setVisibility(View.GONE);
        defaultDateText.setBackgroundResource(R.drawable.round_white_border_red_background);
        dateLayout.setPadding(defaultDateText, 20, 10, 20, 10);
        dateLayout.setMargin(defaultDateText, 0, 5, 5, 0);

        yearText = (FreeTextView) dateLayout.addFreeView(new FreeTextView(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        yearText.setTextColor(Color.WHITE);
        yearText.setTextSizeFitResolution(35);
        dateLayout.setMargin(yearText, 5, 10, 0, 0);

        dateText = (FreeTextView) dateLayout.addFreeView(new FreeTextView(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                yearText,
                new int[]{RelativeLayout.BELOW});
        dateText.setTextColor(Color.WHITE);
        dateText.setTextSizeFitResolution(65);
        dateLayout.setMargin(dateText, 5, 10, 0, 0);

        FreeLayout calendarLayout = (FreeLayout) dialogLayout.addFreeView(new FreeLayout(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, 500,
                dateLayout,
                new int[]{RelativeLayout.BELOW});
        calendarLayout.setPicSize(640);

        monthText = (FreeTextView) calendarLayout.addFreeView(new FreeTextView(context),
                200, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL});
        monthText.setGravity(Gravity.CENTER);
        monthText.setTextColor(0xff414141);
        monthText.setTextSizeFitResolution(35);
        calendarLayout.setMargin(monthText, 0, 20, 0, 0);

        previousMonthIcon = (ImageView) calendarLayout.addFreeView(new ImageView(context),
                35, 35,
                monthText,
                new int[]{RelativeLayout.LEFT_OF});
        previousMonthIcon.setBackgroundResource(R.drawable.btn_previous);
        calendarLayout.setMargin(previousMonthIcon, 0, 20, 10, 0);

        nextMonthIcon = (ImageView) calendarLayout.addFreeView(new ImageView(context),
                35, 35,
                monthText,
                new int[]{RelativeLayout.RIGHT_OF});
        nextMonthIcon.setBackgroundResource(R.drawable.btn_next);
        calendarLayout.setMargin(nextMonthIcon, 10, 20, 0, 0);

        FreeLayout calendarBaseLayout = (FreeLayout) calendarLayout.addFreeView(new FreeLayout(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL},
                monthText,
                new int[]{RelativeLayout.BELOW});
        calendarBaseLayout.setPicSize(640);
        calendarBaseLayout.setPadding(calendarBaseLayout, 0, 10, 0, 10);

        sundayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65);
        sundayText.setGravity(Gravity.CENTER);
        sundayText.setTextColor(0xffd1d1d1);
        sundayText.setTextSizeFitResolution(30);
        sundayText.setText("S");

        mondayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65,
                sundayText,
                new int[]{RelativeLayout.RIGHT_OF});
        mondayText.setGravity(Gravity.CENTER);
        mondayText.setTextColor(0xffd1d1d1);
        mondayText.setTextSizeFitResolution(30);
        mondayText.setText("M");

        tuesdayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65,
                mondayText,
                new int[]{RelativeLayout.RIGHT_OF});
        tuesdayText.setGravity(Gravity.CENTER);
        tuesdayText.setTextColor(0xffd1d1d1);
        tuesdayText.setTextSizeFitResolution(30);
        tuesdayText.setText("T");

        wednesdayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65,
                tuesdayText,
                new int[]{RelativeLayout.RIGHT_OF});
        wednesdayText.setGravity(Gravity.CENTER);
        wednesdayText.setTextColor(0xffd1d1d1);
        wednesdayText.setTextSizeFitResolution(30);
        wednesdayText.setText("W");

        thursdayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65,
                wednesdayText,
                new int[]{RelativeLayout.RIGHT_OF});
        thursdayText.setGravity(Gravity.CENTER);
        thursdayText.setTextColor(0xffd1d1d1);
        thursdayText.setTextSizeFitResolution(30);
        thursdayText.setText("T");

        fridayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65,
                thursdayText,
                new int[]{RelativeLayout.RIGHT_OF});
        fridayText.setGravity(Gravity.CENTER);
        fridayText.setTextColor(0xffd1d1d1);
        fridayText.setTextSizeFitResolution(30);
        fridayText.setText("F");

        saturdayText = (FreeTextView) calendarBaseLayout.addFreeView(new FreeTextView(context),
                65, 65,
                fridayText,
                new int[]{RelativeLayout.RIGHT_OF});
        saturdayText.setGravity(Gravity.CENTER);
        saturdayText.setTextColor(0xffd1d1d1);
        saturdayText.setTextSizeFitResolution(30);
        saturdayText.setText("S");

        daysLayout = (FreeLayout) calendarLayout.addFreeView(new FreeLayout(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL},
                calendarBaseLayout,
                new int[]{RelativeLayout.BELOW});
        daysLayout.setPicSize(640);

        FreeLayout doneLayout = (FreeLayout) dialogLayout.addFreeView(new FreeLayout(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, 80,
                calendarLayout,
                new int[]{RelativeLayout.BELOW});
        doneLayout.setPicSize(640);

        doneText = (FreeTextView) doneLayout.addFreeView(new FreeTextView(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.CENTER_VERTICAL});
        doneText.setGravity(Gravity.CENTER);
        doneText.setTextColor(0xffee5f49);
        doneText.setTextSizeFitResolution(38);
        doneText.setText("OK");
        doneLayout.setPadding(doneText, 15, 5, 15, 5);
        doneLayout.setMargin(doneText, 0, 0, 20, 0);

        cancelText = (FreeTextView) doneLayout.addFreeView(new FreeTextView(context),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_VERTICAL},
                doneText,
                new int[]{RelativeLayout.LEFT_OF});
        cancelText.setGravity(Gravity.CENTER);
        cancelText.setTextColor(0xffee5f49);
        cancelText.setTextSizeFitResolution(38);
        cancelText.setText("CANCEL");
        doneLayout.setPadding(cancelText, 15, 5, 15, 5);
        doneLayout.setMargin(cancelText, 0, 0, 10, 0);

        //
        View view1 = baseLayout.addFreeView(new View(getContext()),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                dialogLayout,
                new int[]{RelativeLayout.ABOVE});
        view1.setBackgroundColor(Color.TRANSPARENT);
        dismissViews.add(view1);

        View view2 = baseLayout.addFreeView(new View(getContext()),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                dialogLayout,
                new int[]{RelativeLayout.BELOW});
        view2.setBackgroundColor(Color.TRANSPARENT);
        dismissViews.add(view2);

        View view3 = baseLayout.addFreeView(new View(getContext()),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                dialogLayout,
                new int[]{RelativeLayout.RIGHT_OF});
        view3.setBackgroundColor(Color.TRANSPARENT);
        dismissViews.add(view3);

        View view4 = baseLayout.addFreeView(new View(getContext()),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT,
                dialogLayout,
                new int[]{RelativeLayout.LEFT_OF});
        view4.setBackgroundColor(Color.TRANSPARENT);
        dismissViews.add(view4);

        for (View view : dismissViews) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!canceledOnTouchOutside) {
                        return;
                    }
                    dismiss();
                }
            });
        }

        setContentView(baseLayout);

        refreshDateText(pickDate);

        monthDate = new Date(pickDate.getYear(), pickDate.getMonth(), 1, 1, 1, 1);
        generateCalendar(monthDate, daysLayout);

        setListener();
    }

    private void generateCalendar(Date monthDate, FreeLayout daysLayout) {
        daysLayout.removeAllViews();
        daysText.clear();

        ArrayList<DateTextView> allDaysText = new ArrayList<DateTextView>();

        Context context = daysLayout.getContext();

        boolean isMatchFirstDay = false;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                int lastIndex = allDaysText.size() - 1;

                DateTextView dayText = null;
                if (i == 0 && j == 0) {
                    dayText = (DateTextView) daysLayout.addFreeView(new DateTextView(context),
                            65, 65);
                } else if (j == 0) {
                    dayText = (DateTextView) daysLayout.addFreeView(new DateTextView(context),
                            65, 65,
                            allDaysText.get(lastIndex - 6),
                            new int[]{RelativeLayout.BELOW});
                } else {
                    dayText = (DateTextView) daysLayout.addFreeView(new DateTextView(context),
                            65, 65,
                            allDaysText.get(lastIndex),
                            new int[]{RelativeLayout.RIGHT_OF, RelativeLayout.ALIGN_BOTTOM});
                }
                dayText.setGravity(Gravity.CENTER);
                dayText.setTextColor(0xff414141);
                dayText.setTextSizeFitResolution(30);
                allDaysText.add(dayText);

                if (i == 0 && monthDate.getDay() == j) {
                    isMatchFirstDay = true;
                }
                if (isMatchFirstDay) {
                    daysText.add(dayText);
                    Date date = new Date(monthDate.getYear(), monthDate.getMonth(), daysText.size(), 1, 1, 1);
                    if (date.getMonth() != monthDate.getMonth()) {
                        continue;
                    }

                    dayText.setDate(date);
                    dayText.setText(String.valueOf(date.getDate()));
                    dayText.setOnClickListener(this);

                    if (date.getYear() == pickDate.getYear() &&
                            date.getMonth() == pickDate.getMonth() &&
                            date.getDate() == pickDate.getDate()) {
                        dayText.setBackgroundResource(R.drawable.circle_red);
                    } else if (date.getYear() == today.getYear() &&
                            date.getMonth() == today.getMonth() &&
                            date.getDate() == today.getDate()) {
                        dayText.setBackgroundResource(R.drawable.circle_green);
                    } else {
                        dayText.setBackgroundResource(R.drawable.btn_clock_selector);
                    }
                } else {
                    dayText.setText("");
                }
            }
        }
    }

    private void refreshDateText(Date date) {
        yearText.setText(new SimpleDateFormat("yyyy").format(date));
        dateText.setText(new SimpleDateFormat("EEEE, MMM dd").format(date));
        monthText.setText(new SimpleDateFormat("MMM yyyy").format(date));
    }

    private void setListener() {
        dateLayout.setOnClickListener(this);
        defaultDateText.setOnClickListener(this);
        previousMonthIcon.setOnClickListener(this);
        nextMonthIcon.setOnClickListener(this);
        doneText.setOnClickListener(this);
        cancelText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(dateLayout)) {
            Date currentDate = new Date();
            final ArrayList<String> years = new ArrayList<String>();
            for (int i = 1930; i < (1901 + currentDate.getYear()); i++) {
                years.add(0, String.valueOf(i));
            }
            EasySingleSelectDialog.showSlef(activity, "西元", years, new EasySingleSelectDialog.OnSingleSelectClickListener() {
                @Override
                public void onSingleSelectClick(int position) {
                    int year = Integer.parseInt(years.get(position));
                    pickDate.setYear(year - 1900);

                    refreshDateText(pickDate);

                    monthDate = new Date(pickDate.getYear(), pickDate.getMonth(), 1, 1, 1, 1);
                    generateCalendar(monthDate, daysLayout);
                }
            });
        } else if (v.equals(defaultDateText)) {
            if (defaultDate == null) {
                defaultDate = new Date();
            }
            pickDate = new Date(defaultDate.getTime());

            refreshDateText(pickDate);

            monthDate = new Date(pickDate.getYear(), pickDate.getMonth(), 1, 1, 1, 1);
            generateCalendar(monthDate, daysLayout);
            generateCalendar(monthDate, daysLayout);
        } else if (v.equals(previousMonthIcon)) {
            monthDate = new Date(monthDate.getYear(), monthDate.getMonth() - 1, 1, 1, 1, 1);
            monthText.setText(new SimpleDateFormat("MMM yyyy").format(monthDate));
            generateCalendar(monthDate, daysLayout);
        } else if (v.equals(nextMonthIcon)) {
            monthDate = new Date(monthDate.getYear(), monthDate.getMonth() + 1, 1, 1, 1, 1);
            monthText.setText(new SimpleDateFormat("MMM yyyy").format(monthDate));
            generateCalendar(monthDate, daysLayout);
        } else if (v.equals(doneText)) {
            if (mOnDatePickListener != null) {
                mOnDatePickListener.onTimePick(pickDate);
            }
            dismiss();
        } else if (v.equals(cancelText)) {
            dismiss();
        } else if (daysText.contains(v)) {
            pickDate = ((DateTextView) v).getDate();

            for (DateTextView dayText : daysText) {
                Date date = dayText.getDate();
                if (date == null) {
                    dayText.setBackgroundResource(R.drawable.btn_clock_selector);
                    continue;
                }

                if (date.getYear() == pickDate.getYear() &&
                        date.getMonth() == pickDate.getMonth() &&
                        date.getDate() == pickDate.getDate()) {
                    dayText.setBackgroundResource(R.drawable.circle_red);
                } else if (date.getYear() == today.getYear() &&
                        date.getMonth() == today.getMonth() &&
                        date.getDate() == today.getDate()) {
                    dayText.setBackgroundResource(R.drawable.circle_green);
                } else {
                    dayText.setBackgroundResource(R.drawable.btn_clock_selector);
                }
            }
            refreshDateText(pickDate);
        }
    }

    private Animation getShowAnimation() {
        if (showAnimation == null) {
            AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);
            alpha.setDuration(500);
            showAnimation = alpha;
        }
        return showAnimation;
    }

    private Animation getDismissAnimation() {
        if (dismissAnimation == null) {
            AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
            alpha.setDuration(250);
            dismissAnimation = alpha;
        }
        return dismissAnimation;
    }

    public void setOnDatePickListener(OnDatePickListener onDatePickListener) {
        mOnDatePickListener = onDatePickListener;
    }

    public void setDefaultDate(Date defaultDate) {
        this.defaultDate = defaultDate;
        defaultDateText.setVisibility(View.GONE);
        if (defaultDate == null) {
            return;
        }
        defaultDateText.setVisibility(View.VISIBLE);
        defaultDateText.setText("相片日期 " + new SimpleDateFormat("yyyy/MM/dd").format(defaultDate));
    }

    public static void showSlef(Activity activity, Date pickDate, OnDatePickListener onDatePickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyDatePickDialog(activity, pickDate);
        }

        dialog.setOnDatePickListener(onDatePickListener);
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    public static void showSlef(Activity activity, Date pickDate, Date defaultDate, OnDatePickListener onDatePickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyDatePickDialog(activity, pickDate);
        }

        dialog.setDefaultDate(defaultDate);
        dialog.setOnDatePickListener(onDatePickListener);
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void show() {
        super.show();
//        dialogLayout.startAnimation(getShowAnimation());

        Toast.makeText(activity, "點擊日期可快速更改年份", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void dismiss() {
        if (activity.isFinishing()) {
            return;
        }
        dialogLayout.startAnimation(getDismissAnimation());
        dialogLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                }
                EasyDatePickDialog.super.dismiss();
            }
        }, 250);
    }

    public static void dismissSelf() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);

        this.canceledOnTouchOutside = cancel;
    }

    public class DateTextView extends FreeLayout {

        private View clickView;
        private FreeTextView textView;

        private OnClickListener mOnClickListener;

        private Date date;

        public DateTextView(Context context) {
            super(context);

            textView = (FreeTextView) this.addFreeView(new FreeTextView(context),
                    50, 50,
                    new int[]{CENTER_IN_PARENT});

            clickView = this.addFreeView(new View(context),
                    30, 30,
                    new int[]{CENTER_IN_PARENT});
            clickView.setBackgroundColor(Color.TRANSPARENT);

            clickView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        mOnClickListener.onClick(DateTextView.this);
                    }
                }
            });
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Date getDate() {
            return date;
        }

        @Override
        public void setOnClickListener(final OnClickListener onClickListener) {
            mOnClickListener = onClickListener;
        }

        public void setTextSizeFitResolution(int size) {
            textView.setTextSizeFitResolution(size);
        }

        public void setGravity(int gravity) {
            textView.setGravity(gravity);
        }

        public void setTextColor(int color) {
            textView.setTextColor(color);
        }

        public void setText(int resid) {
            textView.setText(resid);
        }

        public void setText(CharSequence text) {
            textView.setText(text);
        }

        public CharSequence getText() {
            return textView.getText();
        }
    }
}
