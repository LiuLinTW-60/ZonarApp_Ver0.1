package com.zonar.zonarapp.ui.layout;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ListView;

import com.james.views.FreeLayout;
import com.zonar.zonarapp.R;
import com.zonar.zonarapp.ui.view.CircleWaveView;
import com.zonar.zonarapp.ui.view.RingTextView;
import com.zonar.zonarapp.ui.view.RingView;
import com.zonar.zonarapp.ui.view.TabTextView;

public class ZaMajorLayout extends FreeLayout{

    public TabTextView intelligenceTab;
    public TabTextView hifiTab;
    public CircleWaveView circleWaveView;
    //public ImageView rimImage;
    public RingView rimImage;
    public RingTextView rimText;
    public ListView modeList;

    public ZaMajorLayout(Context context, double[] EQ_arr) {
        super(context);
        this.setBackgroundColor(0xff050505);
        //
        //DrawerLayout drawerView = findViewById(R.layout.activity_drawer);

        FreeLayout bottomLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, 116,
                new int[]{ALIGN_PARENT_BOTTOM});
        bottomLayout.setBackgroundColor(0xffc48c5c);

        intelligenceTab = (TabTextView) bottomLayout.addFreeView(new TabTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        intelligenceTab.tabIcon.setImageResource(R.drawable.ic_ai);
        intelligenceTab.tabText.setText("智慧模式");

        hifiTab = (TabTextView) bottomLayout.addFreeView(new TabTextView(context),
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
                intelligenceTab,
                new int[]{END_OF});
        hifiTab.tabIcon.setImageResource(R.drawable.ic_hifi);
        hifiTab.tabText.setText("Hi-Fi模式");

        View divider = bottomLayout.addFreeView(new View(context),
                1, 72,
                new int[]{CENTER_VERTICAL},
                intelligenceTab,
                new int[]{END_OF});
        divider.setBackgroundColor(0xff000000);

        //
        FreeLayout baseLayout = (FreeLayout) this.addFreeView(new FreeLayout(context),
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
                bottomLayout,
                new int[]{ABOVE});
//        FreeLayout scrollLayout = baseLayout.addFreeScrollView(new FreeLayout(context),
//                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        //FreeLayout draw = (FreeLayout) baseLayout.addFreeView(inflate(context,R.layout.activity_drawer,null)
          //      , DrawerLayout.LayoutParams.MATCH_PARENT, DrawerLayout.LayoutParams.MATCH_PARENT);

        FreeLayout adjustLayout = (FreeLayout) baseLayout.addFreeView(new FreeLayout(context),
                664, 664,
                new int[]{CENTER_HORIZONTAL});
        setMargin(adjustLayout, 0, 34, 0, 0);

        circleWaveView = (CircleWaveView) adjustLayout.addFreeView(new CircleWaveView(context, EQ_arr),
                580, 580,
                new int[]{CENTER_IN_PARENT});

        rimImage = (RingView) adjustLayout.addFreeView(new RingView(context), 664,664, new int[]{CENTER_HORIZONTAL});
        rimText = (RingTextView) adjustLayout.addFreeView(new RingTextView(context), 664, 664, new int[]{CENTER_HORIZONTAL});

        //
        modeList = (ListView) baseLayout.addFreeView(new ListView(context),
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        modeList.setCacheColorHint(Color.TRANSPARENT);
        modeList.setBackgroundColor(0xaa4a3727);
        modeList.setVisibility(GONE);

        //
        setListener();
    }

    private void setListener() {
        circleWaveView.setOnAngleChangedListener(new CircleWaveView.OnAngleChangedListener() {
            @Override
            public void onAngleChanged(float angle) {
//                LogUtils.v("ZaMajorLayout", "[OnAngleChangedListener] angle -> " + angle);

                rimImage.setRotation(angle);

                rimText.rotate(angle);
            }
        });
    }
}
