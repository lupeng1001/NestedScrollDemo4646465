package com.example.downrefresh.new_part_views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.downrefresh.new_part_views.BaseRefreshListener;
import com.example.downrefresh.new_part_views.State;
import com.jwenfeng.library.pulltorefresh.util.DisplayUtil;


/**
 * 通用的下拉刷新上拉加载更多主view 支持listview recycleview scrollview
 * @author lupeng
 */
public class PullToRefreshLayout extends FrameLayout {
    private static final String TAG = "PullToRefreshLayout";

    private HeadView mHeaderView;
    private FooterView mFooterView;
    private View mChildView;

    private static final long ANIM_TIME = 250;
    private static int HEAD_HEIGHT = 55;
    private static int FOOT_HEIGHT = 55;

    private static int head_height;
    private static int head_height_2;
    private static int foot_height;
    private static int foot_height_2;

    private float mTouchY;
    private float mCurrentY;

    private boolean canLoadMore = true;
    private boolean canRefresh = true;
    private boolean isRefresh;
    private boolean isLoadMore;

    //滑动的最小距离
    private int mTouchSlope;

    //设置下拉阻尼和上拉阻尼
    private float pull_down_rato = 3.0f;//下拉阻尼
    private float pull_up_rato = 1.0f;//上拉阻尼


    private BaseRefreshListener refreshListener;

    public void setRefreshListener(BaseRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 计算控件宽高
     *
     * @param child
     */
    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void cal() {
        head_height = DisplayUtil.dp2Px(getContext(), HEAD_HEIGHT);
        foot_height = DisplayUtil.dp2Px(getContext(), FOOT_HEIGHT);
        head_height_2 = DisplayUtil.dp2Px(getContext(), HEAD_HEIGHT + 1);
        foot_height_2 = DisplayUtil.dp2Px(getContext(), FOOT_HEIGHT * 2);
        mTouchSlope = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.e(TAG,"****mTouchSlope="+mTouchSlope);
    }

    private void init() {
        cal();
        int count = getChildCount();
        if (count != 1) {
            new IllegalArgumentException("child only can be one");
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mChildView = getChildAt(0);
        addHeadView();
        addFooterView();
    }

    private void addHeadView() {
        if (mHeaderView == null) {
            mHeaderView = new HeadRefreshView(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        mHeaderView.getView().setLayoutParams(layoutParams);
        if (mHeaderView.getView().getParent() != null)
            ((ViewGroup) mHeaderView.getView().getParent()).removeAllViews();
        addView(mHeaderView.getView(), 0);
    }

    private void addFooterView() {
        if (mFooterView == null) {
            mFooterView = new LoadMoreView(getContext());
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        layoutParams.gravity = Gravity.BOTTOM;
        mFooterView.getView().setLayoutParams(layoutParams);
        if (mFooterView.getView().getParent() != null)
            ((ViewGroup) mFooterView.getView().getParent()).removeAllViews();
        addView(mFooterView.getView());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!canLoadMore && !canRefresh) return super.onInterceptTouchEvent(ev);
        if (isRefresh || isLoadMore) return true;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();
                mCurrentY = mTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float dy = currentY - mCurrentY;
                if (canRefresh) {
                    boolean canChildScrollUp = canChildScrollUp();
                    if (dy > mTouchSlope && !canChildScrollUp) {
                        mHeaderView.begin();
                        return true;
                    }
                }
                if (canLoadMore) {
                    boolean canChildScrollDown = canChildScrollDown();
                    if (dy < -mTouchSlope && !canChildScrollDown) {
                        mFooterView.begin();
                        return true;
                    }
                }
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefresh || isLoadMore) return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurrentY = event.getY();
                //设置下拉阻尼为3.0f
                float dura = 0f;
                if(mCurrentY - mTouchY>0){
                    //下拉
                    dura = (mCurrentY - mTouchY)/pull_down_rato;
                }else if(mCurrentY - mTouchY<0){
                    //上拉
                    dura = (mCurrentY - mTouchY)/pull_up_rato;
                }
                //下拉delta值大于0且支持下拉刷新
                if (dura > 0 && canRefresh) {
                    dura = Math.min(head_height_2, dura);
                    dura = Math.max(0, dura);
                    mHeaderView.getView().getLayoutParams().height = (int) dura;
                    ViewCompat.setTranslationY(mChildView, dura);
                    //view重新layout
                    requestLayout();
                    //下拉进度的view处理
                    mHeaderView.progress(dura, head_height);
                } else {
                    if (canLoadMore) {
                        dura = Math.min(foot_height_2, Math.abs(dura));
                        dura = Math.max(0, Math.abs(dura));
                        mFooterView.getView().getLayoutParams().height = (int) dura;
                        ViewCompat.setTranslationY(mChildView, -dura);
                        requestLayout();
                        mFooterView.progress(dura, foot_height);
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float currentY = event.getY();
                float dy2 = 0f;
                if(mCurrentY - mTouchY>0){
                    //下拉
                    dy2 = (currentY - mTouchY)/pull_down_rato;
                }else if(mCurrentY - mTouchY<0){
                    //上拉
                    dy2 = (currentY - mTouchY)/pull_up_rato;
                }
                final int dy1 = (int) dy2;
                if (dy1 > 0 && canRefresh) {
                    if (dy1 >= head_height) {
                        createAnimatorTranslationY(State.REFRESH,
                                dy1 > head_height_2 ? head_height_2 : dy1, head_height,
                                new CallBack() {
                                    @Override
                                    public void onSuccess() {
                                        isRefresh = true;
                                        if (refreshListener != null) {
                                            refreshListener.refresh();
                                        }
                                        mHeaderView.loading();
                                    }
                                });
                    } else if (dy1 > 0 && dy1 < head_height) {
                        setFinish(dy1, State.REFRESH);
                        mHeaderView.normal();
                    }
                } else {
                    if (canLoadMore) {
                        if (Math.abs(dy1) >= foot_height) {
                            createAnimatorTranslationY(State.LOADMORE, Math.abs(dy1) > foot_height_2 ? foot_height_2 : Math.abs(dy1), foot_height, new CallBack() {
                                @Override
                                public void onSuccess() {
                                    isLoadMore = true;
                                    if (refreshListener != null) {
                                        refreshListener.loadMore();
                                    }
                                    mFooterView.loading();
                                }
                            });
                        } else {
                            setFinish(Math.abs(dy1), State.LOADMORE);
                            mFooterView.normal();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private boolean canChildScrollDown() {
        if (mChildView == null) {
            return false;
        }

        return ViewCompat.canScrollVertically(mChildView, 1);
    }

    private boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        return ViewCompat.canScrollVertically(mChildView, -1);
    }

    /**
     * 创建动画
     */
    public void createAnimatorTranslationY(@State.REFRESH_STATE final int state, final int start, final int purpose, final CallBack callBack) {
        final ValueAnimator anim;
        anim = ValueAnimator.ofInt(start, purpose);
        anim.setDuration(ANIM_TIME);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                if (state == State.REFRESH) {
                    mHeaderView.getView().getLayoutParams().height = value;
                    ViewCompat.setTranslationY(mChildView, value);
                    if (purpose == 0) { //代表结束加载
                        mHeaderView.finishing(value, head_height_2);
                    } else {
                        mHeaderView.progress(value, head_height);
                    }
                } else {
                    mFooterView.getView().getLayoutParams().height = value;
                    ViewCompat.setTranslationY(mChildView, -value);
                    if (purpose == 0) { //代表结束加载
                        mFooterView.finishing(value, head_height_2);
                    } else {
                        mFooterView.progress(value, foot_height);
                    }
                }
                if (value == purpose) {
                    if (callBack != null)
                        callBack.onSuccess();
                }
                requestLayout();


            }

        });
        anim.start();
    }


    /**
     * 结束下拉刷新
     */
    private void setFinish(int height, @State.REFRESH_STATE final int state) {
        createAnimatorTranslationY(state, height, 0, new CallBack() {
            @Override
            public void onSuccess() {
                if (state == State.REFRESH) {
                    isRefresh = false;
                    mHeaderView.normal();

                } else {
                    isLoadMore = false;
                    mFooterView.normal();
                }
            }
        });
    }

    private void setFinish(@State.REFRESH_STATE int state) {
        if (state == State.REFRESH) {
            if (mHeaderView != null && mHeaderView.getView().getLayoutParams().height > 0 && isRefresh) {
                setFinish(head_height, state);
            }
        } else {
            if (mFooterView != null && mFooterView.getView().getLayoutParams().height > 0 && isLoadMore) {
                setFinish(foot_height, state);
            }
        }
    }

    public interface CallBack {
        void onSuccess();
    }


    /**
     * 结束刷新
     * */
    public void finishRefresh(){
        setFinish(State.REFRESH);
    }

    /**
     * 结束加载更多
     * */
    public void finishLoadMore(){
        setFinish(State.LOADMORE);
    }

    /**
     * 设置是否启用加载更多
     * */
    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    /**
     * 设置是下拉刷新头部
     * @param mHeaderView 需实现 HeadView 接口
     * */
    public void setHeaderView(HeadView mHeaderView) {
        this.mHeaderView = mHeaderView;
    }

    /**
     * 设置是下拉刷新尾部
     * @param mFooterView 需实现 FooterView 接口
     * */
    public void setFooterView(FooterView mFooterView) {
        this.mFooterView = mFooterView;
    }


    /**
     * 设置刷新控件的高度
     * @param dp 单位为dp
     */
    public void setHeadHeight(int dp){
        head_height = DisplayUtil.dp2Px(getContext(),dp);
    }

    /**
     * 设置加载更多控件的高度
     * @param dp 单位为dp
     */
    public void setFootHeight(int dp){
        foot_height = DisplayUtil.dp2Px(getContext(),dp);
    }

    /**
     * 同时设置加载更多控件和刷新控件的高度
     * @param dp 单位为dp
     */
    public void setAllHeight(int dp){
        head_height = DisplayUtil.dp2Px(getContext(),dp);
        foot_height = DisplayUtil.dp2Px(getContext(),dp);
    }

    /**
     * 同时设置加载更多控件和刷新控件的高度
     * @param refresh  刷新控件的高度 单位为dp
     * @param loadMore 加载控件的高度 单位为dp
     */
    public void setAllHeight(int refresh,int loadMore){
        head_height = DisplayUtil.dp2Px(getContext(),refresh);
        foot_height = DisplayUtil.dp2Px(getContext(),loadMore);
    }

    /**
     * 设置刷新控件的下拉的最大高度 且必须大于本身控件的高度  最佳为2倍
     * @param dp 单位为dp
     */
    public void setMaxHeadHeight(int dp){
        if(head_height >= DisplayUtil.dp2Px(getContext(),dp)){
            return;
        }
        head_height_2 = DisplayUtil.dp2Px(getContext(),dp);
    }

    /**
     * 设置加载更多控件的上拉的最大高度 且必须大于本身控件的高度  最佳为2倍
     * @param dp 单位为dp
     */
    public void setMaxFootHeight(int dp){
        if(foot_height >= DisplayUtil.dp2Px(getContext(),dp)){
            return;
        }
        foot_height_2 = DisplayUtil.dp2Px(getContext(),dp);
    }

    /**
     * 同时设置加载更多控件和刷新控件的最大高度 且必须大于本身控件的高度  最佳为2倍
     * @param dp 单位为dp
     */
    public void setAllMaxHeight(int dp){
        if(head_height >= DisplayUtil.dp2Px(getContext(),dp)){
            return;
        }
        if(foot_height >= DisplayUtil.dp2Px(getContext(),dp)){
            return;
        }
        head_height_2 = DisplayUtil.dp2Px(getContext(),dp);
        foot_height_2 = DisplayUtil.dp2Px(getContext(),dp);
    }

    /**
     * 同时设置加载更多控件和刷新控件的最大高度 且必须大于本身控件的高度  最佳为2倍
     * @param refresh  刷新控件下拉的最大高度 单位为dp
     * @param loadMore 加载控件上拉的最大高度 单位为dp
     */
    public void setAllMaxHeight(int refresh,int loadMore){
        if(head_height >= DisplayUtil.dp2Px(getContext(),refresh)){
            return;
        }
        if(foot_height >= DisplayUtil.dp2Px(getContext(),loadMore)){
            return;
        }
        head_height_2 = DisplayUtil.dp2Px(getContext(),refresh);
        foot_height_2 = DisplayUtil.dp2Px(getContext(),loadMore);
    }

}
