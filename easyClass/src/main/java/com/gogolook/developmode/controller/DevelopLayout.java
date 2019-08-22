
package com.gogolook.developmode.controller;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DevelopLayout extends RelativeLayout {
	public RelativeLayout topLayout;
	public TextView titleText;
	public TextView subTitleText;
	public Button nextButton;
	public LinearLayout itemLayout;

	public DevelopLayout(Context context) {
		super(context);
		this.setBackgroundColor(0xff313131);

		topLayout = new RelativeLayout(context);
		topLayout.setId(0x1001);
		topLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) topLayout.getLayoutParams()).addRule(ALIGN_PARENT_TOP);
		topLayout.setPadding(15, 5, 15, 5);
		this.addView(topLayout);

		LinearLayout titleLayout = new LinearLayout(context);
		titleLayout.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) titleLayout.getLayoutParams()).addRule(CENTER_VERTICAL);
		titleLayout.setOrientation(LinearLayout.VERTICAL);
		topLayout.addView(titleLayout);

		titleText = new TextView(context);
		titleText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		titleText.setTextColor(Color.WHITE);
		titleText.setTextSize(18);
		titleText.setGravity(Gravity.CENTER_VERTICAL);
		titleLayout.addView(titleText);

		subTitleText = new TextView(context);
		subTitleText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		subTitleText.setTextColor(Color.GRAY);
		subTitleText.setTextSize(14);
		subTitleText.setGravity(Gravity.CENTER_VERTICAL);
		subTitleText.setVisibility(GONE);
		titleLayout.addView(subTitleText);

		nextButton = new Button(context);
		nextButton.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		((RelativeLayout.LayoutParams) nextButton.getLayoutParams()).addRule(ALIGN_PARENT_RIGHT);
		((RelativeLayout.LayoutParams) nextButton.getLayoutParams()).addRule(CENTER_VERTICAL);
		nextButton.setTextColor(Color.WHITE);
		nextButton.setTextSize(14);
		nextButton.setGravity(Gravity.CENTER_VERTICAL);
		nextButton.setText("Continue");
		nextButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		nextButton.setBackgroundColor(Color.TRANSPARENT);
		topLayout.addView(nextButton);

		View line = new View(context);
		line.setId(0x1002);
		line.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
		((RelativeLayout.LayoutParams) line.getLayoutParams()).addRule(BELOW, topLayout.getId());
		line.setBackgroundColor(0xff005599);
		this.addView(line);

		ScrollView scrollView = new ScrollView(context);
		scrollView.setId(0x1003);
		scrollView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		((RelativeLayout.LayoutParams) scrollView.getLayoutParams()).addRule(BELOW, line.getId());
		scrollView.setPadding(15, 5, 15, 5);
		this.addView(scrollView);

		itemLayout = new LinearLayout(context);
		itemLayout.setLayoutParams(new ScrollView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		itemLayout.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(itemLayout);

	}

}
