package com.example.downrefresh.new_part_views;

/**
 * 数据刷新和加载接口
 * @auther lupeng
 */

public interface BaseRefreshListener {
    /**
     * 刷新
     */
    void refresh();

    /**
     * 加载更多
     */
    void loadMore();
}
