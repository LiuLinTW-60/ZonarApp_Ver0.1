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

import java.util.ArrayList;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ListView;

/**
 * 客製化 adapter 的內容
 * 
 * @author JamesX
 * @param <T>
 * @param <V>
 */
public abstract class FreeAdapter<T, V extends View> extends BaseAdapter {
	public static final int TO_WIDTH = 0x1000;
	public static final int TO_HEIGHT = 0x1001;
	private Context mContext;
	protected int monitorWidth, monitorHeight;
	private ArrayList<T> arrayList;
	protected int picSize = 640;
	protected int defaultSize;

	public abstract V initView(int position);

	public abstract void setView(int position, V convertView);

	public FreeAdapter(Context context, T[] array) {
		this.mContext = context;
		this.arrayList = new ArrayList<T>();
		for (T target : array) {
			arrayList.add(target);
		}
		setup();
	}

	public FreeAdapter(Context context, ArrayList<T> arrayList) {
		this.mContext = context;
		this.arrayList = arrayList;
		setup();
	}

	private void setup() {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;
		defaultSize = monitorWidth;
	}

	public FreeAdapter(Context context, int toWich, ArrayList<T> arrayList) {
		this.mContext = context;
		this.arrayList = arrayList;

		setDatum(toWich);
	}

	public Context getContext() {
		return mContext;
	}

	public void addItems(ArrayList<T> arrayList) {
		this.arrayList.addAll(arrayList);
		notifyDataSetChanged();
	}

	public ArrayList<T> getItems() {
		return arrayList;
	}

	/**
	 * @param toWich
	 */
	public void setDatum(int toWich) {
		setPicSize(-1, -1, toWich);
	}

	/**
	 * 預設螢幕畫面是640*960
	 * 
	 * @param picSize (int) 預設為640
	 */
	public void setPicSize(int picSize) {
		setPicSize(picSize, -1, TO_WIDTH);
	}

	/**
	 * 預設螢幕畫面是640*960，
	 * 
	 * @param layoutWidth (int) 預設為640
	 * @param layoutHeight (int) 預設為960
	 */
	public void setPicSize(int picWidth, int picHeight) {
		setPicSize(picWidth, picHeight, TO_WIDTH);
	}

	/**
	 * 預設螢幕畫面是640*960，以螢幕寬度為基底
	 * 
	 * @param picWidth (int) 預設為640
	 * @param picHeight (int) 預設為960
	 * @param toWich (int) 可以設定為 TO_WIDTH 或是 TO_HEIGHT
	 */
	public void setPicSize(int picWidth, int picHeight, int toWich) {
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		monitorWidth = dm.widthPixels;
		monitorHeight = dm.heightPixels;

		if (toWich == TO_WIDTH) {
			defaultSize = monitorWidth;
			if (picWidth != -1) {
				this.picSize = picWidth;
			}
		}
		else if (toWich == TO_HEIGHT) {
			defaultSize = monitorHeight;
			if (picHeight != -1) {
				this.picSize = picHeight;
			}
		}
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public T getItem(int position) {
		return arrayList.get(position);
	}

	public void addItem(T object) {
		arrayList.add(object);
		notifyDataSetChanged();
	}

	public void setItem(int position, T object) {
		arrayList.set(position, object);
		notifyDataSetChanged();
	}

	public void removeItem(int position) {
		arrayList.remove(position);
		notifyDataSetChanged();
	}

	public void removeItem(T object) {
		arrayList.remove(object);
		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View item, ViewGroup parent) {
		if (item == null) {
			item = initView(position);

			if (item.getLayoutParams() == null) {
				if (parent instanceof ListView) {
					item.setLayoutParams(new ListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
				else if (parent instanceof GridView) {
					item.setLayoutParams(new GridView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
				else if (parent instanceof Gallery) {
					item.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}
				else {
					item.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				}

			}

			int width = item.getLayoutParams().width;
			if (width != LayoutParams.WRAP_CONTENT && width != LayoutParams.MATCH_PARENT) {
				item.getLayoutParams().width = (int) (0.5f + width * defaultSize / (float) picSize);
			}

			int height = item.getLayoutParams().height;
			if (height != LayoutParams.WRAP_CONTENT && height != LayoutParams.MATCH_PARENT) {
				item.getLayoutParams().height = (int) (0.5f + height * defaultSize / (float) picSize);
			}
		}
		setView(position, (V) item);

		return item;
	}

	protected int resize(int size) {
		return (int) (0.5f + size * defaultSize / (float) picSize);
	}
}
