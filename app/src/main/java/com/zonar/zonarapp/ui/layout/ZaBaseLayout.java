package com.zonar.zonarapp.ui.layout;

import android.content.Context;

import com.james.views.FreeLayout;
import com.zonar.zonarapp.ui.slide.SlideMenuView;
import com.zonar.zonarapp.ui.view.Toolbar;

public class ZaBaseLayout extends FreeLayout {
    public Toolbar toolbar;
    public FreeLayout contentLayout;
    public SlideMenuView slideMenuView;

    public ZaBaseLayout(Context context) {
        super(context);

        toolbar = (Toolbar) this.addFreeView(new Toolbar(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        contentLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                toolbar,
                new int[]{BELOW});
        contentLayout.setBackgroundColor(0xff050505);

        slideMenuView = (SlideMenuView) this.addFreeView(new SlideMenuView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        slideMenuView.setVisibility(GONE);
    }
}
