package com.example.downrefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.sak.ultilviewlib.UltimateRefreshView;
import com.sak.ultilviewlib.adapter.BaseFooterAdapter;
import com.sak.ultilviewlib.adapter.BaseHeaderAdapter;
import com.sak.ultilviewlib.adapter.InitFooterAdapter;
import com.sak.ultilviewlib.adapter.InitHeaderAdapter;

import com.sak.ultilviewlib.util.MeasureTools;
import com.tuacy.nestedscrolldemo.new_type_interface.OnFooterRefreshListener;
import com.tuacy.nestedscrolldemo.new_type_interface.OnHeaderRefreshListener;

/**
 * Created by Administrator on 2018/9/12.
 */

public class MyUltimateRefreshView extends LinearLayout{
    private static final String TAG = UltimateRefreshView.class.getSimpleName();
    // 刷新时状态
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    // pull state
    private static final int PULL_UP_STATE = 0;
    private static final int PULL_DOWN_STATE = 1;

    private int mPullState;

    /**
     * list or grid
     */
    private AdapterView<?> mAdapterView;
    /**
     * RecyclerView
     */
    private RecyclerView mRecyclerView;
    /**
     * ScrollView
     */
    private ScrollView mScrollView;
    /**
     * WebView
     */
    private WebView mWebView;


    //Header
    private int mHeaderState;
    private View mHeaderView;
    private int mHeadViewHeight;
    //Footer
    private int mFooterState;
    private View mFooterView;
    private int mFooterViewHeight;
    //action
    private int lastY;

    private BaseHeaderAdapter mBaseHeaderAdapter;
    private BaseFooterAdapter mBaseFooterAdapter;
    private OnHeaderRefreshListener mOnHeaderRefreshListener;
    private OnFooterRefreshListener mOnFooterRefreshListener;

    private Context mContext;


    public MyUltimateRefreshView(Context context) {
        super(context);
        init(context);
    }

    public MyUltimateRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyUltimateRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //设置为垂直布局，避免每次在xml中修改(LinearLayout 默认为horizontal)
        setOrientation(VERTICAL);
        mContext = context;

    }


    public void setBaseHeaderAdapter(BaseHeaderAdapter baseHeaderAdapter) {
        mBaseHeaderAdapter = baseHeaderAdapter;
        initHeaderView();
        initSubViewType();
    }

    public void setBaseHeaderAdapter() {
        mBaseHeaderAdapter = new InitHeaderAdapter(mContext);
        initHeaderView();
        initSubViewType();
    }

    public void setBaseFooterAdapter(BaseFooterAdapter baseFooterAdapter) {
        mBaseFooterAdapter = baseFooterAdapter;
        initFooterView();
    }

    public void setBaseFooterAdapter() {
        mBaseFooterAdapter = new InitFooterAdapter(mContext);
        initFooterView();
    }

    /**
     * 计算顶部view 高度，将其隐藏
     */
    private void initHeaderView() {
        mHeaderView = mBaseHeaderAdapter.getHeaderView();
        MeasureTools.measureView(mHeaderView);
        mHeadViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeadViewHeight);
        params.topMargin = -mHeadViewHeight;
        addView(mHeaderView, 0, params);

    }

    private void initFooterView() {
        mFooterView = mBaseFooterAdapter.getFooterView();
        MeasureTools.measureView(mFooterView);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mFooterViewHeight);
        addView(mFooterView, params);
    }


    /**
     * 确定UltimateRefreshView 内部子视图类型
     */
    private void initSubViewType() {
        int count = getChildCount();
        if (count < 2) {
            return;
        }

        View view = getChildAt(1);

        if (view instanceof AdapterView<?>) {
            mAdapterView = (AdapterView<?>) view;
        }

        if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
        }

        if (view instanceof ScrollView) {
            mScrollView = (ScrollView) view;
        }

        if (view instanceof WebView) {
            mWebView = (WebView) view;
        }

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int y = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - lastY;
                if (isParentViewScroll(deltaY)) {
                    Log.e(TAG, "onInterceptTouchEvent: belong to ParentView");
                    return true; //此时,触发onTouchEvent事件
                }
                break;
            default:
                break;
        }
        return false;
    }


    public void cancelAction(){
        int topMargin = getHeaderTopMargin();
        Log.e(TAG, "onTouchEvent: topMargin==" + topMargin);
        if (mPullState == PULL_DOWN_STATE) {
            if (topMargin >= 0) {
                headerRefreshing();
            } else {
                reSetHeaderTopMargin(-mHeadViewHeight);
            }
        } else if (mPullState == PULL_UP_STATE) {
            if (Math.abs(topMargin) >= mHeadViewHeight
                    + mFooterViewHeight) {
                // 开始执行footer 刷新
                footerRefreshing();
            } else {
                // 还没有执行刷新，重新隐藏
                reSetHeaderTopMargin(-mHeadViewHeight);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG,"y="+y);
                Log.e(TAG,"lastY="+lastY);
                int deltaY = y - lastY;
                if (mPullState == PULL_DOWN_STATE) {//表示当前是往下拉
                    if(deltaY != 0 && mAdapterView.getFirstVisiblePosition() == 0){
                        Log.e(TAG, "onTouchEvent: pull down begin-->" + deltaY);
                        Log.e(TAG,"****mHeaderState="+mHeaderState);
                        initHeaderViewToRefresh(deltaY);
                    }



                } else if (mPullState == PULL_UP_STATE) {
                    initFooterViewToRefresh(deltaY);
                }
                lastY = y;
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG,"ACTION_UP执行");
                cancelAction();
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.e(TAG,"ACTION_CANCEL执行");
                cancelAction();
                break;
        }

        return super.onTouchEvent(event);
    }


    private void initHeaderViewToRefresh(int deltaY) {
        if (mBaseHeaderAdapter == null) {
            return;
        }
        int topDistance = UpdateHeadViewMarginTop(deltaY);
        Log.e(TAG,"***topDistance="+topDistance);
        Log.e(TAG,"***mHeadViewHeight="+mHeadViewHeight);
        if (topDistance < 0 && topDistance > -mHeadViewHeight) {
            Log.e(TAG,"---pullViewToRefresh被调用");
            mBaseHeaderAdapter.pullViewToRefresh(deltaY);
            mHeaderState = PULL_TO_REFRESH;//下拉刷新状态
        } else if (topDistance > 0 && mHeaderState != RELEASE_TO_REFRESH) {
            Log.e(TAG,"---releaseViewToRefresh被调用");
            mBaseHeaderAdapter.releaseViewToRefresh(deltaY);
            mHeaderState = RELEASE_TO_REFRESH;//释放刷新状态
        }

    }

    private void initFooterViewToRefresh(int deltaY) {
        if (mBaseFooterAdapter == null) {
            return;
        }

        int topDistance = UpdateHeadViewMarginTop(deltaY);

        Log.e("zzz", "the distance  is " + topDistance);

        // 如果header view topMargin 的绝对值大于或等于(header + footer) 四分之一 的高度
        // 说明footer view 完全显示出来了，修改footer view 的提示状态
        if (Math.abs(topDistance) >= (mHeadViewHeight + mFooterViewHeight)/4
                && mFooterState != RELEASE_TO_REFRESH) {
            mBaseFooterAdapter.pullViewToRefresh(deltaY);
            mFooterState = RELEASE_TO_REFRESH;
        } else if (Math.abs(topDistance) < (mHeadViewHeight + mFooterViewHeight)/4) {
            mBaseFooterAdapter.releaseViewToRefresh(deltaY);
            mFooterState = PULL_TO_REFRESH;
        }
    }


    private int UpdateHeadViewMarginTop(int deltaY) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float topMargin = params.topMargin + deltaY*0.5f;
        //float topMargin = params.topMargin + deltaY;
        params.topMargin = (int) topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }


    private void headerRefreshing() {
        if (mBaseHeaderAdapter == null) {
            return;
        }
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);
        mBaseHeaderAdapter.headerRefreshing();
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener.onHeaderRefresh(this);

        }
    }

    private void footerRefreshing() {
        if (mBaseFooterAdapter == null) {
            return;
        }

        mFooterState = REFRESHING;
        int top = mHeadViewHeight + mFooterViewHeight;
        setHeaderTopMargin(-top);
        mBaseFooterAdapter.footerRefreshing();
        if (mOnFooterRefreshListener != null) {
            mOnFooterRefreshListener.onFooterRefresh(this);
        }
    }

    public void onHeaderRefreshComplete() {
        if (mBaseHeaderAdapter == null) {
            return;
        }
        setHeaderTopMargin(-mHeadViewHeight);
        mBaseHeaderAdapter.headerRefreshComplete();
        mHeaderState = PULL_TO_REFRESH;
    }

    public void onFooterRefreshComplete() {
        if (mBaseFooterAdapter == null) {
            return;
        }
        setHeaderTopMargin(-mHeadViewHeight);
        mBaseFooterAdapter.footerRefreshComplete();
        mFooterState = PULL_TO_REFRESH;
    }

    /**
     * 滑动由父View（当前View）处理
     *
     * @param deltaY
     * @return
     */
    private boolean isParentViewScroll(int deltaY) {
        boolean belongToParentView = false;
        if (mHeaderState == REFRESHING) {
            belongToParentView = false;
        }

        if (mAdapterView != null) {

            if (deltaY > 0) {
                View child = mAdapterView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }

                if (mAdapterView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            } else if (deltaY < 0) {
                View lastChild = mAdapterView.getChildAt(mAdapterView
                        .getChildCount() - 1);
                if (lastChild == null) {
                    // 如果mAdapterView中没有数据,不拦截
                    belongToParentView = false;
                }
                // 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
                // 等于父View的高度说明mAdapterView已经滑动到最后
                if (lastChild.getBottom() <= getHeight()
                        && mAdapterView.getLastVisiblePosition() == mAdapterView
                        .getCount() - 1) {
                    mPullState = PULL_UP_STATE;
                    belongToParentView = true;
                }
            }
        }


        if (mRecyclerView != null) {
            if (deltaY > 0) {
                View child = mRecyclerView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }
                LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int firstPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();

                if (firstPosition == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            } else if (deltaY < 0) {
                View child = mRecyclerView.getChildAt(0);
                if (child == null) {
                    belongToParentView = false;
                }
                if (mRecyclerView.computeVerticalScrollExtent() + mRecyclerView.computeVerticalScrollOffset()
                        >= mRecyclerView.computeVerticalScrollRange()){
                    belongToParentView = true;
                    mPullState = PULL_UP_STATE;
                }else {
                    belongToParentView = false;
                }
            }
        }

        if (mScrollView != null) {
            View child = mScrollView.getChildAt(0);
            if (deltaY > 0) {

                if (child == null) {
                    belongToParentView = false;
                }

                int distance = mScrollView.getScrollY();
                if (distance == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            } else if (deltaY < 0
                    && child.getMeasuredHeight() <= getHeight()
                    + mScrollView.getScrollY()) {
                mPullState = PULL_UP_STATE;
                belongToParentView = true;

            }
        }

        if (mWebView != null) {
            View child = mWebView.getChildAt(0);
            if (deltaY > 0) {

                if (child == null) {
                    belongToParentView = false;
                }

                int distance = mWebView.getScrollY();
                if (distance == 0) {
                    mPullState = PULL_DOWN_STATE;
                    belongToParentView = true;
                }
            }
        }


        return belongToParentView;
    }


    /**
     * 获取当前header view 的topMargin
     *
     * @return
     * @description
     */

    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }

    /**
     * 设置header view 的topMargin的值
     *
     * @param topMargin ，为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
     * @description
     */
    private void setHeaderTopMargin(int topMargin) {

        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
    }

    /**
     * //上拉或下拉至一半时，放弃下来，视为完成一次下拉统一处理，初始化所有内容
     *
     * @param topMargin
     */
    private void reSetHeaderTopMargin(int topMargin) {

        if (mBaseHeaderAdapter != null) {
            mBaseHeaderAdapter.headerRefreshComplete();
        }

        if (mBaseFooterAdapter != null) {
            mBaseFooterAdapter.footerRefreshComplete();
        }

        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
    }


    public void setOnHeaderRefreshListener(OnHeaderRefreshListener onHeaderRefreshListener) {
        mOnHeaderRefreshListener = onHeaderRefreshListener;
    }

    public void setOnFooterRefreshListener(OnFooterRefreshListener onFooterRefreshListener) {
        mOnFooterRefreshListener = onFooterRefreshListener;
    }
}
