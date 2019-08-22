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
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 貼紙功能，目前只支援貼紙的移動與定點貼功能
 * @author JamesX
 * @since 2012/05/17
 */
public class TouchAreaView extends RelativeLayout{
	private Context context;

	public TouchAreaView(Context context) {
		super(context);
		this.context = context;
		
		setTouchAreaView();
	}

	private void setTouchAreaView() {
		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		this.setDrawingCacheEnabled(true);
	}
	
	/**
	 * 定點貼紙功能
	 * @param bitmap (Bitmap) 貼紙的圖
	 * @param x (float) 貼紙的x座標
	 * @param y (float) 貼紙的y座標
	 * @param width (int) 貼紙的寬(自訂) 若不想自訂可以直接使用 addSimpleSticker(bitmap, x, y)
	 * @param height (int) 貼紙的長(自訂) 若不想自訂可以直接使用 addSimpleSticker(bitmap, x, y)
	 * @see addSimpleSticker(final Bitmap bitmap, final float x, final float y)
	 */
	public void addSimpleSticker(final Bitmap bitmap, final float x, final float y, final int width, final int height){
		final ImageView imageSticker = new ImageView(context);
		imageSticker.setLayoutParams(new LayoutParams(width, height));
		((RelativeLayout.LayoutParams)imageSticker.getLayoutParams()).setMargins((int)x-width/2, (int)y-height/2, 0, 0);
		imageSticker.setImageBitmap(bitmap);
		this.addView(imageSticker);
	}
	
	/**
	 * 定點貼紙功能
	 * @param bitmap (Bitmap) 貼紙的圖
	 * @param x (float) 貼紙的x座標
	 * @param y (float) 貼紙的y座標
	 * @see addSimpleSticker(final Bitmap bitmap, final float x, final float y, final int width, final int height)
	 */
	public void addSimpleSticker(final Bitmap bitmap, final float x, final float y){
		addSimpleSticker(bitmap, x, y, bitmap.getWidth(), bitmap.getHeight());
	}
	
	/**
	 * 增加可移動的貼紙功能
	 * @param bitmap bitmap (Bitmap) 貼紙的圖
	 * @param width (int) 貼紙的寬(自訂) 若不想自訂可以直接使用 addMovingSticker(bitmap)
	 * @param height (int) 貼紙的長(自訂) 若不想自訂可以直接使用 addMovingSticker(bitmap)
	 * @see addMovingSticker(final Bitmap bitmap)
	 */
	public void addMovingSticker(final Bitmap bitmap, final int width, final int height){
		final ImageView imageSticker = new ImageView(context);
		imageSticker.setLayoutParams(new LayoutParams(width, height));
		((RelativeLayout.LayoutParams)imageSticker.getLayoutParams()).setMargins((int)getWidth()/2, (int)getHeight()/2, 0, 0);
		imageSticker.setImageBitmap(bitmap);
		this.addView(imageSticker);
		
		imageSticker.setOnTouchListener(new OnTouchListener(){
			int lastX, lastY;
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					imageSticker.setBackgroundColor(0xccff0000);
					lastX = (int)event.getRawX();
					lastY = (int)event.getRawY();
				}
				else if(event.getAction() == MotionEvent.ACTION_MOVE){
					int dx = (int)event.getRawX()-lastX;
					int dy = (int)event.getRawY()-lastY;
					int left = view.getLeft()+dx;
					int top = view.getTop()+dy;
					int right = view.getRight()+dx;
					int bottom = view.getBottom()+dy;
					view.layout(left, top, right, bottom);
					view.postInvalidate();
					
					lastX = (int)event.getRawX();
					lastY = (int)event.getRawY();
				}
				else if(event.getAction() == MotionEvent.ACTION_UP){
					((RelativeLayout.LayoutParams)view.getLayoutParams()).setMargins(view.getLeft(), view.getTop(), 0, 0);
					imageSticker.setBackgroundColor(0x00000000);
				}
				return true;
			}
		});
	}
	
	/**
	 * 增加可移動的貼紙功能
	 * @param bitmap bitmap (Bitmap) 貼紙的圖
	 * @see addMovingSticker(final Bitmap bitmap, final int width, final int height)
	 */
	public void addMovingSticker(final Bitmap bitmap){
		addMovingSticker(bitmap, bitmap.getWidth(), bitmap.getHeight());
	}
}
