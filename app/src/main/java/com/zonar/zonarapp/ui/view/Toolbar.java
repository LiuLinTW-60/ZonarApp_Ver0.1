package com.zonar.zonarapp.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;
import com.zonar.zonarapp.R;

public class Toolbar extends FreeLayout {

    public ImageView backButton;
    public ImageView menuButton;
    public LinearLayout menuLayout;
    public FreeTextView titleText;
    public ImageView logoImage;

    public Toolbar(Context context) {
        super(context);
        setBackgroundColor(0xff050505);

        FreeLayout freeLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, 100);

        FreeLayout buttonLayout = (FreeLayout) freeLayout.addFreeView(new FreeLayout(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_VERTICAL});

        backButton = (ImageView) buttonLayout.addFreeView(new ImageView(context),
                80, 80,
                new int[]{CENTER_VERTICAL});
        backButton.setImageResource(R.drawable.ic_back);
        setPadding(backButton, 15, 15, 15, 15);

        menuButton = (ImageView) buttonLayout.addFreeView(new ImageView(context),
                80, 80,
                new int[]{CENTER_VERTICAL});
        menuButton.setImageResource(R.drawable.ic_hamburger);
        menuButton.setVisibility(GONE);
        setPadding(menuButton, 15, 15, 15, 15);

        menuLayout = (LinearLayout) freeLayout.addFreeView(new LinearLayout(context),
                LayoutParams.WRAP_CONTENT, 80,
                new int[]{CENTER_VERTICAL, ALIGN_PARENT_RIGHT});
        menuLayout.setOrientation(LinearLayout.HORIZONTAL);
        menuLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        titleText = (FreeTextView) freeLayout.addFreeView(new FreeTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                new int[]{CENTER_IN_PARENT});
        titleText.setTextColor(Color.WHITE);
        titleText.setTextSizeFitResolution(60);
        titleText.setGravity(Gravity.CENTER);
        titleText.setVisibility(GONE);

        logoImage = (ImageView) freeLayout.addFreeView(new ImageView(context),
                220, 52,
                new int[]{CENTER_VERTICAL});
        logoImage.setImageResource(R.drawable.logo_zonar);
        logoImage.setVisibility(VISIBLE);
        setMargin(logoImage, 105, 0, 0, 0);

    }

    public void hideAppButton() {
        backButton.setVisibility(View.GONE);
        menuButton.setVisibility(View.GONE);
    }

    public void showMenuButton() {
        backButton.setVisibility(View.GONE);
        menuButton.setVisibility(View.VISIBLE);
    }

    public void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
        menuButton.setVisibility(View.GONE);
    }

    public ImageView getBackButton() {
        return backButton;
    }
}
