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

package com.james.easyclass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.james.utils.LogUtils;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * <br> Chinese Explanation:
 * <br> 如果需要使用SD卡相關的動作，需 實體化  參數有filePath的建構子
 * <br>
 * <br> English Explanation:
 * <br> To use this class, ImageManager.initial must be called in the first place, otherwise null point exception might be caught.
 * @author JamesX
 * @since 2010/04/26
 */
public class ImageManager {
	private final String TAG = ImageManager.class.getSimpleName();
    private static ImageManager imageManager;
    private Context mContext;
    private final String SD_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    private String mPath;
	private File mFile;
    private Map<String, SoftReference<BitmapInfo>> cache = Collections.synchronizedMap(new HashMap<String, SoftReference<BitmapInfo>>());
    
    public static class BitmapInfo{
    	public static final int TYPE_FROM_SD_FILE = 0x1001;
    	public static final int TYPE_FROM_RES_ID = 0x1002;
    	public static final int TYPE_FROM_RES_NAME = 0x1003;
    	
    	public int type;
    	public Bitmap bitmap;
    	
    	public BitmapInfo(int type, Bitmap bitmap){
    		this.bitmap = bitmap;
    		this.type = type;
    	}
    }
    
	/**
	 * @deprecated
	 * @param context
	 * @return
	 */
	public static ImageManager getDefault(Context context){
		if(imageManager == null){
			imageManager = getInstance(context, null);
		}
		//
		init(null);
		//
		return imageManager;
	}
	
	/**
	 * 
	 */
	public static ImageManager getInstance(Context context){
		if(imageManager == null){
			imageManager = getInstance(context, null);
		}
		//
		init(null);
		//
		return imageManager;
	}
	
	/**
	 * @deprecated
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static ImageManager getDefault(Context context, String filePath){
		if(imageManager == null){
			imageManager = new ImageManager();
		}
		//
		imageManager.mContext = context;
		//
		init(filePath);
		//
		return imageManager;
	}
	
	/**
	 * 
	 * @param context
	 * @param filePath
	 * @return
	 */
	public static ImageManager getInstance(Context context, String filePath){
		if(imageManager == null){
			imageManager = new ImageManager();
		}
		//
		imageManager.mContext = context;
		//
		init(filePath);
		//
		return imageManager;
	}
	
	private static void init(String filePath){
		//
		if(filePath == null) filePath = "";
		
		if(!filePath.startsWith(imageManager.SD_PATH)){
			if(!filePath.startsWith("/")){
				filePath = "/"+filePath;
			}
			filePath = imageManager.SD_PATH+filePath;
		}
		LogUtils.i("ImageManager", "filePath: "+filePath);
		
		if(imageManager.mFile == null){
			imageManager.mPath = filePath;
			imageManager.mFile = new File(imageManager.mPath);
		}
		else if(!imageManager.mPath.equalsIgnoreCase(filePath)){
			imageManager.mPath = filePath;
			imageManager.mFile = new File(imageManager.mPath);
		}
	}
	
	public Bitmap get(int id){
		return get(String.valueOf(id));
	}
	
	public Bitmap get(String id){
		SoftReference<BitmapInfo> bitmapRef = cache.get(id);
		if(bitmapRef != null){
			if(bitmapRef.get() == null){
				cache.remove(id);
				return null;
			}
			
			Bitmap bitmap = bitmapRef.get().bitmap;
			if(bitmap != null && !bitmap.isRecycled()){
				return bitmap;
			}
			else{
				if(bitmapRef.get().type == BitmapInfo.TYPE_FROM_SD_FILE){
					return loadBitmapFromSD(true, id);
				}
				else if(bitmapRef.get().type == BitmapInfo.TYPE_FROM_RES_ID){
					return getBitmapById(true, Integer.parseInt(id));
				}
				else if(bitmapRef.get().type == BitmapInfo.TYPE_FROM_RES_NAME){
					return getBitmapByName(true, id);
				}
			}
		}
		return null;
	}
	
	public void put(int id, int type, Bitmap bitmap){
		put(String.valueOf(id), type, bitmap);
	}
	
	// TODO
	public void put(String id, int type, Bitmap bitmap){
		cache.put(id, new SoftReference<BitmapInfo>(new BitmapInfo(type, bitmap)));
	}
	
	public void recycle(Bitmap... bitmaps){
		for(Bitmap bitmap:bitmaps){
			if(bitmap == null) continue;
			
			Iterator<String> iterator = cache.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				Bitmap cacheBitmap = cache.get(key).get().bitmap;
				if(bitmap.equals(cacheBitmap)){
					cacheBitmap.recycle();
//					cacheBitmap = null;
//					cache.remove(key);
					break;
				}
			}
		}
	}
	
	public Bitmap reload(Bitmap bitmap){
		Log.v(TAG, "bitmap is null: "+(bitmap == null));
		if(bitmap == null) return null;
		Log.v(TAG, "bitmap.isRecycled(): "+(bitmap.isRecycled()));
		if(!bitmap.isRecycled()) return bitmap;
		
		Iterator<Entry<String, SoftReference<BitmapInfo>>> iterator = cache.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String, SoftReference<BitmapInfo>> entry = iterator.next();
			Bitmap cacheBitmap = entry.getValue().get().bitmap;
			if(bitmap.equals(cacheBitmap)){
				Log.v(TAG, "bitmap hit");
				return get(entry.getKey());
			}
		}
		return null;
	}
    
    /**
     * 將 Bitmap 轉成 byte
     * @param bitmap (Bitmap)
     * @return (byte[])
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap){
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	if(bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)){
    		return baos.toByteArray();
    	}
    	
    	return null;
    }
    
    /**
     * 將 Bytes 轉成 Bitmap
     * @param b (byte[])
     * @return (Bitmap)
     */
    public static Bitmap bytes2Bitmap(byte[] b){
    	return bytes2Bitamp(b, 1);
    }
    
    /**
     * 將 Bytes 轉成 Bitmap
     * @param b
     * @param sampleSize 取樣率，設置為2就是取圖片的1/4
     * @return
     */
    public static Bitmap bytes2Bitamp(byte[] b,int sampleSize){
    	if(b.length == 0){
    		return null;
    	}
    	BitmapFactory.Options o = new BitmapFactory.Options();
    	o.inSampleSize = sampleSize;
    	o.inPurgeable = true;
    	Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length,o);
    	Matrix matrix = new Matrix();
    	matrix.reset();
    	matrix.setRotate(90, bitmap.getWidth()/2, bitmap.getWidth()/2);
    	Bitmap bitmapRot = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    	bitmap.recycle();
    	return bitmapRot;
    }
  
    private static BitmapFactory.Options getOption(){
    	BitmapFactory.Options o = new BitmapFactory.Options();
    	o.inJustDecodeBounds = true;
    	return o;
    }
    /**
     * 圖片取樣
     * @param b 
     * @param width  寬超出範圍則以2的倍數取樣
     * @param height 長超出範圍則以2的倍數取樣
     * @return
     */
    public static int getSampleSize(byte[] b,int width,int height){
    	BitmapFactory.Options o = getOption();
    	BitmapFactory.decodeByteArray(b, 0, b.length,o);  	
    	return caculate(b,b.getClass(),o,width,height);    
    }
    /**
     * 圖片取樣
     * @param resId Drawable ID 
     * @param width  寬超出範圍則以2的倍數取樣
     * @param height 長超出範圍則以2的倍數取樣
     * @return
     */
    public int getSampleSize(int resId, int width, int height){
    	BitmapFactory.Options o = getOption();
    	BitmapFactory.decodeResource(mContext.getResources(), resId, o); 	
    	return caculate(mContext, resId, Integer.class, o, width, height);    
    }
    /**
     * 圖片取樣
     * @param pathName 圖檔路徑 
     * @param width  寬超出範圍則以2的倍數取樣
     * @param height 長超出範圍則以2的倍數取樣
     * @return
     */
    public static int getSampleSize(String pathName, int width, int height){
    	BitmapFactory.Options o = getOption();   
    	return caculate(pathName, pathName.getClass(), o, width, height);    
    }
    
    //計算圖片取樣率
    private static int caculate(Object obj,Class<?> classType,BitmapFactory.Options o,int width,int height){
    	return caculate(null, obj, classType, o, width, height);
	}
	private static int caculate(Context context, Object obj,
			Class<?> classType, BitmapFactory.Options o, int width, int height) {
		int sampleSize = 1;
		try {
			if (classType.equals(String.class)) {
				BitmapFactory.decodeFile((String) obj, o);
			} else if (classType.equals(byte[].class)) {
				BitmapFactory.decodeByteArray((byte[]) obj, 0,((byte[]) obj).length, o);
			} else if (classType.equals(Integer.class)) {
				BitmapFactory.decodeResource(context.getResources(),(Integer) obj, o);
			}

			int widthTmp = o.outWidth;
			int heightTmp = o.outHeight;
			while (true) {
				if (widthTmp < width || heightTmp < height)
					break;
				widthTmp /= 2;
				heightTmp /= 2;
				sampleSize += 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sampleSize;
	}
    
    /**
     * 拷貝圖片至SD卡中(用到SD card 請確認是否執行過 setDir)
	 * @param fileName (String) 圖片名稱 EX: "avator.png"
	 * @param bitmap (Bitmap) 圖片內容 
     * @param format (CompressFormat) 可選擇壓縮方式
     * @see initial(Context context, String filePath)
     */
	public void saveBitmapToSD(String fileName, final Bitmap bitmap, final CompressFormat format){
		if(mFile != null){
			if(!fileName.startsWith("/")){
				fileName = "/"+fileName;
			}
			Log.d(TAG, "saveBitmapToSD " + SD_PATH + mPath + fileName);
			if(!mFile.exists()){
				mFile.mkdirs();
			}
//			sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ mFilePath)));
			// 設定路徑
			String photoFileName = mFile.toString() + fileName;
			File photoFile = new File(photoFileName);
			try {
				// 設定圖片格式
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				if(bitmap.compress(format, 100, baos)){
					InputStream is = new ByteArrayInputStream(baos.toByteArray());
					
					FileOutputStream fos = new FileOutputStream(photoFile);
					byte[] buffer = new byte[8192];
					int count = 0;
					while((count = is.read(buffer, 0, buffer.length)) > 0){
						fos.write(buffer, 0, count);
					}
					fos.close();
					is.close();
					
					fos = null;
					is = null;
					Log.d(TAG, "photoFile copy OK");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			photoFile = null;
		}
		else{
			Log.e(TAG, "file path is null, please add code 'getDefault(...)'");
		}
	}
    
    /**
	 * 拷貝圖片至SD卡中
	 * @param fileName (String) 圖片名稱 EX: "avator.png"
	 * @param drawable (Drawable) 圖片內容
	 * @see initial(Context context, String filePath)
	 */
	public void saveDrawableToSD(String fileName, final Drawable drawable){
		if(mFile != null){
			if(!fileName.startsWith("/")){
				fileName = "/"+fileName;
			}
			Log.d(TAG, "saveDrawableToSD " + SD_PATH + mPath + fileName);
			if(!mFile.exists()){
				mFile.mkdirs();
			}
			// 設定路徑
			String photoFileName = mFile.toString() + fileName;
			File photoFile = new File(photoFileName);
			try {
				// 設定圖片格式
				Bitmap bitmap = Bitmap.createBitmap(
		        		drawable.getIntrinsicWidth(),
		                drawable.getIntrinsicHeight(),
		                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
		        Canvas canvas = new Canvas(bitmap);
		        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		        drawable.draw(canvas);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				InputStream is = new ByteArrayInputStream(baos.toByteArray());
				
				FileOutputStream fos = new FileOutputStream(photoFile);
				byte[] buffer = new byte[8192];
				int count = 0;
				while((count = is.read(buffer, 0, buffer.length)) > 0){
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
				
				fos = null;
				is = null;
				Log.d(TAG, "photoFile copy OK");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			photoFile = null;
		}
		else{
			Log.e(TAG, "file path is null, please add code 'getDefault(...)'");
		}
	}
    
    public Bitmap loadBitmapFromSD(String fileName) {
    	return loadBitmapFromSD(false, fileName, 1);
    }
    
    public Bitmap loadBitmapFromSD(String fileName, int scale) {
    	return loadBitmapFromSD(false, fileName, scale);
    }
	
	/**
	 * 從SD卡讀取圖片，相同於 loadBitmapFromSD(fileName, 1)
	 * @param fileName (String) 圖片名稱 EX: "avator.png"
	 * @return (Bitmap) 圖片內容
	 * @see initial(Context context, String filePath)
	 */
	public Bitmap loadBitmapFromSD(boolean skipcache, String fileName){
		return loadBitmapFromSD(skipcache, fileName, 1);
	}
	
	/**
	 * 從SD卡讀取圖片
	 * @param fileName (String) 圖片名稱 EX: "avator.png"
	 * @param scale (int) sample 的 size
	 * @return (Bitmap) 圖片內容
	 * @see initial(Context context, String filePath)
	 */
	public Bitmap loadBitmapFromSD(boolean skipcache, String fileName, int scale){
		if(!skipcache){
			Bitmap cacheBitmap = get(fileName);
			if(cacheBitmap != null && !cacheBitmap.isRecycled()) return cacheBitmap;
		}
		
		if(mFile != null){
			// 設定路徑 
			if(!fileName.startsWith("/")){
				fileName = "/"+fileName;
			}
			String photoFileName = mFile.toString() + fileName;
			File photoFile = new File(photoFileName);
			Log.d(TAG, "loadBitmapFromSD: " + photoFileName);
			if(photoFile.exists()){
				try {
					FileInputStream fis = new FileInputStream(photoFile);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					options.inSampleSize = scale;
					Bitmap bitmap = BitmapFactory.decodeStream(fis, null, options);
					fis.close();
					
					put(fileName, BitmapInfo.TYPE_FROM_SD_FILE, bitmap);
					return bitmap;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		Log.e(TAG, "file path is null, please add code 'getInstance(...)'");
		return null;
	}
    
    public Bitmap getBitmapById(int resId) {
    	return getBitmapById(false, resId, 1);
    }
    
    public Bitmap getBitmapById(int resId, int scale) {
    	return getBitmapById(false, resId, scale);
    }

	/**
	 * 從 resource id 去取得 bitmap，同等於 getBitmapFromId(resId, 1)
     * @param resId (int) resource id
     * @return 回傳 Bitmap
	 */
	public Bitmap getBitmapById(boolean skipcache, int resId) {
		return getBitmapById(skipcache, resId, 1);
	}
	
    /**
     * 從 resource id 去取得 bitmap
     * @param resId (int) resource id
     * @param scale (int) sample size
     * @return 回傳 Bitmap
     */
    public Bitmap getBitmapById(boolean skipcache, int resId, int scale) {
		if(!skipcache){
			Bitmap cacheBitmap = get(resId);
			if(cacheBitmap != null && !cacheBitmap.isRecycled()) return cacheBitmap;
		}
    	
    	BitmapFactory.Options opt = new BitmapFactory.Options();
    	opt.inPreferredConfig = Bitmap.Config.RGB_565;
    	opt.inPurgeable = true; 
    	opt.inInputShareable = true;
    	opt.inSampleSize = scale;
    	InputStream is = mContext.getResources().openRawResource(resId);
    	Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
    	
    	put(resId, BitmapInfo.TYPE_FROM_RES_ID, bitmap);
    	return bitmap;
    }
    
    public Bitmap getBitmapByName(String resName) {
    	return getBitmapByName(false, resName, 1);
    }
    
    public Bitmap getBitmapByName(String resName, int scale) {
    	return getBitmapByName(false, resName, scale);
    }

    /**
     * 從 resource name 去取得 bitmap，同等於 getBitmapFromName(resName, 1)
     * @param resName (String) resource name
     * @return 回傳 Bitmap
     */
    public Bitmap getBitmapByName(boolean skipcache, String resName) {
    	return getBitmapByName(skipcache, resName, 1);
    }
    
    /**
     * 從 resource name 去取得 bitmap
     * @param resName (String) resource name
     * @param scale (int) sample size
     * @return 回傳 Bitmap
     */
    public Bitmap getBitmapByName(boolean skipcache, String resName, int scale) {
    	if(!skipcache){
			Bitmap cacheBitmap = get(resName);
			if(cacheBitmap != null && !cacheBitmap.isRecycled()) return cacheBitmap;
    	}
		
    	int resId = mContext.getResources().getIdentifier(resName, "drawable", mContext.getPackageName());
    	BitmapFactory.Options opt = new BitmapFactory.Options();
    	opt.inPreferredConfig = Bitmap.Config.RGB_565;
    	opt.inPurgeable = true; 
    	opt.inInputShareable = true;
    	opt.inSampleSize = scale;
    	InputStream is = mContext.getResources().openRawResource(resId);
    	Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
    	
    	put(resId, BitmapInfo.TYPE_FROM_RES_NAME, bitmap);
    	return bitmap;
	}
    
    /**
     * 對 Bitmap 寬度及高度做 roundTo2N 的動作
     * @param bmp (Bitmap) 原圖，可被recycle，避免直接使用bmp = roundTo2N(bmp);
     * @return (Bitmap) 回傳 resize 之後的結果
     */
    public static Bitmap roundTo2N(Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		double w = getLog2(width*2);
		double h = getLog2(height*2);
		
		float scaleWidth = (float) Math.pow(2, (int) w) / width;
		float scaleHeight = (float) Math.pow(2, (int) h) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return bitmap;
	}

    /**
     * 對 Bitmap 寬度做 resize 的動作
     * @param bmp (Bitmap) 原圖
     * @param newWidth (int) 目標寬度
     * @param newHeight (int) 目標高度
     * @return (Bitmap) 回傳 resize 之後的結果
     */
    public static Bitmap resize(Bitmap bmp, final int newWidth, final int newHeight) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		float scaleWidth = (float) newWidth / width;
		float scaleHeight = (float) newHeight / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		bmp.recycle();
		return bitmap;
	}
    
    public static Bitmap resize(Bitmap bmp, final float scale) {
    	if(bmp == null) return bmp;
    	
		int width = bmp.getWidth();
		int height = bmp.getHeight();

		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
		return bitmap;
	}
	
	/**
	 * 將drawable 轉 bitmap
	 * @param drawable (Drawable) 輸入Drawable
	 * @return (Bitmap) 回傳Bitmap
	 */
	public Bitmap drawableToBitmap(final Drawable drawable) {
		Bitmap bitmap = Bitmap.createBitmap(
        		drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
	}
	
	/**
	 * 取 Log2
	 * @param num (int) 輸入值
	 * @return (int) 回傳取完 Log2 的值
	 */
	public static int getLog2(int num)
	{
		int value=0;
		int Quotient = num/2;
		
		while(Quotient > 1)
		{
			value++;
			Quotient = Quotient/2;
		}
		
		value++;
		return value;
	}
	
	/**
	 * 疊合圖片
	 * @param partsImage (Bitmap[]) 輸入 Bitmap 陣列組成一張圖
	 * @return (Bitmap)
	 */
	public static Bitmap overlayBitmaps(Bitmap partsImageBG, Bitmap partsImage, int left, int top, int right, int bottom){
		// 圖片合起來
		Bitmap partsImageAll = Bitmap.createBitmap(partsImageBG.getWidth(), partsImageBG.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(partsImageAll);
		if(partsImageBG != null){
			canvas.drawBitmap(partsImageBG, 0, 0, null);
			if(partsImage != null){
				Rect rec = new Rect();
				rec.left = left;
				rec.top = top;
				rec.right = right;
				rec.bottom = bottom;
				canvas.drawBitmap(partsImage, null, rec, null);
			}
		}
		return partsImageAll;
	}
	
	/**
	 * 產生 bitmap 圓角效果
	 * @param bitmap (Bitmap) 原圖，同等於getRoundCornerBitmap(bitmap, 7)
	 * @return 
	 * @see getRoundedCornerImage(Bitmap bitmap, float scaleArc)
	 */
	public static Bitmap getRoundedCornerImage(Bitmap bitmap){
		return getRoundedCornerImage(bitmap, 7);
	}
	
	/**
	 * 產生 bitmap 圓角效果
	 * @param bitmap (Bitmap) 原圖
	 * @param scaleArc (float) 圓角大小，愈小圓角愈大
	 * @return 
	 */
	public static Bitmap getRoundedCornerImage(Bitmap bitmap, float scaleArc) {
		
        if(bitmap == null) return null;
        
        Bitmap output = bitmap;
        if(!bitmap.isMutable()){
	        output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx=(float)bitmap.getWidth()/scaleArc;
        final float roundPy=(float)bitmap.getHeight()/scaleArc;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPy, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	public static Bitmap getOvalImage(Bitmap bitmap) {
		
        if(bitmap == null) return null;
        
        Bitmap output = bitmap;
        if(!bitmap.isMutable()){
	        output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        
        final int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        final float radius = size/2f;
        
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth()/2f, bitmap.getHeight()/2f, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }
}
