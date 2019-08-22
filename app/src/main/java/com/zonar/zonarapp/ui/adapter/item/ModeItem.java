package com.zonar.zonarapp.ui.adapter.item;

import android.content.Context;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class ModeItem extends FreeLayout {

    public ImageView addIcon;
    public FreeTextView itemText;

    public ModeItem(Context context) {
        super(context);

        FreeLayout itemLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, 90);
        setPadding(itemLayout, 130, 0, 0, 0);

        addIcon = (ImageView) itemLayout.addFreeView(new ImageView(context),
                36, 36,
                new int[]{CENTER_VERTICAL});
        addIcon.setImageResource(R.drawable.circle_btn_add);

        itemText = (FreeTextView) itemLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_VERTICAL},
                addIcon,
                new int[]{END_OF});
        itemText.setTextColor(0xffffffff);
        itemText.setTextSizeFitResolution(50);

    }
}
