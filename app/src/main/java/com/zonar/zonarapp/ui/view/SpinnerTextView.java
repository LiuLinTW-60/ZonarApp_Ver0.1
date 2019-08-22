
package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class SpinnerTextView extends FreeLayout {
    public FreeTextView titleText;
    public ImageView arrowIcon;

    public SpinnerTextView(Context context) {
        super(context);

        this.setFreeLayoutFF();

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(mContext),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        setPadding(baseLayout, 0, 0, 42, 0);

        titleText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_VERTICAL});
        titleText.setTextSizeFitResolution(48);
        titleText.setTextColor(0xff999999);

        arrowIcon = (ImageView) baseLayout.addFreeView(new ImageView(context),
                24, 48,
                new int[]{CENTER_VERTICAL},
                titleText,
                new int[]{END_OF});
        arrowIcon.setImageResource(R.drawable.ic_select_down);
        setMargin(arrowIcon, 8, 0, 0, 0);

    }

    public void setTitle(String text) {
        titleText.setText(text);
    }

    public void setTitle(CharSequence text) {
        titleText.setText(text);
    }

    public String getTitle() {
        return titleText.getText().toString();
    }
}
