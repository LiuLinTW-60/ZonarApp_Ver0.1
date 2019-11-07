package com.zonar.zonarapp;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.james.utils.MonitorUtils;
import com.james.views.FreeLayout;
import com.zonar.zonarapp.ui.layout.ZaBaseLayout;
import com.zonar.zonarapp.ui.slide.SlideMenuView;
import com.zonar.zonarapp.ui.view.Toolbar;

public class ZaBaseActivity extends EasyBaseFragmentActivity {

    private ZaBaseLayout baseLayout;
    private Toolbar toolbar;
    private FreeLayout contentLayout;
    private SlideMenuView slideMenuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _setLayout();

        _findView();

        _setView();

        _setListener();
    }

    private void _setLayout() {
        baseLayout = new ZaBaseLayout(this);
        super.setContentView(baseLayout);
    }

    private void _findView() {
        toolbar = baseLayout.toolbar;
        contentLayout = baseLayout.contentLayout;
        slideMenuView = baseLayout.slideMenuView;
    }

    private void _setView() {
        hideLogoTitle();

//        if (NaApiUtils.getInstance(this).isLogin()) {
//            slideMenuView.loginButton.setVisibility(View.GONE);
//            slideMenuView.logoutButton.setVisibility(View.VISIBLE);
//        } else {
//            slideMenuView.loginButton.setVisibility(View.VISIBLE);
//            slideMenuView.logoutButton.setVisibility(View.GONE);
//        }
    }

    private void _setListener() {
        toolbar.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideMenuView.show();
                toolbar.backButton.setVisibility(View.VISIBLE);
                toolbar.menuButton.setVisibility(View.GONE);
                toolbar.menuLayout.setVisibility(View.GONE);
                showLogoTitle();
            }
        });

        slideMenuView.closeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideMenuView.hide();
                toolbar.backButton.setVisibility(View.GONE);
                toolbar.menuButton.setVisibility(View.VISIBLE);
                toolbar.menuLayout.setVisibility(View.VISIBLE);
                hideLogoTitle();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (slideMenuView.isShown()) {
            //slideMenuView.hide();
            toolbar.backButton.setVisibility(View.GONE);
            toolbar.menuButton.setVisibility(View.VISIBLE);
            toolbar.menuLayout.setVisibility(View.VISIBLE);
            hideLogoTitle();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void showLogoTitle() {
        toolbar.titleText.setVisibility(View.GONE);
        toolbar.logoImage.setVisibility(View.VISIBLE);
    }

    public void hideLogoTitle() {
        if (isDefaultLogoShow()) {
            toolbar.titleText.setVisibility(View.GONE);
            toolbar.logoImage.setVisibility(View.VISIBLE);
        } else {
            toolbar.titleText.setVisibility(View.VISIBLE);
            toolbar.logoImage.setVisibility(View.GONE);
        }
    }

    public void forceHideNPCLogoTitle() {
        toolbar.titleText.setVisibility(View.VISIBLE);
        toolbar.logoImage.setVisibility(View.GONE);
    }

    @Override
    public void setTitle(CharSequence title) {
        toolbar.titleText.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        toolbar.titleText.setText(titleId);
    }

    public String getTitleString() {
        return toolbar.titleText.getText().toString();
    }

    public View addOptionsMenu(View view) {
        int padding = MonitorUtils.resizeByMonitor(this, 15, 750);
        toolbar.addFreeLinearView(toolbar.menuLayout, view,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        view.setPadding(padding, 0, padding, 0);
        return view;
    }

    public View addOptionsMenu(View view, int width, int height) {
        int padding = MonitorUtils.resizeByMonitor(this, 15, 750);
        toolbar.addFreeLinearView(toolbar.menuLayout, view,
                width + 30, height);
        view.setPadding(padding, 0, padding, 0);
        return view;
    }

//    public void onLogout() {
//        NaApiUtils.getInstance(activity).logout();
//    }

    public void setScreenBrightness(float b) {
        //取得window属性保存在layoutParams中
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.screenBrightness = b;//b已经除以10
        getWindow().setAttributes(layoutParams);
        //显示修改后的亮度
//        layoutParams = getWindow().getAttributes();
//        tView.setText(String.valueOf(layoutParams.screenBrightness));
    }

    public void post(Runnable r) {
        new Handler().post(r);
    }

    public void postDelayed(Runnable r, long delayMillis) {
        new Handler().postDelayed(r, delayMillis);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

//    public void confirmToLogin() {
//        confirmToLogin("請先登入會員。");
//    }
//
//    public void confirmToLogin(String message) {
//        NaConfirmDialog.showSelf(activity, message, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                if (i == DialogInterface.BUTTON_POSITIVE) {
//                    gotoLoginActivity();
//                }
//            }
//        });
//    }
//
//    private void gotoLoginActivity() {
//        Intent intent = new Intent(activity, NaLoginActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
//        overridePendingTransition(0, 0);
//    }

    protected boolean isDefaultLogoShow() {
        return false;
    }
}
