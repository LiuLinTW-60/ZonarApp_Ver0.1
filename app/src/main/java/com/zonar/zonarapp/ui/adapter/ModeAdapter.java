package com.zonar.zonarapp.ui.adapter;

import android.content.Context;
import android.view.View;

import com.james.views.FreeAdapter;
import com.zonar.zonarapp.ui.adapter.item.ModeItem;

import java.util.ArrayList;

public class ModeAdapter extends FreeAdapter<String, ModeItem> {

    public ModeAdapter(Context context, ArrayList<String> arrayList) {
        super(context, arrayList);
    }

    @Override
    public ModeItem initView(int position) {
        return new ModeItem(getContext());
    }

    @Override
    public void setView(int position, ModeItem modeItem) {
        if(getItem(position) == null) {
            modeItem.addIcon.setVisibility(View.VISIBLE);
            modeItem.itemText.setText("增加新的模式");
            modeItem.setMargin(modeItem.itemText, 20, 0, 0, 0);
        } else {
            modeItem.addIcon.setVisibility(View.GONE);
            modeItem.itemText.setText(getItem(position));
            modeItem.setMargin(modeItem.itemText, 0, 0, 0, 0);
        }
    }
}
