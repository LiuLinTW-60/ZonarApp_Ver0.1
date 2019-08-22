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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.james.easyclass.R;
import com.james.views.FreeLayout;
import com.james.views.FreeTextButton;
import com.james.views.FreeTextView;

import java.util.ArrayList;

/**
 * Created by spring60569 on 2015/4/15.
 */
public class EasyConfirmDialog extends Dialog {

    private Activity activity;

    private static EasyConfirmDialog dialog;
    private FreeLayout dialogLayout;
    private FreeTextView titleText;
    private FreeTextView alertText;
    private FreeTextButton confirmButton;
    private FreeTextButton cancelButton;
    private ArrayList<View> dismissViews = new ArrayList<>();

    private Animation showAnimation;
    private Animation dismissAnimation;

    private OnClickListener mOnClickListener;

    boolean canceledOnTouchOutside = true;

    public EasyConfirmDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        activity = (Activity) context;

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

        cancelButton = (FreeTextButton) buttonLayout.addFreeView(new FreeTextButton(getContext()),
                225, 80);
        cancelButton.setBackgroundColor(0x00000000);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setTextColor(0xff1a19f3);
        cancelButton.setTextSizeFitResolution(36);
        cancelButton.setText("取消");

        confirmButton = (FreeTextButton) buttonLayout.addFreeView(new FreeTextButton(getContext()),
                225, 80,
                cancelButton,
                new int[]{RelativeLayout.RIGHT_OF});
        confirmButton.setBackgroundColor(0x00000000);
        confirmButton.setGravity(Gravity.CENTER);
        confirmButton.setTextColor(0xff1a19f3);
        confirmButton.setTextSizeFitResolution(36);
        confirmButton.setText("確定");

        //
        View divider1 = buttonLayout.addFreeView(new View(getContext()),
                RelativeLayout.LayoutParams.MATCH_PARENT, 1);
        divider1.setBackgroundColor(0xff616161);

        View divider2 = buttonLayout.addFreeView(new View(getContext()),
                1, RelativeLayout.LayoutParams.MATCH_PARENT,
                new int[]{RelativeLayout.CENTER_IN_PARENT});
        divider2.setBackgroundColor(0xff616161);

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
                    mOnClickListener.onClick(EasyConfirmDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(EasyConfirmDialog.this, DialogInterface.BUTTON_NEGATIVE);
                }
                dismiss();
            }
        });
    }

    public EasyConfirmDialog setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
        return this;
    }

    public static void showSelf(Activity activity, String title, String message, OnClickListener onClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyConfirmDialog(activity);
        }
        dialog.setOnClickListener(onClickListener);
        dialog.titleText.setVisibility(View.VISIBLE);
        dialog.titleText.setText(title);
        dialog.confirmButton.setText("確定");
        dialog.cancelButton.setText("取消");
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
    }

    public static void showSelf(Activity activity, String message, OnClickListener onClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyConfirmDialog(activity);
        }
        dialog.setOnClickListener(onClickListener);
        dialog.titleText.setVisibility(View.GONE);
        dialog.confirmButton.setText("確定");
        dialog.cancelButton.setText("取消");
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
    }

    public static void showSelf(Activity activity, String title, String positiveButtonText, String negativeButtonText, String message, OnClickListener onClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyConfirmDialog(activity);
        }
        dialog.setOnClickListener(onClickListener);
        dialog.titleText.setVisibility(View.VISIBLE);
        dialog.titleText.setText(title);
        dialog.confirmButton.setText(positiveButtonText);
        dialog.cancelButton.setText(negativeButtonText);
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
    }

    public static void showSelf(Activity activity, String positiveButtonText, String negativeButtonText, String message, OnClickListener onClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyConfirmDialog(activity);
        }
        dialog.setOnClickListener(onClickListener);
        dialog.titleText.setVisibility(View.GONE);
        dialog.confirmButton.setText(positiveButtonText);
        dialog.cancelButton.setText(negativeButtonText);
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
    }

    public static void dismissSelf() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void show() {
        super.show();
        dialogLayout.startAnimation(getShowAnimation());
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
                EasyConfirmDialog.super.dismiss();
            }
        }, 250);
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

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);

        this.canceledOnTouchOutside = cancel;
    }
}
