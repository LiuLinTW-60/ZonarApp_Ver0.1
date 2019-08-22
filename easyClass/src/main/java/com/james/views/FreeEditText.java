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
import android.widget.EditText;

/**
 * 適用於各解析度的 edittext
 * 
 * @author JamesX
 */
public class FreeEditText extends EditText {
	private Context mContext;
	private float sdtDensity = 1.5f;
	private int picSize = 750;

	public FreeEditText(Context context) {
		super(context);
		mContext = context;

		setPadding(0, 0, 0, 0);
	}

	/**
	 * 預設螢幕畫面是640*960
	 * 
	 * @param _picSize (int) 預設為640
	 */
	public void setPicSize(int _picSize) {
		picSize = _picSize;
	}

	/**
	 * 設定字的高度(in sp)，一種sp適應所有解析度(寬高設為WRAP_CONTENT時建議使用)
	 * 
	 * @param spValue
	 */
	public void setTextSizeFitSp(float spValue) {
		int monitorWidth = mContext.getResources().getDisplayMetrics().widthPixels;

		float pxValue = spValue * sdtDensity * monitorWidth / picSize;
		float sp = px2sp(pxValue);
		super.setTextSize(sp);
	}

	/**
	 * 設定字的高度(in pixel)(寬高為固定時建議使用)
	 * 
	 * @param pxValue
	 */
	public void setTextSizeFitPx(float pxValue) {
		float sp = px2sp(pxValue);
		sp--;
		super.setTextSize(sp);
	}

	/**
	 * 設定字的高度，可設定任何數值，會隨手機解析度縮放
	 * 
	 * @param pxValue
	 */
	public void setTextSizeFitResolution(float value) {
		setTextSizeFitResolution(value, picSize);
	}

	/**
	 * 設定字的高度，可設定任何數值，會隨手機解析度縮放
	 * 
	 * @param pxValue
	 * @param picWidth
	 */
	public void setTextSizeFitResolution(float value, int picWidth) {
		int monitorWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		setTextSizeFitPx(value * monitorWidth / picWidth);
	}

	/**
	 * 將 pixel 轉為 scaled pixel
	 */
	private float px2sp(float pxValue) {
		final float density = mContext.getResources().getDisplayMetrics().scaledDensity;
		return pxValue / density / 1.3f;
	}
}
