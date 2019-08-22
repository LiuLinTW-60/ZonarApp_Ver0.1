
package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class MenuTextView extends FreeLayout {
    public FreeTextView titleText;
    public ImageView arrowIcon;

    public MenuTextView(Context context) {
        super(context);

        this.setFreeLayoutFF();

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(mContext),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        titleText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                LayoutParams.MATCH_PARENT, 70,
                new int[]{CENTER_HORIZONTAL});
        titleText.setTextSizeFitResolution(60);
        titleText.setTextColor(0xffffffff);

        arrowIcon = (ImageView) baseLayout.addFreeView(new ImageView(context),
                60, 60,
                new int[]{CENTER_VERTICAL, ALIGN_PARENT_END});
        arrowIcon.setImageResource(R.drawable.arrow_white_40);
        arrowIcon.setVisibility(GONE);
        setMargin(arrowIcon, 0, 0, 20, 0);

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

    public ImageView getArrowIcon() {
        return arrowIcon;
    }
}
