package com.example.downrefresh;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


/**
 * 能自己判断是否滚动到了最底部的自定义recycleview
 * @auther lupeng
 */
public class MyRecycleView extends RecyclerView {
    private static final String TAG = "MyRecycleView";
    private loadMoreListener load_listener;

    public loadMoreListener getLoad_listener() {
        return load_listener;
    }

    public void setLoad_listener(loadMoreListener load_listener) {
        this.load_listener = load_listener;
    }

    public MyRecycleView(Context context) {
        super(context);
        addOnScrollListener(listener);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(listener);
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(listener);
    }



    private RecyclerView.OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if(isVisBottom(recyclerView)){
                if(null!=getLoad_listener()){
                    getLoad_listener().onLoadMore();
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };


    public interface loadMoreListener{
        public void onLoadMore();
    }

    /**
     * 判断recycleview是否滚动到了最底部
     * @param recyclerView
     * @return
     */
    public static boolean isVisBottom(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }





}
