package com.zonar.zonarapp;

import android.os.Bundle;

import com.zonar.zonarapp.ui.layout.ZaIncLayout;
import com.zonar.zonarapp.ui.layout.ZaUpdateLayout;

public class ZaUpdateActivity extends ZaBaseActivity {

    private ZaUpdateLayout zaUpdateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();

        findView();

        setView();

        setListener();
    }

    private void setLayout() {
        setTitle("韌體更新");
        getToolbar().showBackButton();

        zaUpdateLayout = new ZaUpdateLayout(this);
        setContentView(zaUpdateLayout);
    }

    private void findView() {

    }

    private void setView() {

    }

    private void setListener() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_right_out);
    }
}
