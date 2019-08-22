
package com.james.views;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.james.easyclass.imageloader.ImageLoader;

public class XmlFreeLayout extends FreeLayout {
	private static final String TAG = XmlFreeLayout.class.getSimpleName();

	private ProgressBar progressBar;

	private Stack<View> mLayoutStack = new Stack<View>();
	private HashMap<View, String> mOnClickEvents = new HashMap<View, String>();

	private OnXmlClickListener mOnXmlClickListener;

	public static interface OnXmlClickListener {
		public void onClick(View v, String functionName);
	}

	public XmlFreeLayout(Context context) {
		super(context);

		setup();
	}

	public void setup() {
		removeAllViews();
		this.setBackgroundColor(0xaa000000);

		mLayoutStack.clear();

		progressBar = (ProgressBar) this.addFreeView(new ProgressBar(getContext()),
				120, 120,
				new int[] {
					CENTER_IN_PARENT
				});
		progressBar.setVisibility(VISIBLE);
	}

	public void dismissProgress() {
		if (progressBar != null) {
			progressBar.setVisibility(GONE);
			removeView(progressBar);
		}
	}

	public void setXmlString(String xmlString) {
		if (progressBar != null) {
			progressBar.setVisibility(GONE);
			removeView(progressBar);
		}
		try {
			InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes("utf-8"));
			parseXml(inputStream);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void setXmlResId(int xmlResid) {
		if (progressBar != null) {
			progressBar.setVisibility(GONE);
			removeView(progressBar);
		}
		InputStream inputStream = getContext().getResources().openRawResource(xmlResid);
		parseXml(inputStream);
	}

	private void parseXml(InputStream inputStream) {
		mLayoutStack.add(this);

		XmlPullParser pullParser = Xml.newPullParser();

		try {
			pullParser.setInput(inputStream, "utf-8"); // 設定語系
			// 利用eventType來判斷目前分析到XML是哪一個部份

			int eventType = pullParser.getEventType();
			// XmlPullParser.END_DOCUMENT表示已經完成分析XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// XmlPullParser.START_TAG表示目前分析到的是XML的Tag，如<title>
				if (eventType == XmlPullParser.START_TAG) {

					String viewName = pullParser.getName();
					Log.v(TAG, "START_TAG: " + viewName);

					HashMap<String, String> paramsMap = new HashMap<String, String>();
					for (int i = 0; i < pullParser.getAttributeCount(); i++) {
						String attributeName = pullParser.getAttributeName(i);
						String attributeValue = pullParser.getAttributeValue(i);
						Log.v(TAG, attributeName + " = " + attributeValue);

						paramsMap.put(attributeName, attributeValue);
					}

					addParsedView(viewName, paramsMap);
				}
				else if (eventType == XmlPullParser.END_TAG) {

					String viewClass = pullParser.getName();
					Log.v(TAG, "END_TAG: " + viewClass);

					if (equalToLayout(viewClass, mLayoutStack.peek())) {
						mLayoutStack.pop();
					}
				}

				// 分析下一個XML Tag
				eventType = pullParser.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
	}

	private void addParsedView(String viewClassName, HashMap<String, String> paramsMap) {
		View currentLayout = mLayoutStack.peek();

		int width = LayoutParams.WRAP_CONTENT;
		if (paramsMap.get("layout_width").equalsIgnoreCase("match_parent")) {
			width = LayoutParams.MATCH_PARENT;
		}
		else if (paramsMap.get("layout_width").equalsIgnoreCase("wrap_content")) {
			width = LayoutParams.WRAP_CONTENT;
		}
		else {
			width = parseSize(paramsMap, "layout_width");
		}

		int height = LayoutParams.WRAP_CONTENT;
		if (paramsMap.get("layout_height").equalsIgnoreCase("match_parent")) {
			height = LayoutParams.MATCH_PARENT;
		}
		else if (paramsMap.get("layout_height").equalsIgnoreCase("wrap_content")) {
			height = LayoutParams.WRAP_CONTENT;
		}
		else {
			height = parseSize(paramsMap, "layout_height");
		}

		View view = newView(viewClassName, paramsMap);
		if (isLayout(view)) {
			mLayoutStack.push(view);
		}

		if (currentLayout instanceof FreeLayout) {
			((FreeLayout) currentLayout).addFreeView(
					view,
					width, height);
		}
		else if (currentLayout instanceof RelativeLayout) {
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
			if (paramsMap.containsKey("layout_alignParentBottom")) {
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			}
			if (paramsMap.containsKey("layout_alignParentTop")) {
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			}
			if (paramsMap.containsKey("layout_alignParentLeft")) {
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			}
			if (paramsMap.containsKey("layout_alignParentRight")) {
				layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			}
			if (paramsMap.containsKey("layout_centerInParent")) {
				layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			}
			if (paramsMap.containsKey("layout_centerHorizontal")) {
				layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
			}
			if (paramsMap.containsKey("layout_centerVertical")) {
				layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
			}
			//
			if (paramsMap.containsKey("layout_toLeftOf")) {
				int id = Math.abs(toId(paramsMap.get("layout_toLeftOf")).hashCode());
				layoutParams.addRule(LEFT_OF, id);
			}
			if (paramsMap.containsKey("layout_toRightOf")) {
				int id = Math.abs(toId(paramsMap.get("layout_toRightOf")).hashCode());
				layoutParams.addRule(RIGHT_OF, id);
			}
			if (paramsMap.containsKey("layout_above")) {
				int id = Math.abs(toId(paramsMap.get("layout_above")).hashCode());
				layoutParams.addRule(ABOVE, id);
			}
			if (paramsMap.containsKey("layout_below")) {
				int id = Math.abs(toId(paramsMap.get("layout_below")).hashCode());
				layoutParams.addRule(BELOW, id);
			}
			if (paramsMap.containsKey("layout_alignLeft")) {
				int id = Math.abs(toId(paramsMap.get("layout_alignLeft")).hashCode());
				layoutParams.addRule(ALIGN_LEFT, id);
			}
			if (paramsMap.containsKey("layout_alignRight")) {
				int id = Math.abs(toId(paramsMap.get("layout_alignRight")).hashCode());
				layoutParams.addRule(ALIGN_RIGHT, id);
			}
			if (paramsMap.containsKey("layout_alignTop")) {
				int id = Math.abs(toId(paramsMap.get("layout_alignTop")).hashCode());
				layoutParams.addRule(ALIGN_TOP, id);
			}
			if (paramsMap.containsKey("layout_alignBottom")) {
				int id = Math.abs(toId(paramsMap.get("layout_alignBottom")).hashCode());
				layoutParams.addRule(ALIGN_BOTTOM, id);
			}

			view.setLayoutParams(layoutParams);
			((RelativeLayout) currentLayout).addView(view);
		}
		else if (currentLayout instanceof LinearLayout) {
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, height);
			if (paramsMap.containsKey("layout_weight")) {
				float layout_weight = Float.parseFloat(paramsMap.get("layout_weight"));
				layoutParams.weight = layout_weight;
			}

			view.setLayoutParams(layoutParams);
			((LinearLayout) currentLayout).addView(view);
		}
		else if (currentLayout instanceof FrameLayout) {
			FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
			view.setLayoutParams(layoutParams);
			((FrameLayout) currentLayout).addView(view);
		}

		setViewPadding(view, paramsMap);

		setViewMargin(view, paramsMap);

		setViewVisibility(view, paramsMap);
	}

	private View newView(String viewClassName, final HashMap<String, String> paramsMap) {
		View view;

		if (viewClassName.equalsIgnoreCase(RelativeLayout.class.getSimpleName())) {
			view = new RelativeLayout(getContext());
		}
		else if (viewClassName.equalsIgnoreCase(LinearLayout.class.getSimpleName())) {
			view = new LinearLayout(getContext());
			if (paramsMap.containsKey("orientation")) {
				if (paramsMap.get("orientation").equalsIgnoreCase("vertical")) {
					((LinearLayout) view).setOrientation(LinearLayout.VERTICAL);
				}
				else {
					((LinearLayout) view).setOrientation(LinearLayout.HORIZONTAL);
				}
			}
			if (paramsMap.containsKey("gravity")) {
				((LinearLayout) view).setGravity(getViewGravity(paramsMap));
			}
		}
		else if (viewClassName.equalsIgnoreCase(FrameLayout.class.getSimpleName())) {
			view = new FrameLayout(getContext());
		}
		else if (viewClassName.equalsIgnoreCase(TextView.class.getSimpleName())) {
			view = new FreeTextView(getContext());
			if (paramsMap.containsKey("text")) {
				String text = paramsMap.get("text");
				if (text.contains("@string/")) {
					String textName = toId(text);
					((TextView) view).setText(getResources().getIdentifier(textName, "string", getContext().getPackageName()));
				}
				else {
					((TextView) view).setText(paramsMap.get("text"));
				}
			}
			if (paramsMap.containsKey("textColor")) {
				((TextView) view).setTextColor(Color.parseColor(paramsMap.get("textColor")));
			}
			if (paramsMap.containsKey("textSize")) {
				((FreeTextView) view).setPicSize(picSize);
				String sizeString = paramsMap.get("textSize");
				Log.v(TAG, "sizeString: " + sizeString);
				if (!TextUtils.isEmpty(sizeString)) {
					if (sizeString.contains("px") || sizeString.contains("dp")) {
						((FreeTextView) view).setTextSizeFitResolution(size2Px(paramsMap, "textSize"));
					}
					else {
						int textSize = Integer.parseInt(sizeString.replace("sp", ""));
						((FreeTextView) view).setTextSize(textSize);
					}
				}
			}
			if (paramsMap.containsKey("singleLine")) {
				((TextView) view).setSingleLine(Boolean.parseBoolean(paramsMap.get("singleLine")));
			}
			if (paramsMap.containsKey("gravity")) {
				((TextView) view).setGravity(getViewGravity(paramsMap));
			}
		}
		else if (viewClassName.equalsIgnoreCase(Button.class.getSimpleName())) {
			view = new FreeTextButton(getContext());
			if (paramsMap.containsKey("text")) {
				String text = paramsMap.get("text");
				if (text.contains("@string/")) {
					String textName = toId(text);
					((TextView) view).setText(getResources().getIdentifier(textName, "string", getContext().getPackageName()));
				}
				else {
					((TextView) view).setText(paramsMap.get("text"));
				}
			}
			if (paramsMap.containsKey("textColor")) {
				((Button) view).setTextColor(Color.parseColor(paramsMap.get("textColor")));
			}
			if (paramsMap.containsKey("textSize")) {
				((FreeTextButton) view).setPicSize(picSize);
				String sizeString = paramsMap.get("textSize");
				if (!TextUtils.isEmpty(sizeString)) {
					if (sizeString.contains("px") || sizeString.contains("dp")) {
						((FreeTextButton) view).setTextSizeFitResolution(size2Px(paramsMap, "textSize"));
					}
					else {
						int textSize = Integer.parseInt(sizeString.replace("sp", ""));
						((FreeTextButton) view).setTextSize(textSize);
					}
				}
			}
			if (paramsMap.containsKey("singleLine")) {
				((Button) view).setSingleLine(Boolean.parseBoolean(paramsMap.get("singleLine")));
			}
			if (paramsMap.containsKey("gravity")) {
				((Button) view).setGravity(getViewGravity(paramsMap));
			}
		}
		else if (viewClassName.equalsIgnoreCase(ImageView.class.getSimpleName())) {
			view = new ImageView(getContext());
			if (paramsMap.containsKey("scaleType")) {
				String scaleType = toId(paramsMap.get("scaleType"));
				if (scaleType.equalsIgnoreCase("fitCenter")) {
					((ImageView) view).setScaleType(ScaleType.FIT_CENTER);
				}
				if (scaleType.equalsIgnoreCase("fitEnd")) {
					((ImageView) view).setScaleType(ScaleType.FIT_END);
				}
				if (scaleType.equalsIgnoreCase("fitStart")) {
					((ImageView) view).setScaleType(ScaleType.FIT_START);
				}
				if (scaleType.equalsIgnoreCase("fitXY")) {
					((ImageView) view).setScaleType(ScaleType.FIT_XY);
				}
				if (scaleType.equalsIgnoreCase("center")) {
					((ImageView) view).setScaleType(ScaleType.CENTER);
				}
				if (scaleType.equalsIgnoreCase("centerCrop")) {
					((ImageView) view).setScaleType(ScaleType.CENTER_CROP);
				}
				if (scaleType.equalsIgnoreCase("centerInside")) {
					((ImageView) view).setScaleType(ScaleType.CENTER_INSIDE);
				}
			}
			if (paramsMap.containsKey("src")) {
				String srcName = toId(paramsMap.get("src"));
				((ImageView) view).setImageResource(getResources().getIdentifier(srcName, "drawable", getContext().getPackageName()));
			}
			if (paramsMap.containsKey("tag")) {
				String url = paramsMap.get("tag");
				ImageLoader.getInstance(getContext(), 0).displayImage(url, ((ImageView) view));
			}
		}
		else {
			view = new View(getContext());
		}

		if (paramsMap.containsKey("id")) {
			int id = Math.abs(toId(paramsMap.get("id")).hashCode());
			view.setId(id);
		}
		if (paramsMap.containsKey("background")) {
			if (paramsMap.get("background").contains("#")) {
				view.setBackgroundColor(Color.parseColor(paramsMap.get("background")));
			}
			else if (paramsMap.get("background").contains("drawable")) {
				String drawableName = toId(paramsMap.get("background"));
				view.setBackgroundResource(getResources().getIdentifier(drawableName, "drawable", getContext().getPackageName()));
			}
		}
		// TODO
		if (paramsMap.containsKey("onClick")) {
			mOnClickEvents.put(view, paramsMap.get("onClick"));
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mOnXmlClickListener != null) {
						mOnXmlClickListener.onClick(v, mOnClickEvents.get(v));
					}
				}
			});
		}

		return view;
	}

	private void setViewPadding(View view, HashMap<String, String> paramsMap) {
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		if (paramsMap.containsKey("padding")) {
			left = parseSize(paramsMap, "padding");
			top = parseSize(paramsMap, "padding");
			right = parseSize(paramsMap, "padding");
			bottom = parseSize(paramsMap, "padding");
		}
		if (paramsMap.containsKey("paddingLeft")) {
			left = parseSize(paramsMap, "paddingLeft");
		}
		if (paramsMap.containsKey("paddingTop")) {
			top = parseSize(paramsMap, "paddingTop");
		}
		if (paramsMap.containsKey("paddingRight")) {
			right = parseSize(paramsMap, "paddingRight");
		}
		if (paramsMap.containsKey("paddingBottom")) {
			bottom = parseSize(paramsMap, "paddingBottom");
		}

		view.setPadding(left, top, right, bottom);
	}

	private void setViewMargin(View view, HashMap<String, String> paramsMap) {
		int left = 0;
		int top = 0;
		int right = 0;
		int bottom = 0;
		if (paramsMap.containsKey("layout_margin")) {
			left = parseSize(paramsMap, "layout_margin");
			top = parseSize(paramsMap, "layout_margin");
			right = parseSize(paramsMap, "layout_margin");
			bottom = parseSize(paramsMap, "layout_margin");
		}
		if (paramsMap.containsKey("layout_marginLeft")) {
			left = parseSize(paramsMap, "layout_marginLeft");
		}
		if (paramsMap.containsKey("layout_marginTop")) {
			top = parseSize(paramsMap, "layout_marginTop");
		}
		if (paramsMap.containsKey("layout_marginRight")) {
			right = parseSize(paramsMap, "layout_marginRight");
		}
		if (paramsMap.containsKey("layout_marginBottom")) {
			bottom = parseSize(paramsMap, "layout_marginBottom");
		}

		if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			((RelativeLayout.LayoutParams) view.getLayoutParams()).setMargins(left, top, right, bottom);
		}
		else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			((LinearLayout.LayoutParams) view.getLayoutParams()).setMargins(left, top, right, bottom);
		}
		else if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
			((FrameLayout.LayoutParams) view.getLayoutParams()).setMargins(left, top, right, bottom);
		}
	}

	private void setViewVisibility(View view, HashMap<String, String> paramsMap) {
		if (paramsMap.containsKey("visibility")) {
			if (paramsMap.get("visibility").equalsIgnoreCase("visible")) {
				view.setVisibility(VISIBLE);
			}
			else if (paramsMap.get("visibility").equalsIgnoreCase("invisible")) {
				view.setVisibility(INVISIBLE);
			}
			else if (paramsMap.get("visibility").equalsIgnoreCase("gone")) {
				view.setVisibility(GONE);
			}
		}
	}

	private int getViewGravity(HashMap<String, String> paramsMap) {
		String gravityString = paramsMap.get("gravity");
		String[] gravities = gravityString.split("\\|");
		ArrayList<String> gravityArrayList = new ArrayList<String>(Arrays.asList(gravities));

		int gravity = -1;
		for (int i = 0; i < gravityArrayList.size(); i++) {
			if (gravityArrayList.get(i).equalsIgnoreCase("center")) {
				gravity = addGravity(gravity, Gravity.CENTER);
			}
			if (gravityArrayList.get(i).equalsIgnoreCase("center_vertical")) {
				gravity = addGravity(gravity, Gravity.CENTER_VERTICAL);
			}
			if (gravityArrayList.get(i).equalsIgnoreCase("center_horizontal")) {
				gravity = addGravity(gravity, Gravity.CENTER_HORIZONTAL);
			}
			if (gravityArrayList.get(i).equalsIgnoreCase("left")) {
				gravity = addGravity(gravity, Gravity.LEFT);
			}
			if (gravityArrayList.get(i).equalsIgnoreCase("top")) {
				gravity = addGravity(gravity, Gravity.TOP);
			}
			if (gravityArrayList.get(i).equalsIgnoreCase("right")) {
				gravity = addGravity(gravity, Gravity.RIGHT);
			}
			if (gravityArrayList.get(i).equalsIgnoreCase("bottom")) {
				gravity = addGravity(gravity, Gravity.BOTTOM);
			}
		}

		return gravity;
	}

	private int addGravity(int gravity, int newGravity) {
		if (gravity == -1) {
			return newGravity;
		}
		else {
			return gravity | newGravity;
		}
	}

	private boolean equalToLayout(String viewClass, View v) {
		if (mLayoutStack.peek() instanceof RelativeLayout &&
				viewClass.equalsIgnoreCase(((RelativeLayout) mLayoutStack.peek()).getClass().getSimpleName())) {
			return true;
		}
		if (mLayoutStack.peek() instanceof LinearLayout &&
				viewClass.equalsIgnoreCase(((LinearLayout) mLayoutStack.peek()).getClass().getSimpleName())) {
			return true;
		}
		if (mLayoutStack.peek() instanceof FrameLayout &&
				viewClass.equalsIgnoreCase(((FrameLayout) mLayoutStack.peek()).getClass().getSimpleName())) {
			return true;
		}
		return false;
	}

	private boolean isLayout(View v) {
		if (v instanceof ViewGroup) {
			return true;
		}
		return false;
	}

	private int size2Px(HashMap<String, String> paramsMap, String key) {
		String sizeString = paramsMap.get(key);
		int size = 0;
		if (sizeString.contains("px")) {
			size = Integer.parseInt(sizeString.replace("px", ""));
		}
		else if (sizeString.contains("dp")) {
			size = dp2px(getContext(), Integer.parseInt(sizeString.replace("dp", "")));
		}
		return size;
	}

	private int parseSize(HashMap<String, String> paramsMap, String key) {
		return resizeInPx(size2Px(paramsMap, key));
	}

	private int resizeInPx(int size) {
		return (int) (0.5f + size * windowSize / (float) picSize);
	}

	private String toId(String id) {
		String[] splits = id.split("/");
		return splits[splits.length - 1];
	}

	public void setOnXmlClickListener(OnXmlClickListener onXmlClickListener) {
		mOnXmlClickListener = onXmlClickListener;
	}

	public static Object invokeFunction(Class<?> c, String functionName, Object receiver, Object... args) {
		try {
			Method m = c.getMethod(functionName);
			return m.invoke(receiver, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
