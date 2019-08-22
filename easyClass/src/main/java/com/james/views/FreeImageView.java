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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * <br>
 * 增加一些ScaleType的ImageView，新增的ScaleType必須使用FreeScaleType <br>
 * 包含MATCH_TOP, MATCH_BOTTOM, MATCH_LEFT, MATCH_RIGHT， <br>
 * 詳見setScaleType(FreeScaleType freeScaleType)方法
 * 
 * @author JamesX
 */
public class FreeImageView extends ImageView {

	private FreeScaleType mScaleType;

	private int mCurrentResid;
	private Bitmap mCurrentBitmap;

	public FreeImageView(Context context) {
		super(context);

		mScaleType = FreeScaleType.CENTER;
		setScaleType(mScaleType);
	}

	public enum FreeScaleType {
		CENTER,
		CENTER_CROP,
		CENTER_INSIDE,
		FIT_CENTER,
		FIT_END,
		FIT_START,
		FIT_XY,
		MATRIX,
		// 以下為新增
		MATCH_TOP,
		MATCH_BOTTOM,
		MATCH_LEFT,
		MATCH_RIGHT,
	}

	/**
	 * <br>
	 * 新增MATCH_TOP, MATCH_BOTTOM, MATCH_LEFT, MATCH_RIGHT四種ScaleType， <br>
	 * MATCH_TOP 效果為將圖片等比例縮放至寬度相同，並將圖片移置最上方，而最下方若超出畫面會有延伸效果不會被擠壓 <br>
	 * MATCH_BOTTOM 效果為將圖片等比例縮放至寬度相同，並將圖片移置最下方，而最上方若超出畫面會有延伸效果不會被擠壓 <br>
	 * MATCH_LEFT 效果為將圖片等比例縮放至高度相同，並將圖片移置最左方，而最右方若超出畫面會有延伸效果不會被擠壓 <br>
	 * MATCH_RIGHT 效果為將圖片等比例縮放至高度相同，並將圖片移置最右方，而最左方若超出畫面會有延伸效果不會被擠壓
	 * 
	 * @param freeScaleType (FreeScaleType) 新增MATCH_TOP, MATCH_BOTTOM, MATCH_LEFT, MATCH_RIGHT
	 */
	public void setScaleType(FreeScaleType freeScaleType) {
		mScaleType = freeScaleType;
		switch (freeScaleType) {
			case CENTER:
				super.setScaleType(ScaleType.CENTER);
				break;
			case CENTER_CROP:
				super.setScaleType(ScaleType.CENTER_CROP);
				break;
			case CENTER_INSIDE:
				super.setScaleType(ScaleType.CENTER_INSIDE);
				break;
			case FIT_CENTER:
				super.setScaleType(ScaleType.FIT_CENTER);
				break;
			case FIT_END:
				super.setScaleType(ScaleType.FIT_END);
				break;
			case FIT_START:
				super.setScaleType(ScaleType.FIT_START);
				break;
			case FIT_XY:
				super.setScaleType(ScaleType.FIT_XY);
				break;
			case MATRIX:
				super.setScaleType(ScaleType.MATRIX);
				break;
			default:
				super.setScaleType(ScaleType.CENTER);
				break;
		}

		postInvalidate();
	}

	@Override
	public void setImageResource(int resId) {
		if (mCurrentResid == resId) {
			return;
		}
		mCurrentBitmap = null;

		if (mScaleType == FreeScaleType.MATCH_TOP) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			InputStream is = getContext().getResources().openRawResource(resId);
			Bitmap bm = BitmapFactory.decodeStream(is, null, options);

			setImageBitmap(bm);
		}
		else if (mScaleType == FreeScaleType.MATCH_BOTTOM) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			InputStream is = getContext().getResources().openRawResource(resId);
			Bitmap bm = BitmapFactory.decodeStream(is, null, options);

			setImageBitmap(bm);
		}
		else if (mScaleType == FreeScaleType.MATCH_LEFT) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			InputStream is = getContext().getResources().openRawResource(resId);
			Bitmap bm = BitmapFactory.decodeStream(is, null, options);

			setImageBitmap(bm);
		}
		else if (mScaleType == FreeScaleType.MATCH_RIGHT) {
			BitmapFactory.Options options = new BitmapFactory.Options();
			InputStream is = getContext().getResources().openRawResource(resId);
			Bitmap bm = BitmapFactory.decodeStream(is, null, options);

			setImageBitmap(bm);
		}
		else {
			super.setImageResource(resId);
		}
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		if (mScaleType == FreeScaleType.MATCH_TOP) {
			mCurrentBitmap = bm;
		}
		else if (mScaleType == FreeScaleType.MATCH_BOTTOM) {
			mCurrentBitmap = bm;
		}
		else if (mScaleType == FreeScaleType.MATCH_LEFT) {
			mCurrentBitmap = bm;
		}
		else if (mScaleType == FreeScaleType.MATCH_RIGHT) {
			mCurrentBitmap = bm;
		}
		else {
			super.setImageBitmap(bm);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mCurrentBitmap != null && !mCurrentBitmap.isRecycled()) {
			if (w <= 0 || h <= 0) {
				return;
			}

			if (mScaleType == FreeScaleType.MATCH_TOP) {
				Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
				Matrix matrix = new Matrix();
				matrix.setScale(bitmap.getWidth() / (float) mCurrentBitmap.getWidth(), bitmap.getWidth() / (float) mCurrentBitmap.getWidth());
				canvas.drawBitmap(mCurrentBitmap, matrix, new Paint());

				super.setImageBitmap(bitmap);
			}
			else if (mScaleType == FreeScaleType.MATCH_BOTTOM) {
				Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
				Matrix matrix = new Matrix();
				matrix.postScale(bitmap.getWidth() / (float) mCurrentBitmap.getWidth(), bitmap.getWidth() / (float) mCurrentBitmap.getWidth());
				matrix.postTranslate(0, bitmap.getHeight() - mCurrentBitmap.getHeight() * bitmap.getWidth() / (float) mCurrentBitmap.getWidth());
				canvas.drawBitmap(mCurrentBitmap, matrix, new Paint());

				super.setImageBitmap(bitmap);
			}
			else if (mScaleType == FreeScaleType.MATCH_LEFT) {
				Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
				Matrix matrix = new Matrix();
				matrix.postScale(bitmap.getHeight() / (float) mCurrentBitmap.getHeight(), bitmap.getHeight() / (float) mCurrentBitmap.getHeight());
				canvas.drawBitmap(mCurrentBitmap, matrix, new Paint());

				super.setImageBitmap(bitmap);
			}
			else if (mScaleType == FreeScaleType.MATCH_RIGHT) {
				Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
				Canvas canvas = new Canvas(bitmap);
				canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
				Matrix matrix = new Matrix();
				matrix.postScale(bitmap.getHeight() / (float) mCurrentBitmap.getHeight(), bitmap.getHeight() / (float) mCurrentBitmap.getHeight());
				matrix.postTranslate(bitmap.getWidth() - mCurrentBitmap.getWidth() * bitmap.getHeight() / (float) mCurrentBitmap.getHeight(), 0);
				canvas.drawBitmap(mCurrentBitmap, matrix, new Paint());

				super.setImageBitmap(bitmap);
			}
		}
	}

	public void setImageSizeInDp(int width_dp, int height_dp) {
		LayoutParams layoutParams = getLayoutParams();
		if (layoutParams == null) {
			layoutParams = new LayoutParams(dip2px(getContext(), width_dp), dip2px(getContext(), height_dp));
		}
		else {
			layoutParams.width = dip2px(getContext(), width_dp);
			layoutParams.height = dip2px(getContext(), height_dp);
		}
		postInvalidate();
	}

	/**
	 * 根據手機的分辨率以 dp 的單位 轉成為 px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根據手機的分辨率以 px 的單位 轉成為 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
