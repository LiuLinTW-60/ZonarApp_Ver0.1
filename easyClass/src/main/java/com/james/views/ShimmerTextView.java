
package com.james.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;

public class ShimmerTextView extends ImageView {

	private Paint paint;
	private String mText;
	private int mTextColor;
	private float mTextSize;

	private Bitmap mBitmap;
	private Canvas mBitmapCanvas;
	private Rect windowsRect;
	private PorterDuffXfermode mMode;

	private int mMonitorWidth;

	private int mAnimationPeriod = 144;
	private int mWindowWidthDp = 35;
	private int mWindowColor = 0xffc1c1c1;

	private CompoundDrawable compoundDrawableLeft;
	private CompoundDrawable compoundDrawableRight;

	private Bitmap mCircleBitmap;

	public static class CompoundDrawable {
		private Context context;
		private float widthDp;
		private float heightDp;
		private float paddingLeftDp;
		private float paddingTopDp;
		private float paddingRightDp;
		private float paddingBottomDp;
		private Bitmap bitmap;

		public CompoundDrawable(Context context, int resid, float widthDp, float heightDp, float paddingDp) {
			this.context = context;
			this.widthDp = widthDp;
			this.heightDp = heightDp;
			this.paddingLeftDp = paddingDp;
			this.paddingTopDp = paddingDp;
			this.paddingRightDp = paddingDp;
			this.paddingBottomDp = paddingDp;

			this.bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
		}

		public CompoundDrawable(Context context, int resid, float widthDp, float heightDp, float paddingLeftDp, float paddingTopDp, float paddingRightDp, float paddingBottomDp) {
			this.context = context;
			this.widthDp = widthDp;
			this.heightDp = heightDp;
			this.paddingLeftDp = paddingLeftDp;
			this.paddingTopDp = paddingTopDp;
			this.paddingRightDp = paddingRightDp;
			this.paddingBottomDp = paddingBottomDp;

			this.bitmap = BitmapFactory.decodeResource(context.getResources(), resid);
		}

		public Rect getRect(int centerX, int centerY) {
			int left = centerX;
			int top = centerY - dp2px(context, heightDp) / 2;
			int right = left + dp2px(context, widthDp);
			int bottom = top + dp2px(context, heightDp);

			return new Rect(left, top, right, bottom);
		}

		public int getWidth() {
			return dp2px(context, widthDp);
		}

		public int getHeight() {
			return dp2px(context, heightDp);
		}

		public int getPaddingLeft() {
			return dp2px(context, paddingLeftDp);
		}

		public int getPaddingTop() {
			return dp2px(context, paddingTopDp);
		}

		public int getPaddingRight() {
			return dp2px(context, paddingRightDp);
		}

		public int getPaddingBottom() {
			return dp2px(context, paddingBottomDp);
		}
	}

	public ShimmerTextView(Context context) {
		super(context);

		setup();
	}

	public ShimmerTextView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setup();
	}

	public ShimmerTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setup();
	}

	private void setup() {
		paint = new Paint();
		mMode = new PorterDuffXfermode(Mode.SRC_IN);
		windowsRect = new Rect();

		mMonitorWidth = getMonitorWidth(getContext());

		setWindowColor(mWindowColor);
	}

	public void setText(String text) {
		mText = text;
	}

	public void setText(int resid) {
		mText = getContext().getResources().getString(resid);
	}

	public String getText() {
		return mText;
	}

	public void setTextColor(int color) {
		mTextColor = color;
	}

	public void setTextSize(float textSize) {
		mTextSize = textSize;
	}

	public void setCompoundDrawablesLeft(int resid, float widthDp, float heightDp) {
		compoundDrawableLeft = new CompoundDrawable(getContext(), resid, widthDp, heightDp, 0);
	}

	public void setCompoundDrawablesLeft(int resid, float widthDp, float heightDp, float paddingDp) {
		compoundDrawableLeft = new CompoundDrawable(getContext(), resid, widthDp, heightDp, paddingDp);
	}

	public void setCompoundDrawablesLeft(int resid, float widthDp, float heightDp, float paddingLeftDp, float paddingTopDp, float paddingRightDp, float paddingBottomDp) {
		compoundDrawableLeft = new CompoundDrawable(getContext(), resid, widthDp, heightDp, paddingLeftDp, paddingTopDp, paddingRightDp, paddingBottomDp);
	}

	public void setCompoundDrawablesRight(int resid, float widthDp, float heightDp) {
		compoundDrawableRight = new CompoundDrawable(getContext(), resid, widthDp, heightDp, 0);
	}

	public void setCompoundDrawablesRight(int resid, float widthDp, float heightDp, float paddingDp) {
		compoundDrawableRight = new CompoundDrawable(getContext(), resid, widthDp, heightDp, paddingDp);
	}

	public void setCompoundDrawablesRight(int resid, float widthDp, float heightDp, float paddingLeftDp, float paddingTopDp, float paddingRightDp, float paddingBottomDp) {
		compoundDrawableRight = new CompoundDrawable(getContext(), resid, widthDp, heightDp, paddingLeftDp, paddingTopDp, paddingRightDp, paddingBottomDp);
	}

	public void setAnimationPeriod(int animationPeriod) {
		mAnimationPeriod = animationPeriod;
	}

	public void setWindowWidthDp(int windowWidthDp) {
		mWindowWidthDp = windowWidthDp;
	}

	public void setWindowColor(int windowColor) {
		mWindowColor = windowColor;

		if (mCircleBitmap != null && !mCircleBitmap.isRecycled()) {
			mCircleBitmap.recycle();
			mCircleBitmap = null;
		}

		mCircleBitmap = Bitmap.createBitmap(64, 64, Config.ARGB_8888);
		Canvas canvas = new Canvas(mCircleBitmap);
		canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
		paint.reset();
		paint.setShader(new RadialGradient(32, 32, 32, mWindowColor, mTextColor, TileMode.MIRROR));
		canvas.drawCircle(32, 32, 34, paint);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (getWidth() == 0 || getHeight() == 0) {
			return;
		}

		if (mBitmap == null ||
				getWidth() != mBitmap.getWidth() ||
				getHeight() != mBitmap.getHeight()) {
			mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);

			mBitmapCanvas = new Canvas(mBitmap);
		}

		mBitmapCanvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);

		// draw text and icons
		paint.reset();
		paint.setAntiAlias(true);
		paint.setColor(mTextColor);
		paint.setTextSize(sp2px(getContext(), mTextSize));
		FontMetrics fontMetrics = paint.getFontMetrics();
		float fontHeight = fontMetrics.bottom - fontMetrics.top;
		float textBaseY = getHeight() - (getHeight() - fontHeight) / 2 - fontMetrics.bottom;
		if (compoundDrawableLeft != null) {
			mBitmapCanvas.drawBitmap(compoundDrawableLeft.bitmap, null, compoundDrawableLeft.getRect(compoundDrawableLeft.getPaddingLeft(), (getHeight() + compoundDrawableLeft.getPaddingTop() - compoundDrawableLeft.getPaddingBottom()) / 2), paint);
			mBitmapCanvas.drawText(getText(), compoundDrawableLeft.getPaddingLeft() + compoundDrawableLeft.getWidth() + compoundDrawableLeft.getPaddingRight(), textBaseY, paint);

			if (compoundDrawableRight != null) {
				int centerX = (int) (compoundDrawableRight.getPaddingLeft() + compoundDrawableLeft.getWidth() + compoundDrawableLeft.getPaddingRight() + paint.measureText(mText) + compoundDrawableRight.getPaddingLeft());
				mBitmapCanvas.drawBitmap(compoundDrawableRight.bitmap, null, compoundDrawableRight.getRect(centerX, (getHeight() + compoundDrawableRight.getPaddingTop() - compoundDrawableRight.getPaddingBottom()) / 2), paint);
			}
		}
		else {
			mBitmapCanvas.drawText(getText(), 0, textBaseY, paint);

			if (compoundDrawableRight != null) {
				int centerX = (int) (paint.measureText(mText) + compoundDrawableRight.getPaddingLeft());
				mBitmapCanvas.drawBitmap(compoundDrawableRight.bitmap, null, compoundDrawableRight.getRect(centerX, (getHeight() + compoundDrawableRight.getPaddingTop() - compoundDrawableRight.getPaddingBottom()) / 2), paint);
			}
		}

		// run shimmer window
		windowsRect.left = windowsRect.left + mMonitorWidth / mAnimationPeriod;
		if (windowsRect.left >= mMonitorWidth) {
			windowsRect.left = -dp2px(getContext(), mWindowWidthDp);
		}
		windowsRect.top = 0;
		windowsRect.right = windowsRect.left + dp2px(getContext(), mWindowWidthDp);
		windowsRect.bottom = getHeight();

		paint.reset();
		paint.setAntiAlias(true);
		// paint.setColor(mWindowColor);
		paint.setXfermode(mMode);
		// mBitmapCanvas.drawCircle(windowsRect.centerX(), windowsRect.centerY(), windowsRect.width() / 2, paint);
		if (mCircleBitmap != null && !mCircleBitmap.isRecycled()) {
			mBitmapCanvas.drawBitmap(mCircleBitmap, null, windowsRect, paint);
		}

		setImageBitmap(mBitmap);

		postInvalidateDelayed(500, 0, 0, getWidth(), getHeight());
	}

	public void refresh() {
		paint.reset();
		paint.setAntiAlias(true);
		paint.setColor(mTextColor);
		paint.setTextSize(sp2px(getContext(), mTextSize));
		FontMetrics fontMetrics = paint.getFontMetrics();
		float fontHeight = fontMetrics.bottom - fontMetrics.top;

		if (getLayoutParams().width == LayoutParams.WRAP_CONTENT) {
			getLayoutParams().width = (int) (mText == null ? 0 : paint.measureText(mText));

			if (compoundDrawableLeft != null) {
				getLayoutParams().width += compoundDrawableLeft.getWidth();
			}
			if (compoundDrawableRight != null) {
				getLayoutParams().width += compoundDrawableRight.getWidth();
			}

			if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
				getLayoutParams().height = (int) (fontHeight);

				if (compoundDrawableLeft != null) {
					getLayoutParams().height = Math.max(getLayoutParams().height, compoundDrawableLeft.getHeight());
				}
				if (compoundDrawableRight != null) {
					getLayoutParams().height = Math.max(getLayoutParams().height, compoundDrawableRight.getHeight());
				}

				// TODO
			}
		}

		requestLayout();
	}

	public static int getMonitorWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static float sp2px(Context context, Float sp) {
		float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		return scaledDensity * sp;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		postInvalidateDelayed(500);
	}

}
