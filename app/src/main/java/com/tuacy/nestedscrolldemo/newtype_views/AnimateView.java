package com.tuacy.nestedscrolldemo.newtype_views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * 自定义View动画
 */

public class AnimateView extends View{
    private static final String TAG = "AnimateView";
    private Paint mPaint;
    private float progress = 0.0f;
    private float local_progress = 0.0f;
    private RectF local_rectf;


    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        local_progress += 1;
        //通知重绘，会重新调用ondraw方法
        invalidate();
    }

    public AnimateView(Context context) {
        super(context);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        local_rectf = new RectF(0,0,300,300);
    }

    public AnimateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        local_rectf = new RectF(0,0,300,300);
    }

    public AnimateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(30);
        mPaint.setTextAlign(Paint.Align.CENTER);
        local_rectf = new RectF(0,0,300,300);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"local_progress="+local_progress);
        Log.e(TAG,"progress="+progress);
        canvas.drawArc(local_rectf,local_progress,progress*2.7f,false,mPaint);

        canvas.drawText(local_progress+"",150,150,mPaint);
    }




    /**
     * 为view设置动画
     */
    public void startAnimate(){
        ObjectAnimator mAnimator = ObjectAnimator.ofFloat(this,"progress",0,360);
        mAnimator.setDuration(36000);
        mAnimator.start();
    }

}
