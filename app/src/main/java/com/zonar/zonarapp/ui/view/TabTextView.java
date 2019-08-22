
package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class TabTextView extends FreeLayout {
    public ImageView tabIcon;
    public FreeTextView tabText;

    public TabTextView(Context context) {
        super(context);

        this.setFreeLayoutFF();

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                375, 116);

        tabIcon = (ImageView) baseLayout.addFreeView(new ImageView(context),
                74, 50,
                new int[]{CENTER_VERTICAL});
        tabIcon.setImageResource(R.drawable.circle_btn_add);
        setMargin(tabIcon, 82, 0, 0, 0);

        tabText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_VERTICAL},
                tabIcon,
                new int[]{END_OF});
        tabText.setTextColor(0xff000000);
        tabText.setTextSizeFitResolution(50);

    }
}
