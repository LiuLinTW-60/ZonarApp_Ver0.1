package com.james.views.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.james.easyclass.R;
import com.james.views.FreeAdapter;
import com.james.views.FreeLayout;
import com.james.views.FreeTextView;

import java.util.ArrayList;

/**
 * Created by spring60569 on 15/11/11.
 */
public class EasySingleSelectDialog extends Dialog {

    private Activity activity;

    private static EasySingleSelectDialog dialog;
    private FreeLayout dialogLayout;
    private FreeTextView titleText;
    private ListView listView;
    private ArrayList<View> dismissViews = new ArrayList<View>();

    private Animation showAnimation;
    private Animation dismissAnimation;

    private OnSingleSelectClickListener mOnSingleSelectClickListener;

    public interface OnSingleSelectClickListener {
        public void onSingleSelectClick(int position);
    }

    boolean canceledOnTouchOutside = true;

    public EasySingleSelectDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        activity = (Activity) context;

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

        titleText = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        titleText.setBackgroundResource(R.drawable.round_top_orange_background);
        titleText.setGravity(Gravity.CENTER_VERTICAL);
        titleText.setTextColor(Color.WHITE);
        titleText.setTextSizeFitResolution(35);
        dialogLayout.setPadding(titleText, 25, 15, 25, 15);

        listView = (ListView) dialogLayout.addFreeView(new ListView(context),
                500, RelativeLayout.LayoutParams.WRAP_CONTENT,
                titleText,
                new int[]{RelativeLayout.BELOW});
        listView.setBackgroundColor(Color.TRANSPARENT);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setCacheColorHint(Color.TRANSPARENT);
        listView.setSelector(R.drawable.none);
        dialogLayout.setPadding(listView, 10, 5, 10, 5);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnSingleSelectClickListener != null) {
                    mOnSingleSelectClickListener.onSingleSelectClick(position);
                }
                dismiss();
            }
        });
    }

    public EasySingleSelectDialog setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            titleText.setVisibility(View.GONE);
            titleText.setText(null);
        } else {
            titleText.setVisibility(View.VISIBLE);
            titleText.setText(title);
        }
        return this;
    }

    public EasySingleSelectDialog setItems(ArrayList<String> items) {
        if (items.size() > 5) {
            dialogLayout.setFreeView(listView,
                    500, 500,
                    titleText,
                    new int[]{RelativeLayout.BELOW});
        } else {
            dialogLayout.setFreeView(listView,
                    500, RelativeLayout.LayoutParams.WRAP_CONTENT,
                    titleText,
                    new int[]{RelativeLayout.BELOW});
        }

        FreeAdapter<String, View> adapter = new FreeAdapter<String, View>(getContext(), items) {
            @Override
            public View initView(int position) {
                FreeLayout freeLayout = new FreeLayout(getContext());
                freeLayout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 80));
                freeLayout.setPicSize(640);
                freeLayout.setBackgroundColor(Color.TRANSPARENT);

                FreeTextView textView = (FreeTextView) freeLayout.addFreeView(new FreeTextView(getContext()),
                        480, 60,
                        new int[]{FreeLayout.CENTER_IN_PARENT});
                textView.setTextSizeFitResolution(38);
                textView.setTextColor(0xfff37a69);
                textView.setGravity(Gravity.CENTER);

                View divider = freeLayout.addFreeView(new View(getContext()),
                        480, 1,
                        new int[]{FreeLayout.CENTER_HORIZONTAL});
                divider.setBackgroundColor(0xaa4a4a4a);

                return freeLayout;
            }

            @Override
            public void setView(int position, View view) {
                FreeLayout freeLayout = (FreeLayout) view;

                FreeTextView textView = (FreeTextView) freeLayout.getChildAt(0);
                textView.setText(getItem(position));

                if (position == 0) {
                    freeLayout.getChildAt(1).setVisibility(View.GONE);
                } else {
                    freeLayout.getChildAt(1).setVisibility(View.VISIBLE);
                }
            }
        };

        listView.setAdapter(adapter);

        return this;
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

    public static void showSlef(Activity activity, String title, ArrayList<String> items, OnSingleSelectClickListener onSingleSelectClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasySingleSelectDialog(activity);
        }
        dialog.setTitle(title);
        dialog.setItems(items);
        dialog.setOnSingleSelectClickListener(onSingleSelectClickListener);
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    public static void showSlef(Activity activity, String title, ArrayList<String> items, boolean cancelable, OnSingleSelectClickListener onSingleSelectClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new EasySingleSelectDialog(activity);
        }
        dialog.setCanceledOnTouchOutside(cancelable);
        dialog.setTitle(title);
        dialog.setItems(items);
        dialog.setOnSingleSelectClickListener(onSingleSelectClickListener);
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void show() {
        super.show();
//        dialogLayout.startAnimation(getShowAnimation());
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
                EasySingleSelectDialog.super.dismiss();
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

    public EasySingleSelectDialog setOnSingleSelectClickListener(OnSingleSelectClickListener onSingleSelectClickListener) {
        mOnSingleSelectClickListener = onSingleSelectClickListener;
        return this;
    }
}
