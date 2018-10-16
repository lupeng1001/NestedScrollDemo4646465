package com.example.downrefresh.new_part_views;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;


/**
 * 自定义上拉加载更多view
 * @auther lupeng
 */
public class LoadMoreView extends FrameLayout implements FooterView {

    private TextView tv;
    private ImageView arrow;
    private RefreshAnimView refreshAnimView;

    public LoadMoreView(Context context) {
        this(context,null);
    }

    public LoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_normal_footer,null);
        addView(view);
        //上拉加载标题
        tv = (TextView) view.findViewById(R.id.normal_footer_text);
        //上拉加载左侧图片
        arrow = (ImageView) view.findViewById(R.id.footer_arrow);
        //上拉加载进度条
        refreshAnimView = (RefreshAnimView) view.findViewById(R.id.refreshAnimView);
    }

    @Override
    public void begin() {

    }

    @Override
    public void progress(float progress, float all) {
        float s = progress / all;
        refreshAnimView.setCurrentProgress(s);
        refreshAnimView.invalidate();
        if (progress > all){
            refreshAnimView.setVisibility(View.GONE);
            arrow.setVisibility(View.VISIBLE);
            tv.setText("松开加载");
        }else{
            refreshAnimView.setVisibility(View.VISIBLE);
            arrow.setVisibility(View.GONE);
            tv.setText("下拉加载");
        }
    }

    @Override
    public void finishing(float progress, float all) {

    }

    @Override
    public void loading() {
        startAnimation();
        arrow.setVisibility(VISIBLE);
        refreshAnimView.setVisibility(GONE);
        tv.setText("加载中...");
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
    public void normal() {
        arrow.setVisibility(GONE);
        refreshAnimView.setVisibility(VISIBLE);
        tv.setText("下拉加载");
    }

    @Override
    public View getView() {
        return this;
    }
}