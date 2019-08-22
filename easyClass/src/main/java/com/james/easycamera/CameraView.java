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

package com.james.easycamera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

/**
 * 這是一個簡易的 camera 照相工具，照相方法為takePicture()，利用addImageView(imageiew)，
 * 或是addHandler(handler)做後續的處理
 * @author JamesX
 *
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = "CameraView";
	public static final int PORTRAIT = 90;
	public static final int LANSCAPE = 0;
	private int mOrientation = PORTRAIT;
	
	private Context mContext;
	private Camera camera;
	private OnPictureTakenListener mOnPictureTakenListener;
	public boolean isRunning = false;
	private int monHeight;
	private int monWidth;
	private DisplayMetrics dm;
	
	private int sampleSize = 1;
	
	public interface OnPictureTakenListener{
		public void onPictureTaken(Bitmap bitmap);
	}
	
	private class HandlePhotoTask extends AsyncTask<byte[], String, String> {
		Bitmap bitmap;
		@Override
		protected String doInBackground(byte[]... jpeg) {
			int size = jpeg.length*jpeg[0].length;
			byte[] b = new byte[size];
			int k = 0;
			for(int i=0; i<jpeg[0].length; i++){
				for(int j=0; j<jpeg.length; j++){
					b[k] = jpeg[j][i];
					k++;
				}
			}
			bitmap = bytes2Bitmap(b);
			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(mOnPictureTakenListener != null){
				mOnPictureTakenListener.onPictureTaken(bitmap);
			}
			isRunning = false;
		}
	}
	
	private Bitmap bytes2Bitmap(byte[] b){
    	if(b.length == 0){
    		return null;
    	}
    	
//    	BitmapFactory.Options o = new BitmapFactory.Options();
//    	o.inJustDecodeBounds = true;
//    	BitmapFactory.decodeByteArray(b, 0, b.length,o);
//    	int widthTmp = o.outWidth;
//		int heightTmp = o.outHeight;
//		int sampleSize = 1;
//		while (true) {
//			if (widthTmp < monWidth || heightTmp < monHeight)
//				break;
//			widthTmp /= 2;
//			heightTmp /= 2;
//			sampleSize += 1;
//		}
    	
		BitmapFactory.Options o2 = new BitmapFactory.Options();
    	o2.inSampleSize = sampleSize;
    	Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length, o2);
    	Matrix matrix = new Matrix();
    	matrix.reset();
    	matrix.setRotate(mOrientation, bitmap.getWidth()/2, bitmap.getWidth()/2);
    	bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    	return bitmap;
    }

	/**
	 * CameraView 的建構子
	 * @param context
	 */
	public CameraView(Context context) {
		super(context);
		this.mContext = context;
		this.getHolder().addCallback(this);
		this.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		getMonitor();
	}
	
	/**
	 * CameraView 的建構子
	 * @param context
	 */
	public CameraView(Context context, int orientation) {
		super(context);
		this.mContext = context;
		this.mOrientation = orientation;
		this.getHolder().addCallback(this);
		this.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		getMonitor();
	}
	
	public void setSampleSize(int sampleSize){
		this.sampleSize = sampleSize;
	}
	
	private void getMonitor(){
		dm = new DisplayMetrics();
		((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		monWidth = dm.widthPixels;
		monHeight = dm.heightPixels;
	}
	
	/**
	 * 執行此功能會拍照，利用 OnPictureTakenListener 加入後續處理的動作
	 */
	public void takePicture(OnPictureTakenListener onPictureTakenListener){
		if(!isRunning){
			this.mOnPictureTakenListener = onPictureTakenListener;
			
			isRunning = true;
			camera.autoFocus(autoFocusCallback);
		}
	}
	
	private ShutterCallback shutterCallback = new ShutterCallback(){
		@Override
		public void onShutter() {
		}
	};
	
	private PictureCallback rawPictureCallback = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] arg0, Camera arg1) {
		}
	};
	
	private PictureCallback jpegPictureCallback = new PictureCallback(){
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			new HandlePhotoTask().execute(data);
			camera.startPreview();
		}
	};
	
	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if(success) 
				camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
			else isRunning = false;
		}
	};
	
	/**
	 * 開啟鏡頭的 preview
	 */
	public void openCamera(){
		openCamera(PORTRAIT);
	}
	
	public void openCamera(int orientation){
		if(camera == null){
			camera = Camera.open();
			if(orientation == PORTRAIT){
				camera.setDisplayOrientation(orientation);
			}
			try {
				camera.setPreviewDisplay(getHolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 關閉鏡頭的 preview並釋放
	 */
	public void closeCamera(){
		if(camera != null){
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(camera == null){
			camera = Camera.open();
			camera.setDisplayOrientation(mOrientation);
			try {
				camera.setPreviewDisplay(getHolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if(camera != null){
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if(camera != null){
			Camera.Parameters parameters = camera.getParameters();
	
			Size previewSize = null;
			int longSide = Math.max(Math.round(dm.heightPixels * dm.density), Math.round(dm.widthPixels * dm.density));
			int shortSide = Math.min(Math.round(dm.heightPixels * dm.density), Math.round(dm.widthPixels * dm.density));
	
			previewSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), longSide, shortSide);
			if (previewSize != null) {
				parameters.setPreviewSize(previewSize.width, previewSize.height);
				Log.d(TAG, "set camera preview size=" + previewSize.width + ", " + previewSize.height);
			}
	
			List<Size> sizes = parameters.getSupportedPictureSizes();
			Size picSize  = getOptimalPreviewSize(sizes, longSide, shortSide);
			parameters.setPictureSize(picSize.width, picSize.height);
			
			parameters.setPictureFormat(PixelFormat.JPEG);
			camera.setParameters(parameters);
			camera.startPreview();
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		double targetRatio = (double) w / h;
		if(sizes == null) return null;

		Size optimalSize = null;
		double minDiff = Double.MAX_VALUE;

		int targetHeight = h;

		// Try to find an size match aspect ratio and size
		for(Size size : sizes) {
			double ratio = (double) size.width / size.height;
			if(Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) {
				continue;
			}
			if(Math.abs(size.height - targetHeight) < minDiff) {
				optimalSize = size;
				minDiff = Math.abs(size.height - targetHeight);
			}
		}

		// Cannot find the one match the aspect ratio, ignore the requirement
		if(optimalSize == null) {
			minDiff = Double.MAX_VALUE;
			for(Size size : sizes) {
				if (Math.abs(size.height - targetHeight) < minDiff) {
					optimalSize = size;
					minDiff = Math.abs(size.height - targetHeight);
				}
			}
		}

		return optimalSize;
	}
}