package com.example.downrefresh;

import java.text.SimpleDateFormat;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;

/**
 * 包含下拉刷新功能的ListView
 * @author poplar
 *
 */
public class RefreshListView extends ListView implements OnScrollListener{
	private static final String TAG = "RefreshListView";
	private View mHeaderView; // 头布局
	private float downY; // 按下的y坐标
	private float moveY; // 移动后的y坐标
	private int mHeaderViewHeight; // 头布局高度
	public static final int PULL_TO_REFRESH = 0;// 下拉刷新
	public static final int RELEASE_REFRESH = 1;// 释放刷新
	public static final int REFRESHING = 2; // 刷新中
	private int currentState = PULL_TO_REFRESH; // 当前刷新模式
	private RotateAnimation rotateUpAnim; // 箭头向上动画
	private RotateAnimation rotateDownAnim; // 箭头向下动画
	private View mArrowView;		// 箭头布局
	private TextView mTitleText;	// 头布局标题
	private ProgressBar pb;			// 进度指示器
	private TextView mLastRefreshTime; // 最后刷新时间
	private OnRefreshListener mListener; // 刷新监听
	private View mFooterView;		// 脚布局
	private int mFooterViewHeight;	// 脚布局高度
	private boolean isLoadingMore; // 是否正在加载更多

	//数据是否加载完毕
	private boolean data_load_finish;
	private LinearLayout load_more_view_layout;//数据加载中显示的view
	private LinearLayout load_finish_view_layout;//数据加载完毕显示的view

	//下拉刷新拖动偏移量(px)
	private int slide_px = 60;

	//新增字段，是否可以下拉加载更多
	private boolean can_pull_refresh;




	public boolean isData_load_finish() {
		return data_load_finish;
	}

	public void setData_load_finish(boolean data_load_finish) {
		this.data_load_finish = data_load_finish;
	}

	public RefreshListView(Context context) {
		super(context);
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化头布局, 脚布局
	 * 滚动监听
	 */
	private void init() {
		if(can_pull_refresh){
			initHeaderView();
		}
		initAnimation();
		initFooterView();
		setOnScrollListener(this);
	}

	/**
	 * 初始化脚布局
	 */
	private void initFooterView() {
		mFooterView = View.inflate(getContext(), R.layout.layout_footer_list, null);
		load_more_view_layout = (LinearLayout)mFooterView.findViewById(R.id.load_more_view_layout);
		load_finish_view_layout = (LinearLayout)mFooterView.findViewById(R.id.load_finish_view_layout);
		mFooterView.measure(0, 0);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		// 隐藏脚布局
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		addFooterView(mFooterView);
	}

	/**
	 * 初始化头布局的动画
	 */
	private void initAnimation() {
		// 向上转, 围绕着自己的中心, 逆时针旋转0 -> -180.
		rotateUpAnim = new RotateAnimation(0f, -180f, 
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateUpAnim.setDuration(300);
		rotateUpAnim.setFillAfter(true); // 动画停留在结束位置

		// 向下转, 围绕着自己的中心, 逆时针旋转 -180 -> -360
		rotateDownAnim = new RotateAnimation(-180f, -360,
				Animation.RELATIVE_TO_SELF, 0.5f, 
				Animation.RELATIVE_TO_SELF, 0.5f);
		rotateDownAnim.setDuration(300);
		rotateDownAnim.setFillAfter(true); // 动画停留在结束位置
		
	}

	/**
	 * 初始化头布局
	 */
	private void initHeaderView() {
		LayoutInflater mInflater = LayoutInflater.from(getContext());
		View local_view = mInflater.inflate(R.layout.test_head_view,null);
		//local_view先主动测量自己
		local_view.measure(0,0);
		Log.e(TAG,"****local_view.getMeasuredHeight()="+local_view.getMeasuredHeight());
		Log.e(TAG,"****local_view.getMeasuredWidth()="+local_view.getMeasuredWidth());

		mHeaderView = View.inflate(getContext(), R.layout.layout_header_list, null);
		mArrowView = mHeaderView.findViewById(R.id.iv_arrow);
		pb = (ProgressBar) mHeaderView.findViewById(R.id.pb_bar);
		mTitleText = (TextView) mHeaderView.findViewById(R.id.tv_refresh);
		mLastRefreshTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
		// 在设置数据适配器之前执行添加 头布局/脚布局 的方法.(放在前面便于理解)
		addHeaderView(mHeaderView);
		// 提前主动测量宽高
		mHeaderView.measure(0, 0);// 按照设置的规则测量
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		Log.e(TAG,"****mHeaderViewHeight="+mHeaderViewHeight);
		System.out.println(" measuredHeight: " + mHeaderViewHeight);
		// 设置内边距, 可以隐藏当前控件 , -自身高度
		mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
		

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
//		// 判断滑动距离, 给Header设置paddingTop
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			downY = ev.getY();
//			System.out.println("downY: " + downY);
//
//			break;
//		case MotionEvent.ACTION_MOVE:
//			moveY = ev.getY();
//			System.out.println("moveY: " + moveY);
//			float offset = moveY - downY; // 移动的偏移量
//			// 只有 偏移量>0, 并且当前第一个可见条目索引是0, 才放大头部
//			if(offset > 0 && getFirstVisiblePosition() == 0){
//
////			int paddingTop = -自身高度 + 偏移量
//
//				int paddingTop = (int) (- mHeaderViewHeight + offset);
//				mHeaderView.setPadding(0, paddingTop, 0, 0);
//
//				if(paddingTop >= slide_px && currentState != RELEASE_REFRESH){// 头布局完全显示
//					System.out.println("切换成释放刷新模式: " + paddingTop);
//					// 切换成释放刷新模式
//					currentState = RELEASE_REFRESH;
//					updateHeader(); // 根据最新的状态值更新头布局内容
//				}else if(paddingTop < 0 && currentState != PULL_TO_REFRESH){ // 头布局不完全显示
//					System.out.println("切换成下拉刷新模式: " + paddingTop);
//					// 切换成下拉刷新模式
//					currentState = PULL_TO_REFRESH;
//					updateHeader(); // 根据最新的状态值更新头布局内容
//				}
//
//				return true; // 当前事件被我们处理并消费
//			}
//
//			break;
//		case MotionEvent.ACTION_UP:
//
//			// 根据刚刚设置状态
//			if(currentState == PULL_TO_REFRESH){
////			- paddingTop < 0 不完全显示, 恢复
//				mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);
//			}else if(currentState == RELEASE_REFRESH){
////			- paddingTop >= 0 完全显示, 执行正在刷新...
//				mHeaderView.setPadding(0, 0, 0, 0);
//				currentState = REFRESHING;
//				updateHeader();
//			}
//			break;
//
//		default:
//			break;
//		}
		
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据状态更新头布局内容
	 */
	private void updateHeader() {
		switch (currentState) {
		case PULL_TO_REFRESH: // 切换回下拉刷新
			// 做动画, 改标题
			mArrowView.startAnimation(rotateDownAnim);
			mTitleText.setText("下拉刷新");
			
			break;
		case RELEASE_REFRESH: // 切换成释放刷新
			// 做动画, 改标题
			mArrowView.startAnimation(rotateUpAnim);
			mTitleText.setText("释放刷新");
			
			break;
		case REFRESHING: // 刷新中...
			mArrowView.clearAnimation();
			mArrowView.setVisibility(View.INVISIBLE);
			pb.setVisibility(View.VISIBLE);
			mTitleText.setText("正在刷新中...");
			
			if(mListener != null){
				mListener.onRefresh(); // 通知调用者, 让其到网络加载更多数据.
			}
			
			break;

		default:
			break;
		}
	}

	/**
	 * 恢复底部view状态
	 */
	public void resetFootView(){
		mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
		load_more_view_layout.setVisibility(View.VISIBLE);
		load_finish_view_layout.setVisibility(View.GONE);
	}

	
	/**
	 * 刷新结束, 恢复界面效果
	 */
	public void onRefreshComplete() {
		if(isLoadingMore){
			// 加载更多
			if(isData_load_finish()){//数据加载完毕的情况
				load_more_view_layout.setVisibility(View.GONE);
				load_finish_view_layout.setVisibility(View.VISIBLE);
				mFooterView.setPadding(0, 0, 0, 0);
			}else{
				mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);
				load_more_view_layout.setVisibility(View.VISIBLE);
				load_finish_view_layout.setVisibility(View.GONE);
			}
			isLoadingMore = false;


		}else {
			// 下拉刷新
			currentState = PULL_TO_REFRESH;
			mTitleText.setText("下拉刷新"); // 切换文本
			mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);// 隐藏头布局
			pb.setVisibility(View.INVISIBLE);
			mArrowView.setVisibility(View.VISIBLE);
			String time = getTime();
			mLastRefreshTime.setText("最后刷新时间: " + time);
		}
		
	}

	private String getTime() {
		long currentTimeMillis = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(currentTimeMillis);
	}

	public interface OnRefreshListener{
		
		void onRefresh(); // 下拉刷新
		
		void onLoadMore();// 加载更多
	}
	
	public void setRefreshListener(OnRefreshListener mListener) {
		this.mListener = mListener;
	}

//    public static int SCROLL_STATE_IDLE = 0; // 空闲
//    public static int SCROLL_STATE_TOUCH_SCROLL = 1; // 触摸滑动
//    public static int SCROLL_STATE_FLING = 2; // 滑翔
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 状态更新的时候
		System.out.println("scrollState: " + scrollState);
		if(isLoadingMore){
			return; // 已经在加载更多.返回
		}
		
		// 最新状态是空闲状态, 并且当前界面显示了所有数据的最后一条. 加载更多
		if(scrollState == SCROLL_STATE_IDLE
				&& getLastVisiblePosition() >= (getCount() - 1)
				&& !isData_load_finish()){
			isLoadingMore = true;
			System.out.println("scrollState: 开始加载更多");
			mFooterView.setPadding(0, 0, 0, 0);
			
			//setSelection(getCount()); // 跳转到最后一条, 使其显示出加载更多.

			if(mListener != null){
				mListener.onLoadMore();
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 滑动过程
	}
}

