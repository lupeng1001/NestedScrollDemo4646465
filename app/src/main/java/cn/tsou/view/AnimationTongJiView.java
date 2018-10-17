package cn.tsou.view;


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

import java.util.ArrayList;
import java.util.List;

import cn.tsou.view.bean.SaveData;


/**
 * 带动画展示效果的多彩统计饼图
 * @author lupeng
 * @date 2018-10-17
 */
public class AnimationTongJiView extends View{
    private static final String TAG = "TestAnimationTongJiView";
    private Paint mPaint;
    private int view_width;
    private int view_height;
    private List<SaveData> data_list = new ArrayList<SaveData>();
    //定义动画操作对象
    private MyAnimation animation;
    //动画持续时间
    private int local_duration = 3000;
    //动画绘制各部分的比例
    private float local_percent;
    private float has_fill_percent;


    public int getLocal_duration() {
        return local_duration;
    }

    public void setLocal_duration(int local_duration) {
        this.local_duration = local_duration;
    }

    public AnimationTongJiView(Context context) {
        super(context);
        initPaint();

    }

    public AnimationTongJiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();

    }

    public AnimationTongJiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();

    }

    public List<SaveData> getData_list() {
        return data_list;
    }

    public void setData_list(List<SaveData> data_list) {
        this.data_list = data_list;
        for(int i=0;i<getData_list().size();i++){
            Log.e(TAG,"i="+i+"执行啦");
            Log.e(TAG,"------getData_list().get(i).getPercent()="+getData_list().get(i).getPercent());
        }
        initAnimation();
        //开启动画
        startAnimation(animation);
    }

    /**
     * 初始化动画对象
     */
    public void initAnimation(){
        animation = new MyAnimation();
        animation.setDuration(local_duration);
    }





    @Override
    protected void onDraw(Canvas canvas) {
        if(null==getData_list()
                ||getData_list().size()==0){
            return;
        }
        Log.e(TAG,"onDraw方法执行");
        view_width = getWidth();
        view_height = getHeight();
        Log.e(TAG,"view_width="+view_width);
        Log.e(TAG,"view_height="+view_height);
        //宽高各减10是为了圆形不要有毛边
        RectF rectf = new RectF(0,0,view_width-10,view_height-10);
        for(int i=0;i<getData_list().size();i++){
            Log.e(TAG,"i="+i+"执行啦");
            Log.e(TAG,"getData_list().get(i).getColor_value()="+getData_list().get(i).getColor_value());
            mPaint.setColor(Color.parseColor(getData_list().get(i).getColor_value()));
            Log.e(TAG,"getData_list().get(i).getPercent()="+getData_list().get(i).getPercent());
            canvas.drawArc(rectf,getData_list().get(i).getCurrent_percent(),getData_list().get(i).getPercent(),true,mPaint);
        }
        //在中间再画一个白色的圆
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawCircle((view_width-10)/2,(view_height-10)/2,(view_width-10)/4,mPaint);


    }

    /**
     * 画笔对象初始化
     */
    public void initPaint(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);
    }


    /**
     * 定义一个内部类，动画对象类
     */
    public class MyAnimation extends Animation{
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            Log.e(TAG,"interpolatedTime="+interpolatedTime);
            for(int i=0;i<getData_list().size();i++){
                Log.e(TAG,"***getData_list().get(i).getPercent()*interpolatedTime="+getData_list().get(i).getPercent()*interpolatedTime);
                getData_list().get(i).setCurrent_percent(getData_list().get(i).getTotal_percent()*interpolatedTime);
                getData_list().get(i).setPercent(getData_list().get(i).getAll_percent()*interpolatedTime);
            }
            invalidate();
        }
    }
}
