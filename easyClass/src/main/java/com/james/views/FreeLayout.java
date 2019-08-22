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

import java.util.HashMap;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;

/**
 * <br>
 * Chinese Explanation: <br>
 * FreeLayout 可以排出各種絕對或是相對或是相對其他view的版面，建議搭配LayoutEditor 使用 <br>
 * <br>
 * English Explanation: <br>
 * FreeLayout is a robust class to make multi-resolution fitness layout. <br>
 * For easier usibilty, please download LayoutEditor as well. <br>
 * <br>
 * <a href= "https://dl.dropboxusercontent.com/u/43934047/EasyClass%E8%88%87LayoutEditor/LayoutEditor_v0.8.6.jar" >LayoutEditor</a>
 * 
 * @author JamesX
 * @since 2012/05/17
 * @see <a href="http://androidthinkermobile.blogspot.tw/">Introduction for FreeLayout</a>
 */
public class FreeLayout extends RelativeLayout {

	protected Context mContext;
	protected DisplayMetrics dm;
	protected int monitorWidth, monitorHeight;
	protected int layoutWidth, layoutHeight;
	protected int picSize = 750;
	protected int id = 0x5797;
	protected int idHandle = 0x5000;
	protected int idContent = 0x6000;
	protected int windowSize;

	public static int VERTICAL = LinearLayout.VERTICAL;
	public static int HORIZONTAL = LinearLayout.HORIZONTAL;

	public static final int TO_WIDTH = 0x1000;
	public static final int TO_HEIGHT = 0x1001;

	private HashMap<View, View> map = new HashMap<View, View>();

	private OnSizedChangedListener mOnSizedChangedListener;

	public interface OnSizedChangedListener {
		public void onSizedChanged(int w, int h, int oldw, int oldh);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * FreeLayout 可以排出各種絕對或是相對或是相對其他view的版面，建議搭配LayoutEditor 使用 <br>
	 * <br>
	 * English Explanation: <br>
	 * FreeLayout is a robust class to make multi-resolution fitness layout. <br>
	 * For easier usibilty, please download LayoutEditor as well. <br>
	 * <br>
	 * <a href= "https://dl.dropboxusercontent.com/u/43934047/EasyClass%E8%88%87LayoutEditor/LayoutEditor_v0.8.6.jar" >LayoutEditor</a>
	 * 
	 * @see <a href="http://androidthinkermobile.blogspot.tw/">Introduction for FreeLayout</a>
	 */

	public FreeLayout(Context context) {
		super(context);
		mContext = context;
		dm = new DisplayMetrics();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
		windowSize = monitorWidth;
	}

	public FreeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		dm = new DisplayMetrics();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
		windowSize = monitorWidth;

	}

	public FreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		dm = new DisplayMetrics();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
		windowSize = monitorWidth;
	}

	/**
	 * Please use setPicSize(layoutWidth, layoutHeight, toWich)
	 * 
	 * @param toWich
	 * @deprecated
	 */
	public void setDatum(int toWich) {
		setPicSize(-1, -1, toWich);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * 預設螢幕畫面是640*960 <br>
	 * <br>
	 * English Explanation: <br>
	 * Default size is 640*960
	 * 
	 * @param picSize (int) default is 640
	 */
	public void setPicSize(int picSize) {
		setPicSize(picSize, -1, TO_WIDTH);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * 預設螢幕畫面是640*960 <br>
	 * <br>
	 * English Explanation: <br>
	 * Default size is 640*960
	 * 
	 * @param picWidth (int) default is 640
	 * @param picHeight (int) default is 960
	 */
	public void setPicSize(int picWidth, int picHeight) {
		setPicSize(picWidth, picHeight, TO_WIDTH);
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
	public void setPicSize(int picWidth, int picHeight, int toWich) {
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
	}

	public void setFreeLayoutWW() {
		if (this.getLayoutParams() != null) {
			this.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
			this.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		}
		else {
			setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}

	public void setFreeLayoutWF() {
		if (this.getLayoutParams() != null) {
			this.getLayoutParams().width = LayoutParams.WRAP_CONTENT;
			this.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		}
		else {
			setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		}
	}

	public void setFreeLayoutFW() {
		if (this.getLayoutParams() != null) {
			this.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			this.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		}
		else {
			setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	public void setFreeLayoutFF() {
		if (this.getLayoutParams() != null) {
			this.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			this.getLayoutParams().height = LayoutParams.MATCH_PARENT;
		}
		else {
			setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 View 加到畫面上的(0,0)位置與相對大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView at position (0,0) and scale size of a view automatically. <br>
	 * 
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return
	 */
	public View addFreeView(View view, int width, int height) {
		return addFreeView(view, width, height, null, null, null, null, null);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 View 加到畫面上相對父 layout 的位置與大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView scale position and size of a view automatically. <br>
	 * Developers may set relations with its parent view. <br>
	 * <br>
	 * Ex: new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT}
	 * 
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param relative (int[]) Ex: new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT}
	 * @return
	 */
	public View addFreeView(View view, int width, int height, int[] relative) {
		return addFreeView(view, width, height, relative, null, null, null,
				null);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 view 加到畫面上相對 view2 的位置與大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView scale position and size of a view automatically. <br>
	 * Developers may set relations with another view, view2. <br>
	 * <br>
	 * Ex: new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * 
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param view2 (View)
	 * @param relative2 (int[]) relations with view2. Ex: new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * @return
	 */
	public View addFreeView(View view, int width, int height, View view2,
			int[] relative2) {
		return addFreeView(view, width, height, null, view2, relative2, null,
				null);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 view 加到畫面上相對 view2 與相對父 layout 的位置與大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView scale position and size of a view automatically. <br>
	 * Developers may set relations with parent view and another view, view2. <br>
	 * <br>
	 * Ex: new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * 
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param relative (int[]) relations with parent view, Ex: new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT}
	 * @param view2 (View)
	 * @param relative2 (int[]) relations with view2. Ex: new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * @return
	 */
	public View addFreeView(View view, int width, int height, int[] relative,
			View view2, int[] relative2) {
		return addFreeView(view, width, height, relative, view2, relative2,
				null, null);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 view 加到畫面上相對 view2 與相對 view3 的位置與大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView scale position and size of a view automatically. <br>
	 * Developers may set relations with other two views, view2 and view3. <br>
	 * <br>
	 * Ex: new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * 
	 * @param view
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param view2 (View)
	 * @param relative2 (int[]) relations with view2. new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * @param view3 (View)
	 * @param relative3 (int[]) relations with view3. new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * @return
	 */
	public View addFreeView(View view, int width, int height, View view2,
			int[] relative2, View view3, int[] relative3) {
		return addFreeView(view, width, height, null, view2, relative2, view3,
				relative3);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 view 加到畫面上相對父 layout與相對 view2 與相對 view3 的位置與大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView scale position and size of a view automatically. <br>
	 * Developers may set relations with parent view and other views, view2 and view3. <br>
	 * <br>
	 * Ex: new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * 
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param relative (int[]) relations with parent view, Ex: new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_LEFT}
	 * @param view2 (View)
	 * @param relative2 (int[]) relations with view2. new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * @param view3 (View)
	 * @param relative3 (int[]) relations with view3. new int[]{RelativeLayout.BELOW, RelativeLayout.ALIGN_TOP}
	 * @return
	 */
	public View addFreeView(View view, int width, int height, int[] relative,
			View view2, int[] relative2, View view3, int[] relative3) {
		id++;
		view.setId(id);
		view.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			view.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			view.getLayoutParams().height = size;
		}
		if (relative != null) {
			for (int i = 0; i < relative.length; i++) {
				((RelativeLayout.LayoutParams) view.getLayoutParams())
						.addRule(relative[i]);
			}
		}

		if (relative2 != null && view2 != null) {
			for (int i = 0; i < relative2.length; i++) {
				((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
						relative2[i], view2.getId());
			}
		}

		if (relative3 != null && view3 != null) {
			for (int i = 0; i < relative3.length; i++) {
				((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
						relative3[i], view3.getId());
			}
		}
		this.addView(view);

		// in case of tab
		if (view.getClass().equals(TabHost.class)) {
			// linear layout
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearLayout.setLayoutParams(new TabHost.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			// tabs
			TabWidget tabs = new TabWidget(mContext);
			tabs.setId(android.R.id.tabs);
			tabs.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			linearLayout.addView(tabs);
			// tab content
			FrameLayout tabostContent = new FrameLayout(mContext);
			tabostContent.setId(android.R.id.tabcontent);
			linearLayout.addView(tabostContent);
			((TabHost) view).addView(linearLayout);
		}

		return view;
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * addView 加強版，可以將 View 加到畫面上任何相對的位置與大小 <br>
	 * <br>
	 * English Explanation: <br>
	 * A more powerful addView scale position and size of a view automatically.
	 * 
	 * @param view (View)
	 * @param left (int) the left of view, namely x. Automatically resize by FreeLayout.
	 * @param top (int) the top of view, namely y. Automatically resize by FreeLayout.
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return (view)
	 */
	public View addFreeView(View view, int left, int top, int width, int height) {
		// TODO offset view
		id++;
		View viewOffset = new View(mContext);
		viewOffset.setId(id);
		viewOffset.setLayoutParams(new LayoutParams((int) (0.5f + left
				* windowSize / (float) picSize), (int) (0.5f + top
				* windowSize / (float) picSize)));
		this.addView(viewOffset);

		id++;
		view.setId(id);
		map.put(view, viewOffset);
		view.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			view.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			view.getLayoutParams().height = size;
		}

		((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
				RelativeLayout.BELOW, viewOffset.getId());
		((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
				RelativeLayout.RIGHT_OF, viewOffset.getId());
		this.addView(view);

		// in case of tab
		if (view.getClass().equals(TabHost.class)) {
			// linear layout
			LinearLayout linearLayout = new LinearLayout(mContext);
			linearLayout.setLayoutParams(new TabHost.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			linearLayout.setOrientation(LinearLayout.VERTICAL);
			// tabs
			TabWidget tabs = new TabWidget(mContext);
			tabs.setId(android.R.id.tabs);
			tabs.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			linearLayout.addView(tabs);
			// tab content
			FrameLayout tabostContent = new FrameLayout(mContext);
			tabostContent.setId(android.R.id.tabcontent);
			linearLayout.addView(tabostContent);
			((TabHost) view).addView(linearLayout);
		}

		return view;
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * 將 view 加到 LinearLayout 裡， <br>
	 * 這邊特別建議先用 addFreeView(...) 將 LinearLayout 加入畫面， <br>
	 * 再用addFreeLinearView(...) 加入 view， <br>
	 * <br>
	 * English Explanation: <br>
	 * add view in a LinearLayout. <br>
	 * Use addFreeView(...) first to add a LinarLayout, and then <br>
	 * use addFreeLinearView(...) to add views into LinearLayout. <br>
	 * <br>
	 * Ex:
	 * 
	 * <pre>
	 * LinearLayout layout = (LinearLayout) addFreeView(new LinearLayout(context),
	 * 		LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
	 * 		new int[] {
	 * 			CENTER_IN_PARENT
	 * 		});
	 * Button btn1 = (Button) addFreeLinearView(layout, new Button(context), 100, 50);
	 * </pre>
	 * 
	 * @param layout (LinearLayout) 要加入 View 的 LinearLayout
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return
	 */
	public View addFreeLinearView(LinearLayout layout, View view, int width,
			int height) {
		return addFreeLinearView(layout, view, width, height, -1);
	}

	/**
	 * <br>
	 * Chinese Explanation: <br>
	 * 將 view 加到 LinearLayout 裡， <br>
	 * 這邊特別建議先用 addFreeView(...) 將 LinearLayout 加入畫面， <br>
	 * 再用addFreeLinearView(...) 加入 view， <br>
	 * <br>
	 * English Explanation: <br>
	 * add view in a LinearLayout. <br>
	 * Use addFreeView(...) first to add a LinarLayout, and then <br>
	 * use addFreeLinearView(...) to add views into LinearLayout. <br>
	 * 
	 * <pre>
	 * Ex:
	 * LinearLayout layout = (LinearLayout)addFreeView(new LinearLayout(context), 
	 *         LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
	 *         new int[]{CENTER_IN_PARENT}); 
	 * Button btn1 = (Button)addFreeLinearView(layout, new Button(context), 100, 50, 1);
	 * </pre>
	 * 
	 * @param layout (LinearLayout) 要加入 View 的 LinearLayout
	 * @param view (View)
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param weight (float) the weight of view
	 * @return
	 */
	public View addFreeLinearView(LinearLayout layout, View view, int width,
			int height, float weight) {
		id++;
		view.setId(id);
		if (weight != -1) {
			view.setLayoutParams(new LinearLayout.LayoutParams(width, height,
					weight));
		} else {
			view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
		}

		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			view.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			view.getLayoutParams().height = size;
		}

		layout.addView(view);
		return view;
	}

	/**
	 * Add a vertical scroll view.
	 * 
	 * @param layout
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return
	 */
	public FreeLayout addFreeScrollView(FreeLayout layout, int width, int height) {
		id++;
		layout.setId(id);
		layout.setLayoutParams(new LayoutParams(width, height));
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setLayoutParams(new LayoutParams(width, height));
		ScrollView scrollView = new ScrollView(mContext);
		scrollView.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			scrollView.getLayoutParams().width = size;
			linearLayout.getLayoutParams().width = size;
			layout.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			scrollView.getLayoutParams().height = size;
			linearLayout.getLayoutParams().height = size;
			layout.getLayoutParams().height = size;
		}
		linearLayout.addView(layout);
		scrollView.addView(linearLayout);
		this.addView(scrollView);

		return layout;
	}

	/**
	 * Add a vertical scroll view.
	 * 
	 * @param scrollView
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return
	 */
	public FreeLayout addFreeScrollView(ScrollView scrollView, int width,
			int height) {
		id++;
		FreeLayout layout = new FreeLayout(scrollView.getContext());
		layout.setId(id);
		layout.setLayoutParams(new LayoutParams(width, height));
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setLayoutParams(new LayoutParams(width, height));
		scrollView.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			scrollView.getLayoutParams().width = size;
			linearLayout.getLayoutParams().width = size;
			layout.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			scrollView.getLayoutParams().height = size;
			linearLayout.getLayoutParams().height = size;
			layout.getLayoutParams().height = size;
		}
		linearLayout.addView(layout);
		scrollView.addView(linearLayout);
		this.addView(scrollView);

		return layout;
	}

	/**
	 * Add a horizontal scroll view.
	 * 
	 * @param layout
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return
	 */
	public FreeLayout addFreeHorizontalScrollView(FreeLayout layout, int width,
			int height) {
		id++;
		layout.setId(id);
		layout.setLayoutParams(new LayoutParams(width, height));
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setLayoutParams(new LayoutParams(width, height));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		HorizontalScrollView scrollView = new HorizontalScrollView(mContext);
		scrollView.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			scrollView.getLayoutParams().width = size;
			linearLayout.getLayoutParams().width = size;
			layout.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			scrollView.getLayoutParams().height = size;
			linearLayout.getLayoutParams().height = size;
			layout.getLayoutParams().height = size;
		}
		linearLayout.addView(layout);
		scrollView.addView(linearLayout);
		this.addView(scrollView);

		return layout;
	}

	/**
	 * Add a horizontal scroll view.
	 * 
	 * @param scrollView
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @return
	 */
	public FreeLayout addFreeHorizontalScrollView(
			HorizontalScrollView scrollView, int width, int height) {
		id++;
		FreeLayout layout = new FreeLayout(scrollView.getContext());
		layout.setId(id);
		layout.setLayoutParams(new LayoutParams(width, height));
		LinearLayout linearLayout = new LinearLayout(mContext);
		linearLayout.setLayoutParams(new LayoutParams(width, height));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		scrollView.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			scrollView.getLayoutParams().width = size;
			linearLayout.getLayoutParams().width = size;
			layout.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			scrollView.getLayoutParams().height = size;
			linearLayout.getLayoutParams().height = size;
			layout.getLayoutParams().height = size;
		}
		linearLayout.addView(layout);
		scrollView.addView(linearLayout);
		this.addView(scrollView);

		return layout;
	}

	/**
	 * Reset LayoutParams of View
	 * 
	 * @param view (View) 可加入任何 View
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 */
	public void setFreeView(View view, int width, int height) {
		//
		setFreeView(view, width, height, null, null, null, null, null);
	}

	/**
	 * Reset LayoutParams of View
	 * 
	 * @param view (View) 可加入任何 View
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param relative (int[]) 相對父 layout 的關係，可多層關係 Ex: RelativeLayout.CENTER_IN_PARENT
	 */
	public void setFreeView(View view, int width, int height, int[] relative) {
		//
		setFreeView(view, width, height, relative, null, null, null, null);
	}

	/**
	 * Reset LayoutParams of View
	 * 
	 * @param view (View) 可加入任何 View
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param relative 相對父 layout 的關係，可多層關係 Ex: RelativeLayout.CENTER_IN_PARENT
	 * @param view2 (View) 相對位置的 View
	 * @param relative2 (int[]) 相對 view2 的關係，可多層關係 Ex: RelativeLayout.BELOW
	 */
	public void setFreeView(View view, int width, int height, int[] relative,
			View view2, int[] relative2) {

		setFreeView(view, width, height, relative, view2, relative2, null, null);
	}

	/**
	 * Reset LayoutParams of View
	 * 
	 * @param view (View) 可加入任何 View
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param view2 (View) 相對位置的 View
	 * @param relative2 (int[]) 相對 view2 的關係，可多層關係 Ex: RelativeLayout.BELOW
	 */
	public void setFreeView(View view, int width, int height, View view2,
			int[] relative2) {

		setFreeView(view, width, height, null, view2, relative2, null, null);
	}

	/**
	 * Reset LayoutParams of View
	 * 
	 * @param view
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param view2
	 * @param relative2
	 * @param view3
	 * @param relative3
	 */
	public void setFreeView(View view, int width, int height, View view2,
			int[] relative2, View view3, int[] relative3) {

		setFreeView(view, width, height, null, view2, relative2, view3,
				relative3);
	}

	/**
	 * Reset LayoutParams of View
	 * 
	 * @param view
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 * @param relative
	 * @param view2
	 * @param relative2
	 * @param view3
	 * @param relative3
	 */
	public void setFreeView(View view, int width, int height, int[] relative,
			View view2, int[] relative2, View view3, int[] relative3) {
		if (view == null)
			return;
		//
		view.setLayoutParams(new LayoutParams(width, height));
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			view.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			view.getLayoutParams().height = size;
		}
		if (relative != null) {
			for (int i = 0; i < relative.length; i++) {
				((RelativeLayout.LayoutParams) view.getLayoutParams())
						.addRule(relative[i]);
			}
		}

		if (relative2 != null && view2 != null) {
			for (int i = 0; i < relative2.length; i++) {
				((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
						relative2[i], view2.getId());
			}
		}

		if (relative3 != null && view3 != null) {
			for (int i = 0; i < relative3.length; i++) {
				((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
						relative3[i], view3.getId());
			}
		}

		requestLayout();
	}

	/**
	 * 重設 View 的 LayoutParams
	 * 
	 * @param view (View)
	 * @param left (int) the left of view, namely x. Automatically resize by FreeLayout.
	 * @param top (int) the top of view, namely y. Automatically resize by FreeLayout.
	 * @param width (int) the width of view. Automatically resize by FreeLayout.
	 * @param height (int) the height of view. Automatically resize by FreeLayout.
	 */
	public void setFreeView(View view, int left, int top, int width, int height) {
		if (view == null)
			return;

		// TODO offset view
		FreeLayout parentLayout = (FreeLayout) view.getParent();
		View viewOffset = parentLayout.map.get(view);
		if (viewOffset == null) {
			parentLayout.id++;
			viewOffset = new View(mContext);
			viewOffset.setId(parentLayout.id);
			viewOffset.setLayoutParams(new LayoutParams((int) (0.5f + left
					* windowSize / (float) picSize), (int) (0.5f + top
					* windowSize / (float) picSize)));

			int index = 0;
			for (int i = 0; i < parentLayout.getChildCount(); i++) {
				if (parentLayout.getChildAt(i).equals(view)) {
					index = i;
					break;
				}
			}

			parentLayout.addView(viewOffset, index);
			parentLayout.map.put(view, viewOffset);
		} else {
			viewOffset.getLayoutParams().width = (int) (0.5f + left
					* windowSize / (float) picSize);
			viewOffset.getLayoutParams().height = (int) (0.5f + top
					* windowSize / (float) picSize);
		}

		view.setLayoutParams(new LayoutParams(width, height));
		view.getLayoutParams().width = width;
		view.getLayoutParams().height = height;
		if (width != LayoutParams.MATCH_PARENT
				&& width != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + width * windowSize / (float) picSize);
			view.getLayoutParams().width = size;
		}
		if (height != LayoutParams.MATCH_PARENT
				&& height != LayoutParams.WRAP_CONTENT) {
			int size = (int) (0.5f + height * windowSize / (float) picSize);
			view.getLayoutParams().height = size;
		}

		((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
				RelativeLayout.BELOW, viewOffset.getId());
		((RelativeLayout.LayoutParams) view.getLayoutParams()).addRule(
				RelativeLayout.RIGHT_OF, viewOffset.getId());

		requestLayout();
	}

	/**
	 * 可以對view加入margin效果(長度會針對畫面大小轉換)
	 * 
	 * @param view (View)
	 * @param left (int)
	 * @param top (int)
	 * @param right (int)
	 * @param bottom (int)
	 */
	public void setMargin(View view, int left, int top, int right, int bottom) {
		((RelativeLayout.LayoutParams) (view.getLayoutParams())).setMargins(
				(int) (0.5f + left * windowSize / (float) picSize),
				(int) (0.5f + top * windowSize / (float) picSize),
				(int) (0.5f + right * windowSize / (float) picSize),
				(int) (0.5f + bottom * windowSize / (float) picSize));
	}

	/**
	 * 可以對view加入padding效果(長度會針對畫面大小轉換)
	 * 
	 * @param view
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public void setPadding(View view, int left, int top, int right, int bottom) {
		view.setPadding((int) (0.5f + left * windowSize / (float) picSize),
				(int) (0.5f + top * windowSize / (float) picSize),
				(int) (0.5f + right * windowSize / (float) picSize),
				(int) (0.5f + bottom * windowSize / (float) picSize));
	}

	public void setOnSizedChangedListener(
			OnSizedChangedListener onSizedChangedListener) {
		mOnSizedChangedListener = onSizedChangedListener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		layoutWidth = getWidth();
		layoutHeight = getHeight();

		if (mOnSizedChangedListener != null)
			mOnSizedChangedListener.onSizedChanged(w, h, oldw, oldh);
	}

	protected int reverseScale(int size) {
		return size * picSize / windowSize;
	}

	/**
	 * 根據手機的分辨率以 dp 的單位 轉成為 px
	 */
	public static int dp2px(Context context, float dp) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (dp * density + 0.5f);
	}

	/**
	 * 根據手機的分辨率以 px 的單位 轉成為 dp
	 */
	public static int px2dip(Context context, float px) {
		final float density = context.getResources().getDisplayMetrics().density;
		return (int) (px / density + 0.5f);
	}
}
