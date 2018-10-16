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

/**
 * Created by Administrator on 2018/9/6.
 */

public class WifiView extends View{
    private static final String TAG = "WifiView";
    //定义画笔
    private Paint mPaint;
    //指定圆弧的起始角度和绘制圆弧的弧度
    private int start_angle = -135;
    private int sweep_angle = 90;



    private int max_count = 4;

    private Handler handle;

    //定义wifi圆环的数量
    private int wifi_arc_count = 0;
    //定义圆环之间的单位间隔
    private int wifi_discount = 30;


    //当前view的宽度和高度
    private int view_width;
    private int view_height;



    public WifiView(Context context) {
        super(context);
        initPaint();
        initView();
    }

    public WifiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initView();
    }

    /**
     * 利用hangle的postdelay方法开始运动
     */
    private void initView() {
//        handle = new Handler();
//        handle.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//               invalidate();
//                handle.postDelayed(this,500);
//            }
//        },500);
//        invalidate();

    }

    public WifiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initView();
    }


    public void initPaint(){
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        view_width = getWidth();
        view_height = getHeight();
        Log.e(TAG,"当前view的宽度="+view_width);
        Log.e(TAG,"当前view的高度="+view_height);
        //先绘制死的wifi图标
        RectF mRectf = new RectF(0,0,150,150);
        RectF mRectf2 = new RectF(15,15,135,135);
        RectF mRectf3 = new RectF(30,30,120,120);
        RectF mRectf4 = new RectF(45,45,105,105);
        canvas.drawArc(mRectf,start_angle,sweep_angle,false,mPaint);
        canvas.drawArc(mRectf2,start_angle,sweep_angle,false,mPaint);
        canvas.drawArc(mRectf3,start_angle,sweep_angle,false,mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectf4,start_angle,sweep_angle,true,mPaint);
        //绘制动态的wifi图标
//        for(int i=0;i<=wifi_arc_count;i++){
//            //需要从最底层的圆弧画起
//            RectF mRectf = new RectF(wifi_discount*(3-i),wifi_discount*(3-i),300-wifi_discount*(3-i),300-wifi_discount*(3-i));
//            if(i==0){
//                //如果是最后一个
//                mPaint.setStyle(Paint.Style.FILL);
//                canvas.drawArc(mRectf,start_angle,sweep_angle,true,mPaint);
//            }else{
//                mPaint.setStyle(Paint.Style.STROKE);
//                canvas.drawArc(mRectf,start_angle,sweep_angle,false,mPaint);
//            }
//        }
//        wifi_arc_count++;
//        if(wifi_arc_count==4){
//            wifi_arc_count = 0;
//        }



    }
}
