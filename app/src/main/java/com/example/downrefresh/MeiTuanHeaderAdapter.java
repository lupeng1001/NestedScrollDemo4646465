package com.example.downrefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.sak.ultilviewlib.adapter.BaseHeaderAdapter;
import com.sak.ultilviewlib.util.MeasureTools;
import com.tuacy.nestedscrolldemo.R;

/**
 * Created by engineer on 2017/4/30.
 */

public class MeiTuanHeaderAdapter extends BaseHeaderAdapter {
    private static final String TAG = "MeiTuanHeaderAdapter";

    private ImageView loading;
    private int viewHeight;
    private float pull_distance=0;

    public MeiTuanHeaderAdapter(Context context) {
        super(context);
    }

    @Override
    public View getHeaderView() {
        View mView = mInflater.inflate(R.layout.meituan_header_refresh_layout, null, false);
        loading = (ImageView) mView.findViewById(R.id.loading);
        MeasureTools.measureView(mView);
        viewHeight = mView.getMeasuredHeight();
        return mView;
    }

    @Override
    public void pullViewToRefresh(int deltaY) {
        Log.e(TAG,"deltaY="+deltaY);
        if(deltaY>0){
            Log.e(TAG,"deltaY>0执行");
        }else if(deltaY<0){
            Log.e(TAG,"deltaY<0执行");
        }
        //这里乘以0.3 是因为UltimateRefreshView 源码中对于滑动有0.3的阻尼系数，为了保持一致
        pull_distance=pull_distance+deltaY*0.5f;
        float scale = pull_distance / viewHeight;
        Log.e(TAG,"viewHeight="+viewHeight);
        Log.e(TAG,"pull_distance="+pull_distance);
        Log.e(TAG,"scale="+scale);
        loading.setScaleX(scale);
        loading.setScaleY(scale);
    }


    @Override
    public void releaseViewToRefresh(int deltaY) {
        Log.e(TAG,"releaseViewToRefresh执行:deltaY="+deltaY);
        loading.setImageResource(R.drawable.mei_tuan_loading_pre);
        AnimationDrawable mAnimationDrawable= (AnimationDrawable) loading.getDrawable();
        mAnimationDrawable.start();
    }

    @Override
    public void headerRefreshing() {
        loading.setImageResource(R.drawable.mei_tuan_loading);
        AnimationDrawable mAnimationDrawable= (AnimationDrawable) loading.getDrawable();
        mAnimationDrawable.start();
    }

    @Override
    public void headerRefreshComplete() {
        loading.setImageResource(R.drawable.pull_image);
        loading.setScaleX(0);
        loading.setScaleY(0);
        pull_distance=0;
    }
}
