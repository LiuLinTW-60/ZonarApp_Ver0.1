package com.zonar.zonarapp.ui.layout;

import android.content.Context;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.zonar.zonarapp.R;
import com.zonar.zonarapp.ui.view.TitleContentText;

public class ZaAboutLayout extends FreeLayout {

    public TitleContentText modelText;
    public TitleContentText versionText;
    public TitleContentText serialNumberText;
    public TitleContentText currentText;
    public TitleContentText oaeText;
    public TitleContentText hiresText;
    public TitleContentText ancText;
    public TitleContentText hearThruText;

    public ZaAboutLayout(Context context) {
        super(context);

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        baseLayout.setBackgroundColor(0xff090909);

        ImageView bgImage = (ImageView) baseLayout.addFreeView(new ImageView(context),
                LayoutParams.MATCH_PARENT, 400,
                new int[]{ALIGN_PARENT_BOTTOM});
        bgImage.setImageResource(R.drawable.bg_xr);
        setMargin(bgImage, 0, 0, 0, 110);

        modelText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        modelText.setText("產品型號", "ZONAR");
        setMargin(modelText, 120, 80, 0, 0);

        versionText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                modelText,
                new int[]{BELOW});
        versionText.setText("韌體版本", "1.2");
        setMargin(versionText, 120, 30, 0, 0);

        serialNumberText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                versionText,
                new int[]{BELOW});
        serialNumberText.setText("產品獨立序號", "13425425");
        setMargin(serialNumberText, 120, 30, 0, 0);

        currentText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                serialNumberText,
                new int[]{BELOW});
        currentText.setText("產品目前狀態", "2354363");
        setMargin(currentText, 120, 30, 0, 0);

        oaeText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                currentText,
                new int[]{BELOW});
        oaeText.setText("OAE", "On");
        setMargin(oaeText, 120, 30, 0, 0);

        hiresText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                oaeText,
                new int[]{BELOW});
        hiresText.setText("Hires/一般模式", "Hi-Res");
        setMargin(hiresText, 120, 30, 0, 0);

        ancText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                hiresText,
                new int[]{BELOW});
        ancText.setText("ANC", "On");
        setMargin(ancText, 120, 30, 0, 0);

        hearThruText = (TitleContentText) baseLayout.addFreeView(new TitleContentText(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                ancText,
                new int[]{BELOW});
        hearThruText.setText("HearThru", "Off");
        setMargin(hearThruText, 120, 30, 0, 0);
    }
}
