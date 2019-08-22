package com.james.views;

import java.util.Date;

import com.james.views.FreeLayout;
import com.james.views.FreeTextView;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

/**
 * 
 * 自定义MsgListView，继承了ListView， 但填充了listview的头部，即下拉更新样式，并实现其功能
 * 
 * @author yanbo
 * 
 */
public class RefreshableListView extends ListView implements OnScrollListener {
	public final static int RELEASE_To_REFRESH = 0;
	public final static int PULL_To_REFRESH = 1;
	public final static int REFRESHING = 2;
	public final static int DONE = 3;

	private HeaderView headView; // 頭部
//	private LinearLayout footerView; // 底部

	private FreeTextView tipsTextview; // 下拉更新
	private FreeTextView lastUpdatedTextView; // 最新更新
	private ImageView arrowImage; // 箭頭
	private ProgressBar progressBar; // 更新進度條

	private RotateAnimation animation; // 旋轉特效，更新中箭頭翻轉，向下變向上
	private RotateAnimation reverseAnimation;

	private boolean isRecored; // 用於保證startY的值在一個完整的touch事件中只被記錄一次

	private int headContentWidth; // 頭部寬度
	private int headContentHeight; // 頭部高度

	private int startY; // 高度起始位置，用來記錄與頭部距離
	public int firstItemIndex; // 列表中首行索引，用來記錄其與頭部距離

	private int firstVisibleItem;
	private int visibleItemCount;
	private int totalItemCount;

	public int state; // 下拉更新中、放開更新中、正在更新中、完成更新

	private boolean isBack;

	public OnRefreshListener refreshListener; // 更新監聽

	private OnAppendListener mOnAppendListener;

	private final static String TAG = "RefreshableListView";

	public interface OnRefreshListener {
		public void onRefresh();
	}

	public interface OnAppendListener {
		public void onAppend();
	}

	/*
	 * public MsgListView(Context context, AttributeSet attrs) { super(context,
	 * attrs); init(context); }
	 */
	public RefreshableListView(Context context, int loadingResId) {
		super(context);
		init(context, loadingResId);
	}

	public class HeaderView extends FreeLayout {
		public FreeLayout headerLayout;
		public ImageView loadingImage;
		public ProgressBar progressBar;
		public FreeTextView loadingText;
		public FreeTextView updateText;

		public HeaderView(Context context) {
			super(context);

			this.setFreeLayoutFF();
			this.setPicSize(640, 960, TO_WIDTH);

			headerLayout = (FreeLayout) this.addFreeView(new FreeLayout(
					mContext), 640, LayoutParams.WRAP_CONTENT);
			headerLayout.setPicSize(640, 960, TO_WIDTH);
			
			loadingImage = (ImageView) headerLayout.addFreeView(new ImageView(
					mContext), 90, 90, new int[] { CENTER_VERTICAL });
			setMargin(loadingImage, 15, 0, 0, 0);
			
			progressBar = (ProgressBar) headerLayout.addFreeView(new ProgressBar(
					mContext), 45, 45, new int[] { CENTER_VERTICAL });
			setMargin(progressBar, 25, 0, 0, 0);

			loadingText = (FreeTextView) headerLayout.addFreeView(
					new FreeTextView(mContext), LayoutParams.WRAP_CONTENT, 45,
					new int[] { CENTER_HORIZONTAL });
			loadingText.setTextColor(Color.BLACK);
			loadingText.setTextSizeFitPx(loadingText.getLayoutParams().height*8/10);
			loadingText.setGravity(Gravity.CENTER);

			updateText = (FreeTextView) headerLayout.addFreeView(
					new FreeTextView(mContext), LayoutParams.WRAP_CONTENT, 30,
					new int[] { CENTER_HORIZONTAL },
					loadingText, new int[] { BELOW });
			updateText.setTextColor(Color.BLACK);
			updateText.setTextSizeFitPx(updateText.getLayoutParams().height*8/10);
			updateText.setGravity(Gravity.CENTER);

		}
	}

	private void init(Context context, int loadingResId) {
		headView = new HeaderView(context);
		headView.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		arrowImage = headView.loadingImage;
		arrowImage.setMinimumWidth(50);
		arrowImage.setMinimumHeight(30);
		arrowImage.setImageResource(loadingResId);
		progressBar = headView.progressBar;
		tipsTextview = headView.loadingText;
		lastUpdatedTextView = headView.updateText;

		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();

		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();

		addHeaderView(headView);
		/*
		 * footerView = new LinearLayout(context);
		 * footerView.setLayoutParams(new
		 * ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,50));
		 * footerView.setBackgroundColor(Color.WHITE); footerView.invalidate();
		 * addFooterView(footerView, null, false);
		 */
		setOnScrollListener(this); // 滾動監聽

		animation = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true); // 特效animation設置

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(250);
		reverseAnimation.setFillAfter(true); // 特效reverseAnimation設置
	}

	public void onScroll(AbsListView arg0, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) { // 滚动事件
		firstItemIndex = firstVisibleItem;// 得到首item索引

		this.firstVisibleItem = firstVisibleItem;
		this.visibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {
			if (firstVisibleItem + visibleItemCount == totalItemCount) {
				if (mOnAppendListener != null)
					mOnAppendListener.onAppend();
			}
		}
	}

	public boolean onTouchEvent(MotionEvent event) {// 触摸事件
		switch (event.getAction()) {

		case MotionEvent.ACTION_DOWN:// 手按下 对应下拉更新状态

			if (this.firstItemIndex != 0 && this.getCount() > 1) {
				return super.onTouchEvent(event);
			}
			if (firstItemIndex == 0 && !isRecored) {// 如果首item索引为0，且尚未记录startY,则在下拉时记录之，并执行isRecored = true;
				startY = (int) event.getY();
				isRecored = true;
			}
			break;

		case MotionEvent.ACTION_UP:// 手放開 对应放開更新状态
			if (this.firstItemIndex != 0 && this.getCount() > 1) {
				return super.onTouchEvent(event);
			}

			if (state != REFRESHING) {// 手放開有4个状态：下拉更新、放開更新、正在更新、完成更新。如果当前不是正在更新
				if (state == DONE) {// 如果当前是完成更新，什么都不做
				}
				if (state == PULL_To_REFRESH) {// 如果当前是下拉更新，状态设为完成更新（意即下拉更新中就放開了，实际未完成更新），执行changeHeaderViewByState()
					state = DONE;
					changeHeaderViewByState();
				}
				if (state == RELEASE_To_REFRESH) {// 如果当前是放開更新，状态设为正在更新（意即放開更新中放開手，才是真正地更新），执行changeHeaderViewByState()
					state = REFRESHING;
					changeHeaderViewByState();
					onRefresh();// 真正更新，所以执行onrefresh，执行后状态设为完成更新
				}
			}

			isRecored = false;// 手放開，则无论怎样，可以重新记录startY,因为只要手放開就认为一次更新已完成
			isBack = false;

			break;

		case MotionEvent.ACTION_MOVE:// 手拖动，拖动过程中不断地实时记录当前位置
			if (this.firstItemIndex != 0 && this.getCount() > 1) {
				return super.onTouchEvent(event);

			}

			int tempY = (int) event.getY();

			if (!isRecored && firstItemIndex == 0) {// 如果首item索引为0，且尚未记录startY,则在拖动时记录之，并执行isRecored
													// = true;
				isRecored = true;
				startY = tempY;
			}
			if (state != REFRESHING && isRecored) {// 如果状态不是正在更新，且已记录startY：tempY为拖动过程中一直在变的高度，startY为拖动起始高度
				// 可以松手去更新了
				if (state == RELEASE_To_REFRESH) {// 如果状态是放開更新
					// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
					if ((tempY - startY < headContentHeight)// 如果实时高度大于起始高度，且两者之差小于头部高度，则状态设为下拉更新
							&& (tempY - startY) > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
					}
					// 一下子推到顶了
					else if (tempY - startY <= 0) {// 如果实时高度小于等于起始高度了，则说明到顶了，状态设为完成更新
						state = DONE;
						changeHeaderViewByState();
					}
					// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					else {// 如果当前拖动过程中既没有到下拉更新的地步，也没有到完成更新（到顶）的地步，则保持放開更新状态
							// 不用进行特别的操作，只用更新paddingTop的值就行了
					}
				}
				// 还没有到达显示放開更新的时候,DONE或者是PULL_To_REFRESH状态
				if (state == PULL_To_REFRESH) {// 如果状态是下拉更新
					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if (tempY - startY >= headContentHeight) {// 如果实时高度与起始高度之差大于等于头部高度，则状态设为放開更新
						state = RELEASE_To_REFRESH;
						isBack = true;
						changeHeaderViewByState();
					}
					// 上推到顶了
					else if (tempY - startY <= 0) {// 如果实时高度小于等于起始高度了，则说明到顶了，状态设为完成更新
						state = DONE;
						changeHeaderViewByState();
					}
				}

				// done状态下
				if (state == DONE) {// 如果状态是完成更新
					if (tempY - startY > 0) {// 如果实时高度大于起始高度了，则状态设为下拉更新
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
					}
				}

				// 更新headView的size
				if (state == PULL_To_REFRESH) {// 如果状态是下拉更新，更新headview的size ?
					headView.setPadding(0, -1 * headContentHeight
							+ (tempY - startY), 0, 0);
					headView.invalidate();
				}

				// 更新headView的paddingTop
				if (state == RELEASE_To_REFRESH) {// 如果状态是放開更新，更新
													// headview的paddingtop ?
					headView.setPadding(0, tempY - startY - headContentHeight,
							0, 0);
					headView.invalidate();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:
			arrowImage.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImage.clearAnimation();
			arrowImage.startAnimation(animation);

			tipsTextview.setText("Release To Refresh...");
			break;
		case PULL_To_REFRESH:
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImage.clearAnimation();
			arrowImage.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				arrowImage.clearAnimation();
				arrowImage.startAnimation(reverseAnimation);

				tipsTextview.setText("Pull To Refresh...");
			} else {
				tipsTextview.setText("Pull To Refresh...");
			}
			break;

		case REFRESHING:
			headView.setPadding(0, 0, 0, 0);
			headView.invalidate();

			progressBar.setVisibility(View.VISIBLE);
			arrowImage.clearAnimation();
			arrowImage.setVisibility(View.GONE);
			tipsTextview.setText("Refreshing...");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			headView.invalidate();

			progressBar.setVisibility(View.GONE);
			arrowImage.clearAnimation();
			tipsTextview.setText("Pull To Refresh");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}
	
	public ImageView getArrowImage(){
		return arrowImage;
	}

	public void setOnAppendListener(OnAppendListener onAppendListener) {
		mOnAppendListener = onAppendListener;
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}

	public void onRefreshComplete() {
		state = DONE;
		lastUpdatedTextView.setText("Last Update At: " + new Date().toLocaleString());// 更新完成时，头部提醒的更新日期
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	// 此方法直接照搬自网络上的一个下拉更新的demo，此处是“估计”headView的width以及height
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

}
