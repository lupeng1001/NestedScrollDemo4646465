package com.example.downrefresh.new_part_views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;


/**
 * 带动画的自定义下拉刷新view
 * @author lupeng
 */
public class HeadRefreshView extends FrameLayout implements HeadView {

    private TextView tv;
    private ImageView arrow;
    private RefreshAnimView refreshAnimView;

    public HeadRefreshView(Context context) {
        this(context,null);
    }

    public HeadRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HeadRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_head,null);
        addView(view);
        //下拉刷新标题
        tv = (TextView) view.findViewById(R.id.normal_head_text);
        //下拉刷新动画图片
        arrow = (ImageView) view.findViewById(R.id.header_arrow);
        //下拉刷新可变大小的自定义view
        refreshAnimView = (RefreshAnimView) view.findViewById(R.id.refreshAnimView);
    }

    @Override
    public void begin() {
        refreshAnimView.setVisibility(View.VISIBLE);
        arrow.setVisibility(View.GONE);
    }

    @Override
    public void progress(float progress, float all) {
        float s = progress / all;
        refreshAnimView.setCurrentProgress(s);
        refreshAnimView.invalidate();
        if (progress > all){
            refreshAnimView.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
            tv.setText("松开刷新");
        }else{
            refreshAnimView.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
            tv.setText("下拉刷新");
        }
    }

    /**
     * 开始小人跑动的动画
     */
    public void startAnimation(){
        arrow.setImageResource(R.drawable.mei_tuan_loading);
        AnimationDrawable mAnimationDrawable= (AnimationDrawable) arrow.getDrawable();
        mAnimationDrawable.start();
    }

    @Override
    public void finishing(float progress, float all) {

    }

    /**
     * 加载中状态view显示
     */
    @Override
    public void loading() {
        startAnimation();
        arrow.setVisibility(VISIBLE);
        refreshAnimView.setVisibility(GONE);
        tv.setText("刷新中...");
    }
    /**
     * 普通状态view显示
     */
    @Override
    public void normal() {
        arrow.setVisibility(GONE);
        refreshAnimView.setVisibility(VISIBLE);
        tv.setText("下拉刷新");
    }

    @Override
    public View getView() {
        return this;
    }
}
