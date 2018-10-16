package com.example.downrefresh;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.downrefresh.new_part_views.BaseRefreshListener;
import com.example.downrefresh.new_part_views.PullToRefreshLayout;
import com.tuacy.nestedscrolldemo.R;

public class ScrollViewPullToReFreshActivity extends AppCompatActivity {
    private static final String TAG = "ScrollViewPullToReFreshActivity";

    //新版本下拉刷新控件
    private PullToRefreshLayout activity_main;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_view_pull_to_re_fresh);
        activity_main = (PullToRefreshLayout)findViewById(R.id.activity_main);
        activity_main.setRefreshListener(new BaseRefreshListener() {
            @Override
            public void refresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束刷新
                        activity_main.finishRefresh();
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 结束加载更多
                        activity_main.finishLoadMore();
                    }
                }, 2000);
            }
        });


    }
}
