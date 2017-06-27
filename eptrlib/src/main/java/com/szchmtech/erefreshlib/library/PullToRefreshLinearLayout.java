/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.szchmtech.erefreshlib.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.szchmtech.erefreshlib.R;

public class PullToRefreshLinearLayout extends PullToRefreshBase<LinearLayout> {

	private static final OnRefreshListener<LinearLayout> defaultOnRefreshListener = new OnRefreshListener<LinearLayout>() {

		@Override
		public void onRefresh(PullToRefreshBase<LinearLayout> refreshView) {
//			refreshView.getRefreshableView().reload();

		}

	};

//	private final WebChromeClient defaultWebChromeClient = new WebChromeClient() {
//
//		@Override
//		public void onProgressChanged(WebView view, int newProgress) {
//			if (newProgress == 100) {
//				onRefreshComplete();
//			}
//		}
//	};

	public PullToRefreshLinearLayout(Context context) {
		super(context);
		setOnRefreshListener(defaultOnRefreshListener);
	}

	public PullToRefreshLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnRefreshListener(defaultOnRefreshListener);
	}

	public PullToRefreshLinearLayout(Context context, Mode mode) {
		super(context, mode);
		setOnRefreshListener(defaultOnRefreshListener);
	}

	public PullToRefreshLinearLayout(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
		setOnRefreshListener(defaultOnRefreshListener);
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected LinearLayout createRefreshableView(Context context, AttributeSet attrs) {
		LinearLayout linearLayout;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			linearLayout = new InternalWebViewSDK9(context, attrs);
		} else {
			linearLayout = new LinearLayout(context, attrs);
		}
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setId(R.id.linearlayout);
		return linearLayout;
	}

	@Override
	protected boolean isReadyForPullStart() {
		if(mRefreshableView == null){
			return false;
		}
		ScrollView scrollView = null;
		//找出ScrollView
		int childCount = mRefreshableView.getChildCount();
		if(childCount < 1){
			return false;
		}
		View view = mRefreshableView.getChildAt(1);
		if(view == null){
			return false;
		}
		if(view instanceof ScrollView){
			scrollView = (ScrollView) view;
		}else{
			for(int i = 0;i < childCount; i++){
				View childView = mRefreshableView.getChildAt(i);
				Log.e("abc",childView.toString());
				if(childView instanceof ScrollView){
					scrollView = (ScrollView) childView;
				}
			}
		}
		if(scrollView == null){
			return false;
		}
//		Log.e("aaa",""+mInitialMotionRawY);
		return scrollView.getScrollY() == 0 || mInitialMotionRawY < scrollView.getTop();
	}



//	@Override
//	protected boolean isReadyForPullStart() {
//		if(mRefreshableView == null){
//			return false;
//		}
//
//		ScrollView scrollView = null;
//		//找出ScrollView
//		ViewGroup framlayout = (ViewGroup) getChildAt(1);
//		int childCount = framlayout.getChildCount();
//		if(childCount < 1){
//			return false;
//		}
//		View view = framlayout.getChildAt(1);
//		if(view == null){
//			return false;
//		}
//		if(view instanceof ScrollView){
//			scrollView = (ScrollView) view;
//		}else{
//			for(int i = 0;i < childCount; i++){
//				View childView = framlayout.getChildAt(i);
//				Log.e("abc",childView.toString());
//				if(childView instanceof ScrollView){
//					scrollView = (ScrollView) childView;
//				}
//			}
//		}
//		if(scrollView == null){
//			return false;
//		}
//		return scrollView.getScrollY() == 0;
//	}

	@Override
	protected boolean isReadyForPullEnd() {
//		float exactContentHeight = (float) Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
//		return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());
//
//		float exactContentHeight = (float) Math.floor(mRefreshableView.getContentHeight() * mRefreshableView.getScale());
//		return mRefreshableView.getScrollY() >= (exactContentHeight - mRefreshableView.getHeight());

		return false;
	}

	@TargetApi(9)
	final class InternalWebViewSDK9 extends LinearLayout {

		// WebView doesn't always scroll back to it's edge so we add some
		// fuzziness
		static final int OVERSCROLL_FUZZY_THRESHOLD = 2;

		// WebView seems quite reluctant to overscroll so we use the scale
		// factor to scale it's value
		static final float OVERSCROLL_SCALE_FACTOR = 1.5f;

		public InternalWebViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
									   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

////			// Does all of the hard work...
//			OverscrollHelper.overScrollBy(PullToRefreshLinearLayout.this, deltaX, scrollX, deltaY, scrollY,
//					getScrollRange(), OVERSCROLL_FUZZY_THRESHOLD, OVERSCROLL_SCALE_FACTOR, isTouchEvent);

			return returnValue;
		}

//		private int getScrollRange() {
//			int contentHeight = 0;
//			if (getChildCount() > 0) {
//				for(int i=0;i<getChildCount();i++){
//					contentHeight += getChildAt(i).getHeight();
//				}
//				contentHeight = Math.max(0, contentHeight - (getHeight() - getPaddingBottom() - getPaddingTop()));
//			}
//			return contentHeight;
//		}
	}
}
