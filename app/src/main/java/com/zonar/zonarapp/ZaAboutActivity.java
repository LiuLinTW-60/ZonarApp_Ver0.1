package com.zonar.zonarapp;

import android.os.Bundle;

import com.zonar.zonarapp.ui.layout.ZaAboutLayout;

public class ZaAboutActivity extends ZaBaseActivity {

    private ZaAboutLayout zaAboutLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayout();

        findView();

        setView();

        setListener();
    }

    private void setLayout() {
        setTitle("關於產品");
        getToolbar().showBackButton();

        zaAboutLayout = new ZaAboutLayout(this);
        setContentView(zaAboutLayout);
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
