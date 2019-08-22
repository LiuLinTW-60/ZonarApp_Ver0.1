package com.james.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.james.easyclass.R;
import com.james.views.FreeLayout;
import com.james.views.FreeTextButton;
import com.james.views.FreeTextView;

import java.util.ArrayList;

/**
 * Created by spring60569 on 2015/4/15.
 */
public class EasyAlertDialog extends Dialog {

    private static EasyAlertDialog dialog;

    private FreeLayout dialogLayout;
    private FreeTextView titleText;
    private FreeTextView alertText;
    private FreeTextButton confirmButton;
    private ArrayList<View> dismissViews = new ArrayList<>();

    private OnClickListener mOnClickListener;

    boolean canceledOnTouchOutside = true;

    public EasyAlertDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        init();
    }

    private void init() {
        FreeLayout baseLayout = new FreeLayout(getContext());
        baseLayout.setFreeLayoutFF();
        baseLayout.setBackgroundColor(0x99000000);

        dialogLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(getContext()),
                450, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_IN_PARENT});
        dialogLayout.setBackgroundResource(R.drawable.dialog_bg_ios);
        baseLayout.setPadding(dialogLayout, 0, 15, 0, 0);

        titleText = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(getContext()),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL});
        titleText.setGravity(Gravity.CENTER);
        titleText.setTextColor(0xff000000);
        titleText.setTextSizeFitResolution(36);
        titleText.setTypeface(Typeface.DEFAULT_BOLD);
        dialogLayout.setPadding(titleText, 35, 0, 35, 0);

        alertText = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(getContext()),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL},
                titleText,
                new int[]{RelativeLayout.BELOW});
        alertText.setGravity(Gravity.CENTER);
        alertText.setTextColor(0xff000000);
        alertText.setTextSizeFitResolution(36);
        dialogLayout.setPadding(alertText, 35, 0, 35, 0);
        dialogLayout.setMargin(alertText, 0, 5, 0, 0);

        FreeLayout buttonLayout = (FreeLayout) dialogLayout.addFreeView(new FreeLayout(getContext()),
                450, 80,
                new int[]{RelativeLayout.ALIGN_PARENT_RIGHT},
                alertText,
                new int[]{RelativeLayout.BELOW});
        dialogLayout.setMargin(buttonLayout, 0, 20, 0, 0);

        confirmButton = (FreeTextButton) buttonLayout.addFreeView(new FreeTextButton(getContext()),
                450, 80);
        confirmButton.setBackgroundColor(0x00000000);
        confirmButton.setGravity(Gravity.CENTER);
        confirmButton.setTextColor(0xff1a19f3);
        confirmButton.setTextSizeFitResolution(36);
        confirmButton.setText("確定");

        //
        View divider1 = buttonLayout.addFreeView(new View(getContext()),
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        divider1.setBackgroundColor(0xff616161);

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

        setListener();
    }

    private void setListener() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(EasyAlertDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
                dismiss();
            }
        });
    }

    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            alertText.setVisibility(View.GONE);
            alertText.setText(null);
        } else {
            alertText.setVisibility(View.VISIBLE);
            alertText.setText(message);
        }
    }

    public static Dialog showSelf(Activity activity, String message) {
        return showSelf(activity, message, null);
    }

    public static Dialog showSelf(Activity activity, String message, OnClickListener onClickListener) {
        return showSelf(activity, "", message, onClickListener);
    }

    public static Dialog showSelf(Activity activity, String title, String message, OnClickListener onClickListener) {
        try {
            if(dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyAlertDialog(activity);
        }
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.mOnClickListener = onClickListener;

        dialog.titleText.setVisibility(TextUtils.isEmpty(title) ? View.GONE : View.VISIBLE);
        dialog.titleText.setText(title);

        if (!activity.isFinishing()) {
            if (TextUtils.isEmpty(message)) {
                dialog.alertText.setVisibility(View.GONE);
                dialog.alertText.setText(null);
            } else {
                dialog.alertText.setVisibility(View.VISIBLE);
                dialog.alertText.setText(message);
            }
            dialog.show();
        }

        return dialog;
    }

    public static void dismissSelf() {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);

        this.canceledOnTouchOutside = cancel;
    }
}
