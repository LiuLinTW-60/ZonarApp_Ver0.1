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
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * 功能強化的 gallery，可以改變gallery的效果，例如旋轉，放大縮小，漸層(漸層效果只有ImageView可以用)
 * @author JamesX
 *
 */
public class AdvancedGallery extends Gallery{
	private int monitorWidth;
	private int monitorHeight;
    private boolean isImageViewOnly;
	private Camera mCamera = new Camera();     // 用來做類3D效果處理,比如z軸方向上的平移,繞y軸的旋轉
    private int mMaxRotationAngle = 60;     // 圖片繞y軸最大旋轉角度,也就是螢幕最邊上那兩張圖片的旋轉角度
    private int mMaxZoom = -360;             // 圖片在z軸平移的距離,視覺上看起來就是放大縮小的效果.
    private int mCoveflowCenter;
    private boolean mAlphaMode = false;
    private boolean mScaleMode = false;
    private boolean mCircleYMode = false;
    private boolean mPyrMode = false;

    /**
     * AdvancedGallery 的建構子，adapter內容都是 ImageView 才使用這個，否則參考
     * AdvancedGallery(Context context, boolean isImageViewOnly)
     * @param context (Context)
     * @see AdvancedGallery(Context context, boolean isImageViewOnly)
     */
	public AdvancedGallery(Context context) {
		super(context);
		this.setStaticTransformationsEnabled(true);
		this.isImageViewOnly = true;
		
		setup();
	}
    
	/**
	 * AdvancedGallery 的建構子，adapter內容可為客製的 view ，但 isImageViewOnly 須設為false
	 * @param context (Context)
	 * @param isImageViewOnly (boolean) adapter內容都是 ImageView 才使用這個，否則設為false
	 */
	public AdvancedGallery(Context context, boolean isImageViewOnly) {
		super(context);
		this.setStaticTransformationsEnabled(true);
		this.isImageViewOnly = isImageViewOnly;
		
		setup();
	}
	
	private void setup(){
		setSpacing(0);
		
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
	}

	public int getMaxRotationAngle() {
        return mMaxRotationAngle;
    }
	
	/**
	 * 圖片繞y軸最大旋轉角度,也就是螢幕最邊上那兩張圖片的旋轉角度
	 * @param maxRotationAngle
	 */
	public void setMaxRotationAngle(int maxRotationAngle) {
        mMaxRotationAngle = maxRotationAngle;
    }
	
	public boolean getScaleMode() {
        return mScaleMode;
    }
	
	/**
	 * 開啟或關閉放大縮小效果
	 * @param isScale
	 */
	public void setScaleMode(boolean isScale) {
		mScaleMode = isScale;
    }
	
	public boolean getCircleYMode() {
        return mCircleYMode;
    }
	
	/**
	 * 開啟或關閉對 Y 軸旋轉的效果
	 * @param isCircle
	 */
	public void setCircleYMode(boolean isCircle) {
		mCircleYMode = isCircle;
    }
	
	public boolean getPyrMode() {
        return mPyrMode;
    }
	
	/**
	 * 開啟或關閉金字塔旋轉的效果
	 * @param isPyrMode
	 */
	public void setPyrMode(boolean isPyrMode) {
		mPyrMode = isPyrMode;
    }
	
	public boolean getAlphaMode() {
        return mAlphaMode;
    }
	
	/**
	 * 開啟或關閉漸層效果
	 * @param isAlpha
	 */
	public void setAlphaMode(boolean isAlpha) {
        mAlphaMode = isAlpha;
    }
	
	public int getMaxZoom() {
        return mMaxZoom;
    }
	
	/**
	 * maxZoom 圖片在z軸平移的距離,視覺上看起來就是放大縮小的效果，為負值(預設是-380)
	 * @param maxZoom
	 */
	public void setMaxZoom(int maxZoom) {
        mMaxZoom = maxZoom;
    }
	
	// 取得 AdvancedGallery 的中線
	private int getCenterOfCoverflow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }
	
	// 得到 view 的中線
	private int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }
	
	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
        final int childCenter = getCenterOfView(child);
        final int childWidth = child.getWidth();
        int rotationAngle = 0;
        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);
        if (Math.abs(childCenter-mCoveflowCenter)<childWidth/2) {
            transformImageBitmap(child, t, 0);
        } else {
//            rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);
//            if (Math.abs(rotationAngle) > mMaxRotationAngle) {
//                rotationAngle = (rotationAngle < 0) ? - mMaxRotationAngle : mMaxRotationAngle;
//            }
        	int rotate = childWidth/3;
        	if(childCenter> mCoveflowCenter){
        		transformImageBitmap(child, t, rotate);
        	}
        	else{
        		transformImageBitmap(child, t, -rotate);
        	}
        }
        return true;
    }
	
	/**
     * This is called during layout when the size of this view has changed. If
     * you were just added to the view hierarchy, you're called with the old
     * values of 0.
     *
     * @param w
     *            Current width of this view.
     * @param h
     *            Current height of this view.
     * @param oldw
     *            Old width of this view.
     * @param oldh
     *            Old height of this view.
     */
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCoveflowCenter = getCenterOfCoverflow();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
    /**
     * Transform the Image Bitmap by the Angle passed
     *
     * @param imageView
     *            ImageView the ImageView whose bitmap we want to rotate
     * @param t
     *            transformation
     * @param rotationAngle
     *            the Angle by which to rotate the Bitmap
     */
    private void transformImageBitmap(View child, Transformation t, int rotationAngle) {
        mCamera.save();
        final Matrix imageMatrix = t.getMatrix();
        final int imageHeight = child.getHeight();
        final int imageWidth = child.getWidth();
        final int rotation = Math.abs(rotationAngle);

    	// 漸層效果
        if (mAlphaMode && isImageViewOnly) {
        	((ImageView)child).setAlpha((int) (255 - rotation * 2.5));
        }
    	// 放大縮小功能
    	if(mScaleMode){
    		float translateZ = (float)((rotation*3));
            mCamera.translate(0f, 0f, translateZ);
    	}
    	// 金字塔旋轉功能
        if (mPyrMode) {
        	mCamera.translate(0.0f, -rotation, 0.0f);
        }
        // y軸旋轉功能
        if(mCircleYMode){
        	mCamera.rotateY(-rotationAngle);
        }
        mCamera.getMatrix(imageMatrix);
        imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));
        imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
        mCamera.restore();
    }
}
