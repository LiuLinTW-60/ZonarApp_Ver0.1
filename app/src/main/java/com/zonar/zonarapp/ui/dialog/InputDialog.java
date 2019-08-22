package com.zonar.zonarapp.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.james.views.FreeEditText;
import com.james.views.FreeLayout;
import com.james.views.FreeTextButton;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

import java.util.ArrayList;

/**
 * Created by spring60569 on 15/11/11.
 */
public class InputDialog extends Dialog {

    private Activity activity;

    private static InputDialog dialog;
    private FreeLayout dialogLayout;
    private FreeTextView alertText;
    private FreeTextView editTitle1;
    private FreeTextView editTitle2;
    private FreeTextView editTitle3;
    private FreeEditText editText1;
    private FreeEditText editText2;
    private FreeEditText editText3;
    private FreeTextView psText;
    private FreeTextButton confirmButton;
    private FreeTextButton cancelButton;
    private ArrayList<View> dismissViews = new ArrayList<>();

    private Animation showAnimation;
    private Animation dismissAnimation;

    private OnClickListener mOnClickListener;

    boolean canceledOnTouchOutside = true;

    private int limit = -1;

    public InputDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        activity = (Activity) context;

        init(context);
    }

    private void init(Context context) {
        limit = -1;

        FreeLayout baseLayout = new FreeLayout(context);
        baseLayout.setFreeLayoutFF();
        baseLayout.setBackgroundColor(0x66313131);

        dialogLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(context),
                550, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL});
        dialogLayout.setBackgroundResource(R.drawable.dialog_bg);
        baseLayout.setMargin(dialogLayout, 0, 170, 0, 0);

        alertText = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(getContext()),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        alertText.setGravity(Gravity.CENTER);
        alertText.setTextColor(0xff666666);
        alertText.setTextSizeFitResolution(44);
        alertText.setVisibility(View.GONE);
        dialogLayout.setPadding(alertText, 10, 5, 20, 5);
        dialogLayout.setMargin(alertText, 10, 10, 10, 0);

        editTitle1 = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(context),
                95, RelativeLayout.LayoutParams.WRAP_CONTENT,
                alertText,
                new int[]{RelativeLayout.BELOW});
        editTitle1.setTextColor(Color.BLACK);
        editTitle1.setTextSizeFitResolution(48);
        editTitle1.setVisibility(View.GONE);
        dialogLayout.setPadding(editTitle1, 10, 5, 20, 5);
        dialogLayout.setMargin(editTitle1, 10, 10, 10, 0);

        editText1 = (FreeEditText) dialogLayout.addFreeView(new FreeEditText(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                alertText,
                new int[]{RelativeLayout.BELOW},
                editTitle1,
                new int[]{RelativeLayout.RIGHT_OF});
        editText1.setTextColor(Color.BLACK);
        editText1.setBackgroundResource(R.drawable.input);
        editText1.setTextSizeFitResolution(48);
        dialogLayout.setPadding(editText1, 10, 5, 20, 5);
        dialogLayout.setMargin(editText1, 10, 10, 10, 0);

        editTitle2 = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(context),
                95, RelativeLayout.LayoutParams.WRAP_CONTENT,
                editTitle1,
                new int[]{RelativeLayout.BELOW});
        editTitle2.setTextColor(Color.BLACK);
        editTitle2.setTextSizeFitResolution(48);
        editTitle2.setVisibility(View.GONE);
        dialogLayout.setPadding(editTitle2, 10, 5, 20, 5);
        dialogLayout.setMargin(editTitle2, 10, 20, 10, 0);

        editText2 = (FreeEditText) dialogLayout.addFreeView(new FreeEditText(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                editText1,
                new int[]{RelativeLayout.BELOW},
                editTitle2,
                new int[]{RelativeLayout.RIGHT_OF});
        editText2.setTextColor(Color.BLACK);
        editText2.setBackgroundResource(R.drawable.input);
        editText2.setTextSizeFitResolution(48);
        dialogLayout.setPadding(editText2, 10, 5, 20, 5);
        dialogLayout.setMargin(editText2, 10, 25, 10, 0);

        editTitle3 = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(context),
                95, RelativeLayout.LayoutParams.WRAP_CONTENT,
                editTitle2,
                new int[]{RelativeLayout.BELOW});
        editTitle3.setTextColor(Color.BLACK);
        editTitle3.setTextSizeFitResolution(48);
        editTitle3.setVisibility(View.GONE);
        dialogLayout.setPadding(editTitle3, 10, 5, 20, 5);
        dialogLayout.setMargin(editTitle3, 10, 5, 10, 0);

        editText3 = (FreeEditText) dialogLayout.addFreeView(new FreeEditText(context),
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                editText2,
                new int[]{RelativeLayout.BELOW},
                editTitle3,
                new int[]{RelativeLayout.RIGHT_OF});
        editText3.setTextColor(Color.BLACK);
        editText3.setBackgroundResource(R.drawable.input);
        editText3.setTextSizeFitResolution(48);
        dialogLayout.setPadding(editText3, 10, 5, 20, 5);
        dialogLayout.setMargin(editText3, 10, 5, 10, 0);

        psText = (FreeTextView) dialogLayout.addFreeView(new FreeTextView(getContext()),
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.CENTER_HORIZONTAL},
                editText3,
                new int[]{RelativeLayout.BELOW});
        psText.setTextColor(0xffc1c1c1);
        psText.setTextSizeFitResolution(32);
        psText.setGravity(Gravity.CENTER_VERTICAL);
        psText.setVisibility(View.GONE);
        dialogLayout.setPadding(psText, 10, 5, 20, 5);
        dialogLayout.setMargin(psText, 0, 10, 0, 0);

        confirmButton = (FreeTextButton) dialogLayout.addFreeView(new FreeTextButton(getContext()),
                275, RelativeLayout.LayoutParams.WRAP_CONTENT,
                new int[]{RelativeLayout.ALIGN_PARENT_RIGHT},
                psText,
                new int[]{RelativeLayout.BELOW});
        confirmButton.setBackgroundColor(0x00000000);
        confirmButton.setGravity(Gravity.CENTER);
        confirmButton.setTextColor(0xfff37a69);
        confirmButton.setTextSizeFitResolution(40);
        confirmButton.setText("確定");
        dialogLayout.setPadding(confirmButton, 10, 15, 10, 15);
        dialogLayout.setMargin(confirmButton, 0, 20, 0, 0);

        cancelButton = (FreeTextButton) dialogLayout.addFreeView(new FreeTextButton(getContext()),
                275, RelativeLayout.LayoutParams.WRAP_CONTENT,
                psText,
                new int[]{RelativeLayout.BELOW},
                confirmButton,
                new int[]{RelativeLayout.LEFT_OF});
        cancelButton.setBackgroundColor(0x00000000);
        cancelButton.setGravity(Gravity.CENTER);
        cancelButton.setTextColor(0xff4a4a4a);
        cancelButton.setTextSizeFitResolution(40);
        cancelButton.setText("取消");
        dialogLayout.setPadding(cancelButton, 10, 15, 10, 15);
        dialogLayout.setMargin(cancelButton, 0, 20, 0, 0);

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
                    mOnClickListener.onClick(InputDialog.this, DialogInterface.BUTTON_POSITIVE);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnClickListener != null) {
                    mOnClickListener.onClick(InputDialog.this, DialogInterface.BUTTON_NEGATIVE);
                }
                dismiss();
            }
        });

        if (limit > 0) {
            editText1.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (editText1.getText().toString().length() > limit) {
                        Toast.makeText(activity, "字數太多了 (限" + limit + "字以內)", Toast.LENGTH_SHORT).show();
                        editText1.setText(editText1.getText().toString().substring(0, limit));
                    }
                }
            });

            editText2.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (editText2.getText().toString().length() > limit) {
                        Toast.makeText(activity, "字數太多了 (限" + limit + "字以內)", Toast.LENGTH_SHORT).show();
                        editText2.setText(editText2.getText().toString().substring(0, limit));
                    }
                }
            });

            editText3.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (editText3.getText().toString().length() > limit) {
                        Toast.makeText(activity, "字數太多了 (限" + limit + "字以內)", Toast.LENGTH_SHORT).show();
                        editText3.setText(editText3.getText().toString().substring(0, limit));
                    }
                }
            });
        }
    }

    public InputDialog setInputType(int inputType) {
        editText1.setInputType(inputType);
        editText2.setInputType(inputType);
        editText3.setInputType(inputType);

        return this;
    }

    public InputDialog setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            alertText.setVisibility(View.GONE);
            alertText.setText(null);
        } else {
            alertText.setVisibility(View.VISIBLE);
            alertText.setText(title);
        }
        return this;
    }

    public InputDialog setText(String text1, String text2) {
        return setText(text1, text2, null);
    }

    public InputDialog setText(String text1, String text2, String text3) {
        editText1.setText(text1);
        editText2.setText(text2);
        editText3.setText(text3);

        if (TextUtils.isEmpty(text2)) {
            editText2.setVisibility(View.GONE);
        } else {
            editText2.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(text3)) {
            editText3.setVisibility(View.GONE);
        } else {
            editText3.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public InputDialog setHint(String hint1, String hint2) {
        return setHint(hint1, hint2, null);
    }

    public InputDialog setHint(String hint1, String hint2, String hint3) {
        editText1.setHint(hint1);
        editText2.setHint(hint2);
        editText3.setHint(hint3);

        if (TextUtils.isEmpty(hint2)) {
            editText2.setVisibility(View.GONE);
        } else {
            editText2.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(hint3)) {
            editText3.setVisibility(View.GONE);
        } else {
            editText3.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public InputDialog setPsWording(String psWording) {
        psText.setText(psWording);

        if (TextUtils.isEmpty(psWording)) {
            psText.setVisibility(View.GONE);
        } else {
            psText.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public InputDialog setInputTitle(String title1, String title2) {
        return setInputTitle(title1, title2, null);
    }

    public InputDialog setInputTitle(String title1, String title2, String title3) {
        editTitle1.setHint(title1);
        editTitle2.setHint(title2);
        editTitle3.setHint(title3);

        if (TextUtils.isEmpty(title1)) {
            editTitle1.setVisibility(View.GONE);
            editText1.setVisibility(View.GONE);
        } else {
            editTitle1.setVisibility(View.VISIBLE);
            editText1.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(title2)) {
            editTitle2.setVisibility(View.GONE);
            editText2.setVisibility(View.GONE);
        } else {
            editTitle2.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.VISIBLE);
        }

        if (TextUtils.isEmpty(title3)) {
            editTitle3.setVisibility(View.GONE);
            editText3.setVisibility(View.GONE);
        } else {
            editTitle3.setVisibility(View.VISIBLE);
            editText3.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public FreeEditText getEditText1() {
        return editText1;
    }

    public FreeEditText getEditText2() {
        return editText2;
    }

    public FreeEditText getEditText3() {
        return editText3;
    }

    public String getEdiTextContent1() {
        return editText1.getText().toString();
    }

    public String getEdiTextContent2() {
        return editText2.getText().toString();
    }

    public String getEdiTextContent3() {
        return editText3.getText().toString();
    }

    public void setLimitTextLength(int limit) {
        this.limit = limit;
        setListener();
    }

    public InputDialog setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
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

    public static void showSlef(Activity activity, String message, OnClickListener onClickListener) {
        if (dialog == null || !dialog.getContext().equals(activity)) {
            dialog = new InputDialog(activity);
        }
        dialog.setOnClickListener(onClickListener);
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

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        editText1.setHint(null);
        editText2.setHint(null);
        editText1.setText(null);
        editText2.setText(null);

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
                InputDialog.super.dismiss();
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
}
