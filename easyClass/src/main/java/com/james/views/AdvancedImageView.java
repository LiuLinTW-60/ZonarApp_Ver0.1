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

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * 測試階段
 * @author JamesX
 *
 */
public class AdvancedImageView extends ImageView{
	protected Context mContext;
	protected int monitorWidth, monitorHeight;
	protected float monitorDensity;
	private final float BIAS_WIDTH = 428f;
	private final float BIAS_HEIGHT = 642;
	private AdvancedScaleType mAdvancedScaleType = AdvancedScaleType.CENTER;
	private int mViewWidth = 0, mViewHeight = 0;
	private int mBitmapWidth = 0, mBitmapHeight = 0;
	
	public enum  AdvancedScaleType{
		MATRIX,
		FIT_XY,
		FIT_START,
		FIT_CENTER,
		FIT_END,
		FIT_LEFT,
		FIT_TOP,
		FIT_BOTTOM,
		FIT_RIGHT,
		CENTER,
		CENTER_CROP,
		CENTER_INSIDE
	}

	public AdvancedImageView(Context context) {
		super(context);
		mContext = context;
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
		monitorDensity = dm.density;
	}

	public void setAdvancedScaleType(AdvancedScaleType advancedScaleType){
		mAdvancedScaleType = advancedScaleType;

//		mBitmapWidth = (int)(getDrawable().getIntrinsicWidth()/monitorDensity);
//		mBitmapHeight = (int)(getDrawable().getIntrinsicHeight()/monitorDensity);
		
		switch (advancedScaleType) {
		case MATRIX:
			setScaleType(ScaleType.MATRIX);
			break;
		case FIT_XY:
			setScaleType(ScaleType.FIT_XY);
			break;
		case FIT_START:
			setScaleType(ScaleType.FIT_START);
			break;
		case FIT_CENTER:
			setScaleType(ScaleType.FIT_CENTER);
			break;
		case FIT_END:
			setScaleType(ScaleType.FIT_END);
			break;
		case CENTER:
			setScaleType(ScaleType.CENTER);
			break;
		case CENTER_CROP:
			setScaleType(ScaleType.CENTER_CROP);
			break;
		case CENTER_INSIDE:
			setScaleType(ScaleType.CENTER_INSIDE);
			break;
		case FIT_TOP:
			float ratioTop = 1;
			if(mBitmapWidth != 0 && mBitmapWidth != -1) ratioTop = (float)mViewWidth/mBitmapWidth;
			Matrix matrixFitTop = new Matrix();
			float scaleTop = ratioTop*monitorWidth/monitorDensity/BIAS_WIDTH;
			matrixFitTop.setScale(scaleTop, scaleTop);
			setImageMatrix(matrixFitTop);
			setScaleType(ScaleType.MATRIX);
			break;
		case FIT_LEFT:
			float ratioLeft = 1;
			if(mBitmapHeight != 0 && mBitmapHeight != -1) ratioLeft = (float)mViewHeight/mBitmapHeight;
			Matrix matrixFitLeft = new Matrix();
			float scaleLeft = ratioLeft*monitorHeight/monitorDensity/BIAS_HEIGHT;
			matrixFitLeft.setScale(scaleLeft, scaleLeft);
			setImageMatrix(matrixFitLeft);
			setScaleType(ScaleType.MATRIX);
			break;
		case FIT_BOTTOM:
			float ratioBottom = 1;
			if(mBitmapWidth != 0 && mBitmapWidth != -1) ratioBottom = (float)mViewWidth/mBitmapWidth;
			Matrix matrixFitBottom = new Matrix();
			float scaleBottom = ratioBottom*monitorWidth/monitorDensity/BIAS_WIDTH;
			matrixFitBottom.postScale(scaleBottom, scaleBottom);
			matrixFitBottom.postTranslate(0, mViewHeight-mBitmapHeight*ratioBottom);
			setImageMatrix(matrixFitBottom);
			setScaleType(ScaleType.MATRIX);
			break;
		case FIT_RIGHT:
			float ratioRight = 1;
			if(mBitmapHeight != 0 && mBitmapHeight != -1) ratioRight = (float)mViewHeight/mBitmapHeight;
			Matrix matrixFitRight = new Matrix();
			float scaleRight = ratioRight*monitorHeight/monitorDensity/BIAS_HEIGHT;
			matrixFitRight.setScale(scaleRight, scaleRight);
			matrixFitRight.postTranslate(mViewWidth-mBitmapWidth*ratioRight, 0);
			setImageMatrix(matrixFitRight);
			setScaleType(ScaleType.MATRIX);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh){
		super.onSizeChanged(w, h, oldw, oldh);
		mViewWidth = w;
		mViewHeight = h;
		
		setAdvancedScaleType(mAdvancedScaleType);
	}
	
	@Override
	public void setImageBitmap(Bitmap bm){
		super.setImageBitmap(bm);
		mBitmapWidth = bm.getWidth();
		mBitmapHeight = bm.getHeight();
	}
	
	@Override
	public void setImageResource(int resId){
		super.setImageResource(resId);
		
		Bitmap bm = getBitmapFromId(resId);
		mBitmapWidth = bm.getWidth();
		mBitmapHeight = bm.getHeight();
	}
	
    private Bitmap getBitmapFromId(int resId) {
    	BitmapFactory.Options opt = new BitmapFactory.Options();
    	opt.inPreferredConfig = Bitmap.Config.RGB_565;
    	opt.inPurgeable = true; 
    	opt.inInputShareable = true;
    	opt.inSampleSize = 1;
    	InputStream is = mContext.getResources().openRawResource(resId);
    	
    	return BitmapFactory.decodeStream(is, null, opt);
    }
}
