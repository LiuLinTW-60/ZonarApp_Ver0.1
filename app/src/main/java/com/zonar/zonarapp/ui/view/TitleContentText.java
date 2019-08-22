
package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;

public class TitleContentText extends FreeLayout {
    public FreeTextView titleText;
    public FreeTextView contentText;

    public TitleContentText(Context context) {
        super(context);

        this.setFreeLayoutFF();

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(mContext),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        titleText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                300, LayoutParams.WRAP_CONTENT);
        titleText.setTextSizeFitResolution(55);
        titleText.setTextColor(0xffffffff);

        contentText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                300, LayoutParams.WRAP_CONTENT,
                titleText,
                new int[]{END_OF});
        contentText.setTextSizeFitResolution(55);
        contentText.setGravity(Gravity.CENTER_VERTICAL);
        contentText.setTextColor(0xff616566);
        contentText.setSingleLine();
        setMargin(contentText, 0, 8, 0, 0);
    }

    public void setText(int resid1, int resid2) {
        titleText.setText(resid1);
        contentText.setText(resid2);
    }

    public void setText(String text1, String text2) {
        titleText.setText(text1);
        contentText.setText(TextUtils.isEmpty(text2) || "null".equalsIgnoreCase(text2) ? "" : text2);
    }

    public void setTitle(String text) {
        titleText.setText(text);
    }

    public void setContent(String text) {
        contentText.setText(text);
    }

    public String getTitle() {
        return titleText.getText().toString();
    }

    public String getContent() {
        return contentText.getText().toString();
    }
}
