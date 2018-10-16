package com.example.downrefresh.new_part_views;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 下拉刷新的状态值工具类
 * @auther lupeng
 */

public class State {
    @IntDef({REFRESH, LOADMORE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface REFRESH_STATE {

    }

    public static final int REFRESH = 10;
    public static final int LOADMORE = 11;
}
