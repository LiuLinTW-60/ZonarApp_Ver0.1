package com.zonar.zonarapp;

import android.os.Bundle;

import com.zonar.zonarapp.ui.layout.ZaContactLayout;

public class ZaContactActivity extends ZaBaseActivity {

    private ZaContactLayout zaContactLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();

        findView();

        setView();

        setListener();
    }

    private void setLayout() {
        setTitle("聯絡我們");
        getToolbar().showBackButton();

        zaContactLayout = new ZaContactLayout(this);
        setContentView(zaContactLayout);
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
