package com.james.views;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.james.views.FreeLayout;

public class CursorEditText extends FreeLayout{
	private EditText editText;
	private float sdtDensity = 1.5f;
	private int picSize = 750;

	public CursorEditText(Context context, int layoutId, int editId) {
		super(context);
		setFreeLayoutWW();
		setPicSize(picSize, 960, TO_WIDTH);
		setPadding(0, 0, 0, 0);
		
		final LayoutInflater  inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = (View)inflater.inflate(layoutId, null);
		
		this.addFreeView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
				new int[]{CENTER_IN_PARENT});
		view.setPadding(0, 0, 0, 0);
		
		editText = (EditText)findViewById(editId);
		editText.setBackgroundColor(Color.TRANSPARENT);
		editText.setPadding(0, 0, 0, 0);
	}
	
	public EditText getEditText(){
		return editText;
	}
	
	public void setText(String text){
		editText.setText(text);
	}
	
	public Editable getText(){
		return editText.getText();
	}
	
	public void setGravity(int gravity){
		editText.setGravity(gravity);
	}
	
	public void setTextColor(int color){
		editText.setTextColor(color);
	}
	
	public void setHint(CharSequence hint){
		editText.setHint(hint);
	}
	
	public void setSingleLine(boolean singleLine){
		editText.setSingleLine(singleLine);
	}
	
	public void setInputType(int type){
		editText.setInputType(type);
	}
	
	public void setHint(String hint){
		editText.setHint(hint);
	}
	
	public void setHint(int resid){
		editText.setHint(resid);
	}
	
	/**
	 * 預設螢幕畫面是640*960
	 * @param _picSize (int) 預設為640
	 */
	public void setPicSize(int _picSize){
		picSize = _picSize;
	}

	/**
	 * 設定字的高度(in sp)，一種sp適應所有解析度(寬高設為WRAP_CONTENT時建議使用)
	 * @param spValue
	 */
	public void setTextSizeFitSp(float spValue){
		int monitorWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		
		float pxValue = spValue*sdtDensity*monitorWidth/picSize;
		float sp = px2sp(pxValue);
		editText.setTextSize(sp);
	}
	
	/**
	 * 設定字的高度(in pixel)(寬高為固定時建議使用)
	 * @param pxValue
	 */
	public void setTextSizeFitPx(float pxValue){
		float sp = px2sp(pxValue);
		sp--;
		editText.setTextSize(sp);
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
