package com.zonar.zonarapp.ui.slide;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;
import com.zonar.zonarapp.ZaAboutActivity;
import com.zonar.zonarapp.ZaBaseActivity;
import com.zonar.zonarapp.ZaContactActivity;
import com.zonar.zonarapp.ZaIncActivity;
import com.zonar.zonarapp.ZaUpdateActivity;
import com.zonar.zonarapp.ui.view.MenuTextView;

public class SlideMenuView extends FreeLayout {

    private ZaBaseActivity activity;

    public FreeLayout drawerLayout;
    public FreeLayout scrollLayout;
    public FreeTextView nameText;
    public MenuTextView aboutText;
    public MenuTextView updateText;
    public MenuTextView officialText;
    public MenuTextView supportText;
    public MenuTextView contactText;
    public MenuTextView vrRecordingText;
    public MenuTextView incText;
    public FreeTextView logoutText;

    //
    public FreeLayout closeLayout;

    public SlideMenuView(Context context) {
        super(context);
        activity = (ZaBaseActivity) context;

        setFreeLayoutFF();
        setBackgroundColor(0xff191919);

        drawerLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                654, LayoutParams.MATCH_PARENT);
        drawerLayout.setBackgroundColor(0xff191919);

        scrollLayout = drawerLayout.addFreeScrollView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //
        closeLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                drawerLayout,
                new int[]{RIGHT_OF});
        closeLayout.setBackgroundColor(0xff050505);

        FreeLayout closeIconLayout = (FreeLayout) closeLayout.addFreeView(new FreeLayout(context),
                100, 100);
        closeIconLayout.setBackgroundColor(0xffc48c5c);

        ImageView closeIcon = (ImageView) closeIconLayout.addFreeView(new ImageView(context),
                100, 100);
        closeIcon.setImageResource(R.drawable.ic_close);

        //
        setup();

        setListener();
    }

    public void setup() {
        final Context context = getContext();

        ImageView icon = (ImageView) scrollLayout.addFreeView(new ImageView(context),
                180, 180);
        icon.setImageResource(R.drawable.logo_xr);
        setMargin(icon, 90, 84, 0, 0);

        nameText = (FreeTextView) scrollLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                icon,
                new int[]{END_OF});
        nameText.setTextColor(0xffaab1b3);
        nameText.setTextSizeFitResolution(60);
        nameText.setText("Peng Lee");
        setMargin(nameText, 26, 146, 0, 0);

        aboutText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                icon,
                new int[]{BELOW});
        aboutText.setTitle("關於產品");
        setMargin(aboutText, 104, 40, 0, 0);

        updateText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                aboutText,
                new int[]{BELOW});
        updateText.setTitle("韌體更新");
        setMargin(updateText, 104, 20, 0, 0);

        officialText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                updateText,
                new int[]{BELOW});
        officialText.setTitle("XROUND購物官網");
        setMargin(officialText, 104, 20, 0, 0);

        supportText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                officialText,
                new int[]{BELOW});
        supportText.setTitle("產品支援");
        supportText.getArrowIcon().setVisibility(VISIBLE);
        setMargin(supportText, 104, 20, 0, 0);

        contactText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                supportText,
                new int[]{BELOW});
        contactText.setTitle("聯絡我們");
        setMargin(contactText, 104, 20, 0, 0);

        vrRecordingText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                contactText,
                new int[]{BELOW});
        vrRecordingText.setTitle("VR Recording");
        setMargin(vrRecordingText, 104, 20, 0, 0);

        incText = (MenuTextView) scrollLayout.addFreeView(new MenuTextView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT,
                vrRecordingText,
                new int[]{BELOW});
        incText.setTitle("XROUND Inc.");
        setMargin(incText, 104, 20, 0, 0);

        logoutText = (FreeTextView) scrollLayout.addFreeView(new FreeTextView(context),
                340, 88,
                vrRecordingText,
                new int[]{BELOW});
        logoutText.setText("登出");
        logoutText.setGravity(Gravity.CENTER);
        logoutText.setTextSizeFitResolution(60);
        logoutText.setTextColor(0xff494c4d);
        logoutText.setBackgroundColor(0xffb6b6b6);
        setMargin(logoutText, 104, 152, 0, 0);
    }

    public void show() {
        setVisibility(VISIBLE);
        closeLayout.setVisibility(VISIBLE);
        drawerLayout.setVisibility(VISIBLE);
        drawerLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_left_in));
    }

    public void hide() {
        drawerLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.trans_left_out));
        drawerLayout.setVisibility(GONE);
        closeLayout.setVisibility(GONE);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setVisibility(GONE);
                closeLayout.setVisibility(VISIBLE);
            }
        }, 210);
    }

    private void setListener() {
        aboutText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ZaAboutActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });

        updateText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ZaUpdateActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });

        contactText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ZaContactActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });

        incText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ZaIncActivity.class);
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_left_out);
            }
        });
    }
}
