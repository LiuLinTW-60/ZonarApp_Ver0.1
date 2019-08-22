package com.zonar.zonarapp.ui.layout;

import android.content.Context;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class ZaContactLayout extends FreeLayout {

    public ImageView fbImage;
    public ImageView lineImage;
    public ImageView wechatImage;

    public ZaContactLayout(Context context) {
        super(context);

        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        baseLayout.setBackgroundColor(0xff090909);

        FreeLayout centerLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_IN_PARENT});

        ImageView logoImage = (ImageView) centerLayout.addFreeView(new ImageView(context),
                406, 86);
        logoImage.setImageResource(R.drawable.logo_xround);

        FreeTextView contactUsText = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                logoImage,
                new int[]{BELOW, ALIGN_START});
        contactUsText.setTextSizeFitResolution(60);
        contactUsText.setTextColor(0xffffffff);
        contactUsText.setText("聯絡我們");
        setMargin(contactUsText, 20, 90, 0, 0);

        FreeTextView phoneText = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                contactUsText,
                new int[]{BELOW, ALIGN_START});
        phoneText.setTextSizeFitResolution(40);
        phoneText.setTextColor(0xffffffff);
        phoneText.setText("+886 28228 0106");
        setMargin(phoneText, 0, 40, 0, 0);

        FreeTextView emailText = (FreeTextView) centerLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                phoneText,
                new int[]{BELOW, ALIGN_START});
        emailText.setTextSizeFitResolution(40);
        emailText.setTextColor(0xffffffff);
        emailText.setText("service@embracelab.com");
        setMargin(emailText, 0, 40, 0, 0);

        fbImage = (ImageView) centerLayout.addFreeView(new ImageView(context),
                110, 110,
                emailText,
                new int[]{BELOW, ALIGN_START});
        fbImage.setImageResource(R.drawable.ic_fb);
        setMargin(fbImage, 0, 50, 0, 0);

        lineImage = (ImageView) centerLayout.addFreeView(new ImageView(context),
                110, 110,
                emailText,
                new int[]{BELOW},
                fbImage,
                new int[]{END_OF});
        lineImage.setImageResource(R.drawable.ic_line);
        setMargin(lineImage, 52, 50, 0, 0);

        wechatImage = (ImageView) centerLayout.addFreeView(new ImageView(context),
                110, 110,
                emailText,
                new int[]{BELOW},
                lineImage,
                new int[]{END_OF});
        wechatImage.setImageResource(R.drawable.ic_wechat);
        setMargin(wechatImage, 52, 50, 0, 0);

    }
}
