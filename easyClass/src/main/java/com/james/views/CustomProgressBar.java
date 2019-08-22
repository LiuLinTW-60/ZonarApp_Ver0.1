/*
 * Copyright 2012 Thinkermobile, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.james.views;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 可客製化自己的 progress bar，但是目前只支援長條狀的圖
 * 
 * @author JamesX
 * @since 2012/05/17
 */
public class CustomProgressBar extends RelativeLayout {
	private Context mContext;
	private int resId, resId2;
	private ImageView imageFG;

	private int valueMax, value;

	/**
	 * @param context
	 * @param resId (int) resource id 為 progress 的底圖
	 * @param resId2 (int) resource id 為 progress 的圖
	 */
	public CustomProgressBar(Context context, int resId, int resId2) {
		super(context);
		this.mContext = context;
		this.resId = resId;
		this.resId2 = resId2;

		this.setBackgroundResource(resId);

		imageFG = new ImageView(mContext);
		imageFG.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		imageFG.setBackgroundResource(resId2);
		this.addView(imageFG);
	}

	private void update() {
		imageFG.getLayoutParams().width = getWidth() * value / (valueMax == 0 ? 1 : valueMax);
		imageFG.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		imageFG.requestLayout();
	}

	public void setProgress(int valueMax, int value) {
		this.valueMax = valueMax;
		this.value = value;
		update();
	}

	public int getProgress(){
		return value;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		update();
	}
}
