package com.zonar.zonarapp.ui.layout;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class ZaUpdateLayout extends FreeLayout {

    public FreeTextView nameText;
    public View statusView;
    public FreeTextView statusText;
    public FreeTextView updateText;
    public FreeTextView versionText;
    public FreeTextView sizeText;
    public FreeTextView updateButton;

    public ZaUpdateLayout(Context context) {
        super(context);

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        baseLayout.setBackgroundColor(0xff090909);

        ImageView bgImage = (ImageView) baseLayout.addFreeView(new ImageView(context),
                LayoutParams.MATCH_PARENT, 750,
                new int[]{ALIGN_PARENT_BOTTOM});
        bgImage.setImageResource(R.drawable.bg_update);

        nameText = (FreeTextView) baseLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        nameText.setTextColor(0xffffffff);
        nameText.setTextSizeFitResolution(70);
        nameText.setText(Html.fromHtml("<b>ZONAR</b> version 1.2"));
        setMargin(nameText, 120, 50, 0, 0);

        FreeLayout statusLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                nameText,
                new int[]{BELOW, ALIGN_START});
        setMargin(statusLayout, 0, 12, 0, 0);

        statusView = statusLayout.addFreeView(new View(context),
                72, 20,
                new int[]{CENTER_VERTICAL});
        statusView.setBackgroundColor(0xff7ed321);
        setMargin(statusView, 0, 0, 0, 0);

        statusText = (FreeTextView) statusLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_VERTICAL},
                statusView,
                new int[]{END_OF});
        statusText.setTextColor(0xff7ed321);
        statusText.setTextSizeFitResolution(40);
        statusText.setText("產品連接中");
        setMargin(statusText, 14, 0, 0, 0);

        FreeLayout centerLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(context),
                626, 418,
                new int[]{CENTER_HORIZONTAL},
                statusLayout,
                new int[]{BELOW});
        centerLayout.setBackgroundResource(R.drawable.round_dark_background_light_border);
        setMargin(centerLayout, 0, 50, 0, 0);

        updateText = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        updateText.setTextColor(0xffffffff);
        updateText.setTextSizeFitResolution(70);
        updateText.setText("有新任體可更新");
        setMargin(updateText, 57, 74, 0, 0);

        versionText = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                updateText,
                new int[]{BELOW});
        versionText.setTextColor(0xffffffff);
        versionText.setTextSizeFitResolution(40);
        versionText.setText("版本：2.1");
        setMargin(versionText, 57, 26, 0, 0);

        sizeText = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                versionText,
                new int[]{BELOW});
        sizeText.setTextColor(0xffffffff);
        sizeText.setTextSizeFitResolution(40);
        sizeText.setText("檔案大小：21MB");
        setMargin(sizeText, 57, 15, 0, 0);

        updateButton = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                514, 90,
                sizeText,
                new int[]{BELOW});
        updateButton.setBackgroundResource(R.drawable.btn_update);
        updateButton.setTextColor(0xff000000);
        updateButton.setTextSizeFitResolution(55);
        updateButton.setGravity(Gravity.CENTER);
        updateButton.setText("馬上更新");
        setMargin(updateButton, 57, 30, 0, 0);

    }
}
