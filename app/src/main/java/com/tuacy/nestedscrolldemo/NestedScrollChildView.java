package com.tuacy.nestedscrolldemo;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 嵌套滑动的子View 一般使用方法（用scroll来举例）
 * 1. 实现NestedScrollingChild接口
 * 2. 定义NestedScrollingChildHelper变量
 * 3. 在实现的NestedScrollingChild每个接口中调用NestedScrollingChildHelper对应的函数
 * 4. setNestedScrollingEnabled(true); 一般在初始化里面调用设置可以嵌套滑动
 * 4. onTouchEvent 或者 dispatchTouchEvent 方法里面case ACTION_DOWN 调用startNestedScroll函数 告诉父View开始嵌套滑动
 * 5. onTouchEvent 或者 dispatchTouchEvent 方法里面case ACTION_MOVE 调用dispatchNestedPreScroll或者dispatchNestedScroll 这个就视情况而定了
 * 		告诉父View滑动的情况。
 * 6. onTouchEvent 或者 dispatchTouchEvent 方法里面case ACTION_UP 调用stopNestedScroll 告诉父View结束嵌套滑动
 * 7. 重写onDetachedFromWindow方法，调用NestedScrollingChildHelper的onDetachedFromWindow方法
 */
public class NestedScrollChildView extends View implements NestedScrollingChild {

	private NestedScrollingChildHelper nestedScrollChildHelper;
	private int mLastX;
	private int mLastY;
	private int[] mConsumed       = new int[2];//消耗的距离
	private int[] mOffsetInWindow = new int[2];//窗口偏移

	public NestedScrollChildView(Context context) {
		this(context, null);
	}

	public NestedScrollChildView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NestedScrollChildView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		nestedScrollChildHelper = new NestedScrollingChildHelper(this);
		setNestedScrollingEnabled(true);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastX = x;
				mLastY = y;
				// 告诉父View开始嵌套滑动，父View里面会调用到相关的函数
				startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL | ViewCompat.SCROLL_AXIS_VERTICAL);
				break;
			case MotionEvent.ACTION_MOVE:
				int offsetX = x - mLastX;
				int offsetY = y - mLastY;
				if (dispatchNestedPreScroll(offsetX, offsetY, mConsumed, mOffsetInWindow)) {
					//重新调整下移动的距离，因为嵌套对应的父Vie有做相应的滑动事件。所以减掉父View消耗的距离
					offsetX = offsetX - mConsumed[0];
					offsetY = offsetY - mConsumed[1];
				}
				offsetLeftAndRight(offsetX);
				offsetTopAndBottom(offsetY);
				break;
			case MotionEvent.ACTION_UP:
				stopNestedScroll();
				break;
		}
		return true;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		nestedScrollChildHelper.onDetachedFromWindow();
	}

	@Override
	public void setNestedScrollingEnabled(boolean enabled) {
		nestedScrollChildHelper.setNestedScrollingEnabled(enabled);
	}

	@Override
	public boolean isNestedScrollingEnabled(){
		return nestedScrollChildHelper.isNestedScrollingEnabled();
	}

	@Override
	public boolean startNestedScroll(int axes){
		return nestedScrollChildHelper.startNestedScroll(axes);
	}

	@Override
	public void stopNestedScroll(){
		nestedScrollChildHelper.stopNestedScroll();
	}

	@Override
	public boolean hasNestedScrollingParent(){
		return nestedScrollChildHelper.hasNestedScrollingParent();
	}

	@Override
	public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
										int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow){
		return nestedScrollChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);

	}

	@Override
	public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow){
		return nestedScrollChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
	}

	@Override
	public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed){
		return nestedScrollChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
	}

	@Override
	public boolean dispatchNestedPreFling(float velocityX, float velocityY){
		return nestedScrollChildHelper.dispatchNestedPreFling(velocityX, velocityY);
	}


}
