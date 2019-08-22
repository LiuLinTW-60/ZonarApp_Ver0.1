
package com.james.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class XmlBuilder {

	private static XmlBuilder instance;
	private Context mContext;
	protected DisplayMetrics dm;
	protected int monitorWidth, monitorHeight;
	protected int windowSize;
	protected int picSize = 640;
	private boolean mIsAutoAdjustText = false;

	public static int VERTICAL = LinearLayout.VERTICAL;
	public static int HORIZONTAL = LinearLayout.HORIZONTAL;

	public static final int TO_WIDTH = 0x1000;
	public static final int TO_HEIGHT = 0x1001;

	private View mView;

	public static XmlBuilder getInstance(Context context) {

		if (instance == null) {
			instance = new XmlBuilder(context);
		}

		instance.setup();
		return instance;
	}

	public XmlBuilder(Context context) {
		mContext = context;
	}

	private void setup() {
		dm = new DisplayMetrics();
		((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
		windowSize = monitorWidth;

		mIsAutoAdjustText = false;
	}

	public View inflate(int resid) {
		LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = layoutInflater.inflate(resid, null);

		mView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
		return mView;
	}

	public View inflate(LayoutInflater inflater, ViewGroup container, int resid) {
		mView = inflater.inflate(resid, container, false);

		startAdjustView();
		return mView;
	}

	private OnGlobalLayoutListener mOnGlobalLayoutListener = new OnGlobalLayoutListener() {

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		@Override
		public void onGlobalLayout() {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				mView.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
			}
			else {
				mView.getViewTreeObserver().removeGlobalOnLayoutListener(mOnGlobalLayoutListener);
			}
			// TODO
		}
	};

	private void startAdjustView() {
		adjustView(mView);
		if (mView instanceof ViewGroup) {
			adjustRecursively(mView);
		}
		mView.postInvalidate();
	}

	private void adjustRecursively(View view) {
		for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
			View childView = ((ViewGroup) view).getChildAt(i);
			adjustView(childView);

			if (childView instanceof ViewGroup) {
				adjustRecursively(childView);
			}
		}
	}

	private void adjustView(View view) {
		Log.v(getClass().getSimpleName(), "adjustView");

		LayoutParams layoutParams = view.getLayoutParams();
		int width = layoutParams.width;
		Log.v(getClass().getSimpleName(), "layoutParams.width: " + layoutParams.width);
		if (width != LayoutParams.WRAP_CONTENT &&
				width != LayoutParams.MATCH_PARENT) {
			layoutParams.width = width * windowSize / picSize;
		}

		int height = layoutParams.height;
		Log.v(getClass().getSimpleName(), "layoutParams.height: " + layoutParams.height);
		if (height != LayoutParams.WRAP_CONTENT &&
				height != LayoutParams.MATCH_PARENT) {
			layoutParams.height = height * windowSize / picSize;
		}

		int paddingLeft = view.getPaddingLeft() * windowSize / picSize;
		int paddingTop = view.getPaddingTop() * windowSize / picSize;
		int paddingRight = view.getPaddingRight() * windowSize / picSize;
		int paddingBottom = view.getPaddingBottom() * windowSize / picSize;
		view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);

		if (layoutParams instanceof RelativeLayout.LayoutParams) {
			((RelativeLayout.LayoutParams) layoutParams).leftMargin = ((RelativeLayout.LayoutParams) layoutParams).leftMargin * windowSize / picSize;
			((RelativeLayout.LayoutParams) layoutParams).topMargin = ((RelativeLayout.LayoutParams) layoutParams).topMargin * windowSize / picSize;
			((RelativeLayout.LayoutParams) layoutParams).rightMargin = ((RelativeLayout.LayoutParams) layoutParams).rightMargin * windowSize / picSize;
			((RelativeLayout.LayoutParams) layoutParams).bottomMargin = ((RelativeLayout.LayoutParams) layoutParams).bottomMargin * windowSize / picSize;
		}
		if (layoutParams instanceof LinearLayout.LayoutParams) {
			((LinearLayout.LayoutParams) layoutParams).leftMargin = ((LinearLayout.LayoutParams) layoutParams).leftMargin * windowSize / picSize;
			((LinearLayout.LayoutParams) layoutParams).topMargin = ((LinearLayout.LayoutParams) layoutParams).topMargin * windowSize / picSize;
			((LinearLayout.LayoutParams) layoutParams).rightMargin = ((LinearLayout.LayoutParams) layoutParams).rightMargin * windowSize / picSize;
			((LinearLayout.LayoutParams) layoutParams).bottomMargin = ((LinearLayout.LayoutParams) layoutParams).bottomMargin * windowSize / picSize;
		}
		if (layoutParams instanceof FrameLayout.LayoutParams) {
			((FrameLayout.LayoutParams) layoutParams).leftMargin = ((FrameLayout.LayoutParams) layoutParams).leftMargin * windowSize / picSize;
			((FrameLayout.LayoutParams) layoutParams).topMargin = ((FrameLayout.LayoutParams) layoutParams).topMargin * windowSize / picSize;
			((FrameLayout.LayoutParams) layoutParams).rightMargin = ((FrameLayout.LayoutParams) layoutParams).rightMargin * windowSize / picSize;
			((FrameLayout.LayoutParams) layoutParams).bottomMargin = ((FrameLayout.LayoutParams) layoutParams).bottomMargin * windowSize / picSize;
		}

		if (mIsAutoAdjustText) {
			if (view instanceof TextView) {
				TextView textView = (TextView) view;
				float pxValue = textView.getTextSize();
				float sp = px2sp(pxValue * windowSize / picSize);
				sp--;
				textView.setTextSize(sp);
			}
			if (view instanceof Button) {
				Button button = (Button) view;
				float pxValue = button.getTextSize();
				float sp = px2sp(pxValue * windowSize / picSize);
				sp--;
				button.setTextSize(sp);
			}
			if (view instanceof EditText) {
				EditText editText = (EditText) view;
				float pxValue = editText.getTextSize();
				float sp = px2sp(pxValue * windowSize / picSize);
				sp--;
				editText.setTextSize(sp);
			}
		}
	}

	public XmlBuilder setAutoAdjustText(boolean isAutoAdjustText) {
		mIsAutoAdjustText = isAutoAdjustText;

		return this;
	}

	public Context getContext() {
		return mContext;
	}

	public XmlBuilder setPicSize(int picWidth) {
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;

		windowSize = monitorWidth;
		if (picWidth != -1) {
			this.picSize = picWidth;
		}

		return this;
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * 預設螢幕畫面是640*960，以螢幕寬度為基底 <br>
	 * <br>
	 * English Explanation: <br>
	 * Default size is 640*960, take monitor width as default base to scale.
	 * 
	 * @param picWidth (int) 預設為640
	 * @param picHeight (int) 預設為960
	 * @param toWich (int) 可以設定為 TO_WIDTH 或是 TO_HEIGHT
	 */
	public XmlBuilder setPicSize(int picWidth, int picHeight, int toWich) {
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;

		if (toWich == TO_WIDTH) {
			windowSize = monitorWidth;
			if (picWidth != -1) {
				this.picSize = picWidth;
			}
		} else if (toWich == TO_HEIGHT) {
			windowSize = monitorHeight;
			if (picHeight != -1) {
				this.picSize = picHeight;
			}
		}

		return this;
	}

	/**
	 * 將 pixel 轉為 scaled pixel
	 */
	private float px2sp(float pxValue) {
		final float density = mContext.getResources().getDisplayMetrics().scaledDensity;
		return pxValue / density;
	}
}
