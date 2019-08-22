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

import android.os.Handler;
import android.os.Message;

/**
 * CountDownTimer 的進化版，原本的 CountDownTimer 無 resume, restart, seek功能
 * 這邊友對這方做強化功能
 * @author JamesX
 * @since 2012/05/17
 */
public abstract class AdvancedCountdownTimer {

	private final long mCountdownInterval;

	private long mTotalTime;

	private long mRemainTime;

	/**
	 * 設定最大時間(finish 時間點)與每一個 tick 需要的時間間隔
	 * @param millisInFuture (long) finish 的時間點
	 * @param countDownInterval (long)
	 */
	public AdvancedCountdownTimer(long millisInFuture, long countDownInterval) {
		mTotalTime = millisInFuture;
		mCountdownInterval = countDownInterval;

		mRemainTime = millisInFuture;
	}
	
	/**
	 * 設定最大時間，也就是 finish 的時間點
	 * @param millisInFuture (long) finish 的時間點
	 */
	public void setTimerMax(long millisInFuture){
		mTotalTime = millisInFuture;
	}

	/**
	 * 尋找時間點，可以找尋計時器的某時間點
	 * @param value (int) 計時器的某時間點
	 */
	public final void seek(int value) {
		synchronized (AdvancedCountdownTimer.this) {
			mRemainTime = ((100 - value) * mTotalTime) / 100;
		}
	}
	
	/**
	 * 取消計時器
	 */
	public final void cancel() {
		mHandler.removeMessages(MSG_RUN);
		mHandler.removeMessages(MSG_PAUSE);
	}

	/**
	 * 回復計時器(需執行過 pause())
	 */
	public final void resume() {
		mHandler.removeMessages(MSG_PAUSE);
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
	}
	
	/**
	 * 重來計時器
	 */
	public final void restart() {
		synchronized (AdvancedCountdownTimer.this) {
			mRemainTime = mTotalTime;
		}
		mHandler.removeMessages(MSG_RUN);
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_RUN));
	}

	/**
	 * 暫停計時器
	 */
	public final void pause() {
		mHandler.removeMessages(MSG_RUN);
		mHandler.sendMessageAtFrontOfQueue(mHandler.obtainMessage(MSG_PAUSE));
	}

	
	public synchronized final AdvancedCountdownTimer start() {
		if (mRemainTime <= 0) {
			onFinish();
			return this;
		}
		mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_RUN),
				mCountdownInterval);
		return this;
	}

	public abstract void onTick(long millisUntilFinished, int percent);

	
	public abstract void onFinish();

	private static final int MSG_RUN = 1;
	private static final int MSG_PAUSE = 2;
	private static final int MSG_RESTART = 3;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			synchronized (AdvancedCountdownTimer.this) {
				if (msg.what == MSG_RUN) {
					mRemainTime = mRemainTime - mCountdownInterval;

					if (mRemainTime <= 0) {
						onFinish();
					} else if (mRemainTime < mCountdownInterval) {
						sendMessageDelayed(obtainMessage(MSG_RUN), mRemainTime);
					} else {

						onTick(mRemainTime, new Long(100
								* (mTotalTime - mRemainTime) / mTotalTime)
								.intValue());

					
						sendMessageDelayed(obtainMessage(MSG_RUN),
								mCountdownInterval);
					}
				}
				else if (msg.what == MSG_PAUSE) {

				}
				else if (msg.what == MSG_RESTART) {

				}
			}
		}
	};
}

