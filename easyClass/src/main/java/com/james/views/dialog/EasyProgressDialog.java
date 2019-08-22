package com.james.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.james.easyclass.R;
import com.james.utils.MonitorUtils;
import com.james.views.FreeLayout;
import com.james.views.FreeTextView;

import java.util.ArrayList;

/**
 * Created by spring60569 on 2015/4/15.
 */
public class EasyProgressDialog extends Dialog {

    private Activity activity;

    private static EasyProgressDialog dialog;
    private FreeLayout dialogLayout;
    private CustomProgressBar progressBar;
    private FreeTextView progressText;
    private ArrayList<View> dismissViews = new ArrayList<View>();

    private Animation showAnimation;
    private Animation dismissAnimation;

    boolean canceledOnTouchOutside = true;

    public EasyProgressDialog(Activity activity) {
        super(activity, android.R.style.Theme_Translucent_NoTitleBar);

        this.activity = activity;

        init();
    }

    private void init() {

        FreeLayout baseLayout = new FreeLayout(getContext());
        baseLayout.setFreeLayoutFF();
        baseLayout.setBackgroundColor(0x99000000);

        dialogLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(getContext()),
                550, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_IN_PARENT});
        dialogLayout.setBackgroundResource(R.drawable.dialog_bg);
        baseLayout.setPadding(dialogLayout, 0, 35, 0, 35);

        progressBar = (CustomProgressBar) dialogLayout.addFreeView(new CustomProgressBar(getContext()),
                90, 90,
                new int[]{RelativeLayout.CENTER_HORIZONTAL});
        progressBar.setVisibility(View.INVISIBLE);

        progressText = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(getContext()),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL},
                progressBar,
                new int[]{RelativeLayout.BELOW});
        progressText.setTextColor(0xff616161);
        progressText.setTextSizeFitResolution(33);
        progressText.setGravity(Gravity.CENTER);
        progressText.setVisibility(View.GONE);
        dialogLayout.setPadding(progressText, 25, 10, 25, 10);

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
    }

    public void setMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            progressText.setVisibility(View.GONE);
            progressText.setText(null);
        } else {
            progressText.setVisibility(View.VISIBLE);
            progressText.setText(message);
        }
    }

    @Override
    public void show() {
        super.show();
        dialogLayout.startAnimation(getShowAnimation());
        progressBar.setVisibility(View.VISIBLE);
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
                if (!activity.isFinishing()) {
                    progressBar.setVisibility(View.INVISIBLE);
                    EasyProgressDialog.super.dismiss();
                }
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

    public static EasyProgressDialog showSelf(Activity activity, String message) {
        dismissSelf();
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasyProgressDialog(activity);
        }
        if (!activity.isFinishing()) {
            dialog.setMessage(message);
            dialog.show();
        }

        return dialog;
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

    public static class CustomProgressBar extends View {
        private int width, height;
        private Paint paint;
        private int backgroundColor = 0xfff1f1f1;
        private int color = Color.argb(255, 243, 122, 105);

        private Path oval;
        private int strokeWidth = 8;

        private int startAngle = 0;
        private int endAngle = 0;

        private boolean isAnimating = false;

        public CustomProgressBar(Context context) {
            super(context);

            paint = new Paint();

            oval = new Path();

            strokeWidth = MonitorUtils.resizeByMonitor(context, 8);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (getWidth() == 0 || getHeight() == 0) {
                startAngle = 0;
                endAngle = 0;
                return;
            }

            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(backgroundColor);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(width / 2, height / 2, width / 2, paint);

            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(0xff4a4a4a);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(width / 2, height / 2, width / 2 - 1, paint);

            oval.reset();
            float radius = width / 2 - strokeWidth;
            for (int i = startAngle; i < endAngle; i++) {
                double d = i * 2 * Math.PI / 360f;
                if (i == startAngle) {
                    oval.moveTo(width / 2f + radius * (float) Math.cos(d), height / 2 + radius * (float) Math.sin(d));
                } else {
                    oval.lineTo(width / 2f + radius * (float) Math.cos(d), height / 2 + radius * (float) Math.sin(d));
                }
            }

            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor(color);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeWidth(strokeWidth);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(oval, paint);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            this.width = w;
            this.height = h;

            postInvalidate();
        }

        private void start() {
            if (!isAnimating) {
                return;
            }

            startAngle = (startAngle + 29) % 360;
            endAngle = startAngle + 200;
            postInvalidate();

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    start();
                }
            }, 35);
        }

        @Override
        public void setVisibility(int visibility) {
            int original_visibility = getVisibility();
            super.setVisibility(visibility);

            if (visibility == GONE || visibility == INVISIBLE) {
                isAnimating = false;
                startAngle = 0;
                endAngle = 0;
            } else if (visibility == VISIBLE) {
                isAnimating = true;
                if (original_visibility != VISIBLE) {
                    start();
                }
            }
        }

        public void setProgressColor(int color) {
            this.color = color;
        }

        public void setProgressBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }
    }
}
