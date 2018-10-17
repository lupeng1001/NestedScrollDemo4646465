package cn.tsou.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 能判断是否滑动到最底部的自定义listview
 * @author lupeng
 */

public class MyListView extends ListView{
    private static final String TAG = "MyListView";
    private loadMoreListener load_listener;

    //listview是否正在加载数据
    private boolean is_loading;

    public boolean is_loading() {
        return is_loading;
    }

    public void setIs_loading(boolean is_loading) {
        this.is_loading = is_loading;
    }

    public loadMoreListener getLoad_listener() {
        return load_listener;
    }

    public void setLoad_listener(loadMoreListener load_listener) {
        this.load_listener = load_listener;
    }

    public MyListView(Context context) {
        super(context);
        setOnScrollListener(listener);
    }

    public MyListView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(listener);
    }

    public MyListView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnScrollListener(listener);
    }

    private OnScrollListener listener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            Log.e(TAG,"firstVisibleItem="+firstVisibleItem);
            Log.e(TAG,"visibleItemCount="+visibleItemCount);
            Log.e(TAG,"totalItemCount="+totalItemCount);
            if((firstVisibleItem + visibleItemCount) == totalItemCount){//滚动到底部
                Log.e(TAG,"满足滚动到最后条件执行");
                if(!is_loading()&&null!=getLoad_listener()){
                    Log.e(TAG,"满足加载更多条件执行");
                    setIs_loading(true);
                    getLoad_listener().onLoadMore();
                }
            }
        }
    };

    public interface loadMoreListener{
        public void onLoadMore();
    }


}
