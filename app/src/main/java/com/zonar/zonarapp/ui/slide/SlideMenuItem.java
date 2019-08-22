package com.zonar.zonarapp.ui.slide;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class SlideMenuItem extends FreeLayout {

    public ImageView iconImage;
    public FreeTextView itemText;
    public ImageView nextImage;

    public SlideMenuItem(Context context) {
        super(context);

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, 108);
        setPadding(baseLayout, 24, 0, 24, 0);

        iconImage = (ImageView) baseLayout.addFreeView(new ImageView(context),
                48, 48,
                new int[]{CENTER_VERTICAL});

        nextImage = (ImageView) baseLayout.addFreeView(new ImageView(context),
                48, 48,
                new int[]{CENTER_VERTICAL, ALIGN_PARENT_RIGHT});
        nextImage.setImageResource(R.drawable.btn_next);

        itemText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_VERTICAL},
                iconImage,
                new int[]{RIGHT_OF},
                nextImage,
                new int[]{LEFT_OF});
        itemText.setTextColor(Color.BLACK);
        itemText.setTextSizeFitResolution(44);
        setPadding(itemText, 24, 0, 0, 0);

        View divider = baseLayout.addFreeView(new View(context),
                670, 1,
                new int[]{ALIGN_PARENT_BOTTOM, ALIGN_PARENT_RIGHT});
        divider.setBackgroundColor(0xffd1d1d1);

    }
}
