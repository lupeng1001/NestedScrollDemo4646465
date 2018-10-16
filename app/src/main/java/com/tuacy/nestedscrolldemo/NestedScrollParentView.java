package com.tuacy.nestedscrolldemo;

import android.content.Context;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 嵌套滑动的父View 一般使用方法
 * 1. 实现NestedScrollingParent接口
 * 2. 定义NestedScrollingParentHelper变量
 * 3. 在实现的NestedScrollingParent几个接口中(onNestedScrollAccepted, onStopNestedScroll, getNestedScrollAxes)
 * 		调用NestedScrollingParentHelper对应的函数
 * 4. 视情况而定onNestedScroll onNestedPreScroll onNestedFling onNestedPreFling 做相应的处理
 */
public class NestedScrollParentView extends FrameLayout implements NestedScrollingParent {

	private NestedScrollingParentHelper mNestedScrollParentHelper;

	public NestedScrollParentView(Context context) {
		this(context, null);
	}

	public NestedScrollParentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NestedScrollParentView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs, defStyleAttr);
	}

	private void init(Context context, AttributeSet attrs, int defStyleAttr) {
		mNestedScrollParentHelper = new NestedScrollingParentHelper(this);
	}

	@Override
	public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
		// 允许嵌套滑动
		return true;
	}

	@Override
	public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes){
		mNestedScrollParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
	}

	@Override
	public void onStopNestedScroll(View target) {
		mNestedScrollParentHelper.onStopNestedScroll(target);
	}

	@Override
	public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
							   int dxUnconsumed, int dyUnconsumed) {
		// 不考虑这个情况
	}

	// 子View滑动的时候会自动调用这个函数
	@Override
	public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
		if (dx >= 0) {
			// 往右
			if (target.getRight() + dx > getWidth()) {
				// 超过右边界了移动该ViewGroup
				int offsetX = target.getRight() + dx - getWidth();
				// 做相应的移动
				offsetLeftAndRight(offsetX);
				// 告诉子View父View消耗的距离
				consumed[0] = offsetX;
			}
		} else {
			// 往左
			if (target.getLeft() + dx < 0) {
				// 超过了左边界
				int offsetX = target.getLeft() + dx;
				offsetLeftAndRight(offsetX);
				consumed[0] = offsetX;
			}
		}

		if (dy >= 0) {
			// 往下
			if (target.getBottom() + dy > getHeight()) {
				int offsetY = target.getBottom() + dy - getHeight();
				offsetTopAndBottom(offsetY);
				consumed[1] = offsetY;
			}
		} else {
			// 往上
			if (target.getTop() + dy < 0) {
				int offsetY = target.getTop() + dy;
				offsetTopAndBottom(offsetY);
				consumed[1] = offsetY;
			}
		}

	}

	@Override
	public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
		// 先不考虑 fling的情况
		return false;
	}

	@Override
	public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
		// 先不考虑 fling的情况
		return false;
	}

	@Override
	public int getNestedScrollAxes() {
		return mNestedScrollParentHelper.getNestedScrollAxes();
	}
}
