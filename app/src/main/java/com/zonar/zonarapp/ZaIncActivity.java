package com.zonar.zonarapp;

import android.os.Bundle;

import com.zonar.zonarapp.ui.layout.ZaContactLayout;
import com.zonar.zonarapp.ui.layout.ZaIncLayout;

public class ZaIncActivity extends ZaBaseActivity {

    private ZaIncLayout zaIncLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();

        findView();

        setView();

        setListener();
    }

    private void setLayout() {
        setTitle("XROUND Inc.");
        getToolbar().showBackButton();

        zaIncLayout = new ZaIncLayout(this);
        setContentView(zaIncLayout);
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
