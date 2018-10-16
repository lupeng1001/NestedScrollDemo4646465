package com.tuacy.nestedscrolldemo.newtype_views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/9/7.
 */

public class TestAnimationView extends View{
    private DecimalFormat df = new DecimalFormat("#");
    private static final String TAG = "TestAnimationView";

    //定义当前绘制的进度信息
    private float progress=0.0f;
    //画笔对象
    private Paint mPaint;
    //定义view的宽度和高度
    private int view_width;
    private int view_height;
    //定义每次画弧度的角度
    private float single_angle=0.0f;
    //定义是否可以开始绘制
    private boolean can_draw;

    //动画对象
    private MyAnimation mAnimation;


    private Handler mHandle = new Handler();
    private float local_point;


    public TestAnimationView(Context context) {
        super(context);
        initPaint();
        startLocalAnimation();
    }

    public TestAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        startLocalAnimation();
    }

    public TestAnimationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        startLocalAnimation();

    }



    /**
     * 开始执行动画
     */
    private void startLocalAnimation() {
        if(mAnimation==null){
            mAnimation = new MyAnimation();
        }
        mAnimation.setDuration(10000);
        can_draw = true;
        startAnimation(mAnimation);
//        can_draw = true;
//        mHandle.postDelayed(new Runnable() {
//              @Override
//              public void run() {
//                  if(progress==360){
//                      return;
//                  }else{
//                      progress +=60;
//                  }
//
//                  invalidate();
//                  mHandle.postDelayed(this,1000);
//              }
//          },1000);

    }

    /**
     * 初始化画笔
     */
    public void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#FF3746"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!can_draw){
            return;
        }
        view_width = getWidth();
        view_height = getHeight();
        Log.e(TAG,"当前view_width="+view_width);
        Log.e(TAG,"当前view_height="+view_height);
        RectF mRectf =new RectF(0,0,view_width-5,view_height-5);
        Log.e(TAG,"progress="+progress);
        mPaint.setStrokeWidth(2);
        canvas.drawArc(mRectf,0,progress,false,mPaint);
        mPaint.setTextSize(25);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(df.format(local_point)+"%",view_width/2.0f,view_height/2.0f,mPaint);
    }


    /**
     * 定义一个自定义动画的内部类
     */
    public class MyAnimation extends Animation{
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            //利用interpolatedTime参数来改变角度，然后重绘
            Log.e(TAG,"interpolatedTime="+interpolatedTime);
            local_point = interpolatedTime*100;
            if(interpolatedTime<1.0f){
                progress =interpolatedTime*360;
                //非UI线程使用postInvalidate(),UI线程使用Invalidate()
            }
            invalidate();



        }
    }

}
