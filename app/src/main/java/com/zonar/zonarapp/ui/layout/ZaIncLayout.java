package com.zonar.zonarapp.ui.layout;

import android.content.Context;
import android.widget.ImageView;

import com.james.utils.MonitorUtils;
import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class ZaIncLayout extends FreeLayout {

    public ZaIncLayout(Context context) {
        super(context);

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        baseLayout.setBackgroundColor(0xff090909);

        ImageView bgImage = (ImageView) baseLayout.addFreeView(new ImageView(context),
                LayoutParams.MATCH_PARENT, 400,
                new int[]{ALIGN_PARENT_BOTTOM});
        bgImage.setImageResource(R.drawable.bg_xr);
        setMargin(bgImage, 0, 0, 0, 110);

        FreeLayout scrollLayout = baseLayout.addFreeScrollView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        ImageView logoImage = (ImageView) scrollLayout.addFreeView(new ImageView(context),
                406, 86,
                new int[]{CENTER_HORIZONTAL});
        logoImage.setImageResource(R.drawable.logo_xround);
        setMargin(logoImage, 0, 90, 0, 0);

        FreeTextView introductionText = (FreeTextView) scrollLayout.addFreeView(new FreeTextView(context),
                576, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_HORIZONTAL},
                logoImage,
                new int[]{BELOW});
        introductionText.setTextSizeFitResolution(50);
        introductionText.setTextColor(0xffffffff);
        introductionText.setText(R.string.introduction);
        introductionText.setLineSpacing(MonitorUtils.resizeByMonitor(context, 5), 1);
        setMargin(introductionText, 0, 70, 0, 0);

    }
}
