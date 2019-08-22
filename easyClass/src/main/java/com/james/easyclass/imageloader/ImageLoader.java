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

package com.james.easyclass.imageloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ImageView;

import com.james.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 在背景讀取圖片，以節省畫面的lag感
 *
 * @author JamesX
 * @since 2013/02/02
 */
public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();

    public static final int NORMAL = 0x1000;
    public static final int ROUND_CORNER = 0x1001;
    public static final int OVAL = 0x1002;

    private Context context;

    private static ImageLoader instance;
    private MemoryCache memoryCache;
    private FileCache fileCache;
    private ExecutorService executorService;
    private int defaultResId;
    private OnBitmapDownloadListener mOnBitmapDownloadListener;
    private OnDownloadListener mOnDownloadListener;

    @Deprecated
    public interface OnBitmapDownloadListener {
        public void OnBitmapDownload(ImageView imageView, Bitmap bitmap);
    }

    public interface OnDownloadListener {
        public void onStart();

        public void onFinish(ImageView imageView, Bitmap bitmap);
    }

    /**
     * 取得 ImageLoader 的實體位置，若已經存在則不會再 new 新的
     *
     * @param context (Context)
     * @param resid   (int) 預設圖片
     * @return
     */
    public static ImageLoader getInstance(Context context, int resid) {
        if (instance == null) {
            instance = new ImageLoader(context, resid);
        }
        instance.defaultResId = resid;
        return instance;
    }

    /**
     * 建議使用 getInstance(...)，以減少記憶體的使用
     *
     * @param context
     * @param resid
     */
    public ImageLoader(Context context, int resid) {
        this.context = context;
        memoryCache = new MemoryCache();
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
        defaultResId = resid;
    }

    @Deprecated
    public void setOnBitmapDownloadListener(OnBitmapDownloadListener onBitmapDownloadListener) {
        mOnBitmapDownloadListener = onBitmapDownloadListener;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        mOnDownloadListener = onDownloadListener;
    }

    /**
     * 背景下載圖片
     *
     * @param url       (String)
     * @param imageView (ImageView)
     */
    public void displayImage(String url, ImageView imageView) {
        displayImage(false, url, imageView, true, 0.5f, NORMAL, defaultResId);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView) {
        displayImage(needOverwrite, url, imageView, true, 0.5f, NORMAL, defaultResId);
    }

    public void displayImage(String url, ImageView imageView, int shape) {
        displayImage(false, url, imageView, true, 0.5f, shape, defaultResId);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, int shape) {
        displayImage(needOverwrite, url, imageView, true, 0.5f, shape, defaultResId);
    }

    /**
     * @deprecated
     */
    public void DisplayImage(String url, ImageView imageView) {
        displayImage(url, imageView, true, 0.5f, NORMAL, defaultResId);
    }

    /**
     * 背景下載圖片
     *
     * @param url         (String)
     * @param imageView   (ImageView)
     * @param needSampled (boolean)
     */
    public void displayImage(String url, ImageView imageView, boolean needSampled) {
        displayImage(false, url, imageView, needSampled, 0.5f, NORMAL, defaultResId);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled) {
        displayImage(needOverwrite, url, imageView, needSampled, 0.5f, NORMAL, defaultResId);
    }

    /**
     * 背景下載圖片
     *
     * @param url         (String)
     * @param imageView   (ImageView)
     * @param needSampled (boolean)
     * @param scale       (float)
     */
    public void displayImage(String url, ImageView imageView, boolean needSampled, float scale) {
        displayImage(false, url, imageView, needSampled, scale, NORMAL, defaultResId);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled, float scale) {
        displayImage(needOverwrite, url, imageView, needSampled, scale, NORMAL, defaultResId);
    }

    /**
     * @deprecated
     */
    public void DisplayImage(String url, ImageView imageView, boolean needSampled) {
        displayImage(url, imageView, needSampled, 0.5f, NORMAL, defaultResId);
    }

    /**
     * 背景下載圖片
     *
     * @param url               (String)
     * @param imageView         (ImageView)
     * @param needSampled       (boolean)
     * @param defaultImageResid (int)
     */
    public void displayImage(String url, ImageView imageView, boolean needSampled, int defaultImageResid) {
        displayImage(false, url, imageView, needSampled, 0.5f, NORMAL, defaultImageResid);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled, int defaultImageResid) {
        displayImage(needOverwrite, url, imageView, needSampled, 0.5f, NORMAL, defaultImageResid);
    }

    /**
     * 背景下載圖片
     *
     * @param url               (String)
     * @param imageView         (ImageView)
     * @param needSampled       (boolean)
     * @param defaultImageResid (int)
     */
    public void displayImage(String url, ImageView imageView, boolean needSampled, float scale, int defaultImageResid) {
        displayImage(false, url, imageView, needSampled, scale, NORMAL, defaultImageResid);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled, float scale, int defaultImageResid) {
        displayImage(needOverwrite, url, imageView, needSampled, scale, NORMAL, defaultImageResid);
    }

    /**
     * @deprecated
     */
    public void DisplayImage(String url, ImageView imageView, boolean needSampled, int defaultImageResid) {
        displayImage(url, imageView, needSampled, 0.5f, NORMAL, defaultImageResid);
    }

    /**
     * 背景下載圖片
     *
     * @param url         (String)
     * @param imageView   (ImageView)
     * @param needSampled (boolean)
     * @param needConner  (boolean)
     */
    public void displayImage(String url, ImageView imageView, boolean needSampled, boolean needConner) {
        displayImage(false, url, imageView, needSampled, 0.5f, needConner);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled, boolean needConner) {
        displayImage(needOverwrite, url, imageView, needSampled, 0.5f, needConner);
    }

    /**
     * 背景下載圖片
     *
     * @param url         (String)
     * @param imageView   (ImageView)
     * @param needSampled (boolean)
     * @param needConner  (boolean)
     */
    public void displayImage(String url, ImageView imageView, boolean needSampled, float scale, boolean needConner) {
        displayImage(false, url, imageView, needSampled, scale, needConner);
    }

    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled, float scale, boolean needConner) {
        if (needConner) {
            displayImage(needOverwrite, url, imageView, needSampled, scale, ROUND_CORNER, defaultResId);
        } else {
            displayImage(needOverwrite, url, imageView, needSampled, scale, NORMAL, defaultResId);
        }
    }

    /**
     * @deprecated
     */
    public void DisplayImage(String url, ImageView imageView, boolean needSampled, boolean needConner) {
        if (needConner) {
            displayImage(url, imageView, needSampled, 0.5f, ROUND_CORNER, defaultResId);
        } else {
            displayImage(url, imageView, needSampled, 0.5f, NORMAL, defaultResId);
        }
    }

    public void displayImage(String url, ImageView imageView, boolean needSampled, float scale, int shape, int defaultImageResid) {
        displayImage(false, url, imageView, needSampled, scale, shape, defaultImageResid);
    }

    /**
     * 背景下載圖片
     *
     * @param url               (String)
     * @param imageView         (ImageView)
     * @param needSampled       (boolean)
     * @param needConner        (boolean)
     * @param defaultImageResid (int)
     */
    public void displayImage(boolean needOverwrite, String url, ImageView imageView, boolean needSampled, float scale, int shape, int defaultImageResid) {
        //
        defaultResId = defaultImageResid;
        if (url == null) {
            return;
        }
        //
        if (mOnDownloadListener != null)
            mOnDownloadListener.onStart();
        // from memoryCache
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null && !needOverwrite) {
            if (shape == NORMAL) {
                imageView.setImageBitmap(bitmap);
                if (mOnBitmapDownloadListener != null)
                    mOnBitmapDownloadListener.OnBitmapDownload(imageView, bitmap);
                if (mOnDownloadListener != null)
                    mOnDownloadListener.onFinish(imageView, bitmap);
            } else if (shape == ROUND_CORNER) {
                imageView.setImageBitmap(getRoundedCornerImage(bitmap));
                if (mOnBitmapDownloadListener != null)
                    mOnBitmapDownloadListener.OnBitmapDownload(imageView, bitmap);
                if (mOnDownloadListener != null)
                    mOnDownloadListener.onFinish(imageView, bitmap);
            } else if (shape == OVAL) {
                imageView.setImageBitmap(getOvalImage(bitmap));
                if (mOnBitmapDownloadListener != null)
                    mOnBitmapDownloadListener.OnBitmapDownload(imageView, bitmap);
                if (mOnDownloadListener != null)
                    mOnDownloadListener.onFinish(imageView, bitmap);
            }
        } else {
            if (needOverwrite) {
                deleteCacheFile(url);
            }

            File f = fileCache.getFile(url);
            // from SD cache
            bitmap = decodeFile(f, needSampled, scale);
            if (bitmap != null) {
                memoryCache.put(url, bitmap);
                if (shape == NORMAL) {
                    imageView.setImageBitmap(bitmap);
                    if (mOnBitmapDownloadListener != null)
                        mOnBitmapDownloadListener.OnBitmapDownload(imageView, bitmap);
                    if (mOnDownloadListener != null)
                        mOnDownloadListener.onFinish(imageView, bitmap);
                } else if (shape == ROUND_CORNER) {
                    imageView.setImageBitmap(getRoundedCornerImage(bitmap));
                    if (mOnBitmapDownloadListener != null)
                        mOnBitmapDownloadListener.OnBitmapDownload(imageView, bitmap);
                    if (mOnDownloadListener != null)
                        mOnDownloadListener.onFinish(imageView, bitmap);
                } else if (shape == OVAL) {
                    imageView.setImageBitmap(getOvalImage(bitmap));
                    if (mOnBitmapDownloadListener != null)
                        mOnBitmapDownloadListener.OnBitmapDownload(imageView, bitmap);
                    if (mOnDownloadListener != null)
                        mOnDownloadListener.onFinish(imageView, bitmap);
                }
            } else {
                imageView.setImageResource(defaultImageResid);
                queuePhoto(url, imageView, needSampled, scale, shape);
            }
        }
    }

    private void queuePhoto(String url, ImageView imageView, boolean needSample, float scale, int shape) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoaderRunnable(p, needSample, scale, shape));
    }

    public Bitmap getBitmap(String url, boolean needSampled) {
        return getBitmap(url, needSampled, 0.5f);
    }

    /**
     * 可以直接透過 url 下載網路圖片
     *
     * @param url         (String)
     * @param needSampled (boolean)
     * @return
     */
    public Bitmap getBitmap(String url, boolean needSampled, float scale) {
        File file = fileCache.getFile(url);

        // from SD cache
        if (file.exists()) {
            Bitmap bitmap = decodeFile(file, needSampled, scale);
            return bitmap;
        }

        // from web
        try {
            downloadBitmap(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // from SD cache
        Bitmap bitmap = decodeFile(file, needSampled, scale);
        return bitmap;
    }

    public void downloadBitmap(String url) throws Exception {
        File file = fileCache.getFile(url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                url.startsWith("https")) {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }
            }
            };

            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            URL imageUrl = new URL(url);
            HttpsURLConnection conn = (HttpsURLConnection) imageUrl.openConnection();
            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            Utils.CopyStream(is, os);
            os.close();
            is.close();
        } else {
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            Utils.CopyStream(is, os);
            os.close();
            is.close();
        }
    }

    // decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, boolean needSampled, float scale) {
        try {
            // decode image size
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, opt);
            if (needSampled) {
                bitmap = resize(bitmap, scale);
            }

            return bitmap;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }
    }

    private class PhotosLoaderRunnable implements Runnable {
        private PhotoToLoad photoToLoad;
        private boolean needSampled;
        private float scale;
        private int shape;

        public PhotosLoaderRunnable(PhotoToLoad photoToLoad, boolean needSampled, float scale, int shape) {
            this.photoToLoad = photoToLoad;
            this.needSampled = needSampled;
            this.scale = scale;
            this.shape = shape;
        }

        @Override
        public void run() {
            //
            Bitmap bmp = getBitmap(photoToLoad.url, needSampled, scale);
            if (bmp != null)
                memoryCache.put(photoToLoad.url, bmp);
            //
            BitmapDisplayerRunnable runnable = new BitmapDisplayerRunnable(bmp, photoToLoad, shape);
            Activity activity = (Activity) photoToLoad.imageView.getContext();
            activity.runOnUiThread(runnable);
        }
    }

    // Used to display bitmap in the UI thread
    private class BitmapDisplayerRunnable implements Runnable {
        private Bitmap bitmap;
        private PhotoToLoad photoToLoad;
        private int shape;

        public BitmapDisplayerRunnable(Bitmap bitmap, PhotoToLoad photoToLoad, int shape) {
            this.bitmap = bitmap;
            this.photoToLoad = photoToLoad;
            this.shape = shape;
        }

        public void run() {
            //
            if (bitmap != null) {
                if (shape == NORMAL) {
                    photoToLoad.imageView.setImageBitmap(bitmap);
                    if (mOnBitmapDownloadListener != null)
                        mOnBitmapDownloadListener.OnBitmapDownload(photoToLoad.imageView, bitmap);
                    if (mOnDownloadListener != null)
                        mOnDownloadListener.onFinish(photoToLoad.imageView, bitmap);
                } else if (shape == ROUND_CORNER) {
                    photoToLoad.imageView.setImageBitmap(getRoundedCornerImage(bitmap));
                    if (mOnBitmapDownloadListener != null)
                        mOnBitmapDownloadListener.OnBitmapDownload(photoToLoad.imageView, bitmap);
                    if (mOnDownloadListener != null)
                        mOnDownloadListener.onFinish(photoToLoad.imageView, bitmap);
                } else if (shape == OVAL) {
                    photoToLoad.imageView.setImageBitmap(getOvalImage(bitmap));
                    if (mOnBitmapDownloadListener != null)
                        mOnBitmapDownloadListener.OnBitmapDownload(photoToLoad.imageView, bitmap);
                    if (mOnDownloadListener != null)
                        mOnDownloadListener.onFinish(photoToLoad.imageView, bitmap);
                }
            } else {
                photoToLoad.imageView.setImageResource(defaultResId);

                if (mOnBitmapDownloadListener != null)
                    mOnBitmapDownloadListener.OnBitmapDownload(photoToLoad.imageView, bitmap);
                if (mOnDownloadListener != null)
                    mOnDownloadListener.onFinish(photoToLoad.imageView, bitmap);
            }
        }
    }

    public void asyncDownloadBitmaps(final ArrayList<String> urls) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                for (String url : urls) {
                    File file = fileCache.getFile(url);
                    // from SD cache
                    if (file.exists()) {
                        LogUtils.i(TAG, url + " has been cached.");
                        continue;
                    }

                    // from web
                    try {
                        downloadBitmap(url);
                        LogUtils.i(TAG, url + " has been downloaded complete.");
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtils.i(TAG, url + " has been downloaded fail.");
                    }
                }
                return null;
            }
        }.execute();
    }

    @Deprecated
    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    public void clearCacheFiles() {
        memoryCache.clear();
        fileCache.clear();
    }

    @Deprecated
    public void removeCache(String url) {
        memoryCache.remove(url);
        fileCache.remove(url);
    }

    public void deleteCacheFile(String url) {
        memoryCache.remove(url);
        fileCache.remove(url);
    }

    @Deprecated
    public void clearBitmap() {
        memoryCache.clear();
    }

    public void clearCaches() {
        memoryCache.clear();
    }

    public static Bitmap getRoundedCornerImage(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        // final float roundPx = 100;
        final float roundPx = (float) bitmap.getWidth() / 7f;
        final float roundPy = (float) bitmap.getHeight() / 7f;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPy, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getOvalImage(Bitmap bitmap) {
        if (bitmap == null)
            return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();

        final int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        final float radius = size / 2f;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmap.getWidth() / 2f, bitmap.getHeight() / 2f, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return output;
    }

    public Bitmap resize(Bitmap bmp, final float scale) {
        if (bmp == null)
            return bmp;

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        return bitmap;
    }
}
