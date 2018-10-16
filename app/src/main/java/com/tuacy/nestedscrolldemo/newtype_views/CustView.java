package com.tuacy.nestedscrolldemo.newtype_views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;
import com.tuacy.nestedscrolldemo.R;


/**
 * Created by Administrator on 2018/8/28.
 */

public class CustView extends ImageView {
    private static final String TAG = "CustView";
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;
    private Paint mPaint;
    private Paint mPaint2;
    private Paint mPaint3;
    private Paint blank_paint;//各扇形之间的分割线
    private Paint text_paint;//文字画笔
    private int paintColor;
    private int paintSize;
    private int circle_width;//原型图片的半径
    private Context mContext;

    private int roundWidth = 50;
    private int roundHeight = 50;

    //图片的宿放比例
    private float mScale;
    private int mRadius;
    private float animatedValue;


    private Bitmap mBitmap = null;


    /**
     * 此构造方法只能用new CustView()的方式来初始化view
     * @param context
     */
    public CustView(Context context) {
        super(context);
        mContext = context;
        initPaint();
    }

    /**
     * 初始化自定义控件的画笔Paint对象
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor("#FF3746"));
        mPaint.setAntiAlias(true);//消除锯齿

//        <color name="orange">#ffa500</color>
//    <color name="blue">#3299CC</color>

        mPaint2 = new Paint();
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setColor(Color.parseColor("#ffa500"));
        mPaint2.setAntiAlias(true);//消除锯齿

        mPaint3 = new Paint();
        mPaint3.setStyle(Paint.Style.FILL);
        mPaint3.setColor(Color.parseColor("#3299CC"));
        mPaint3.setAntiAlias(true);//消除锯齿


        blank_paint = new Paint();
        blank_paint.setStyle(Paint.Style.FILL);
        blank_paint.setColor(Color.parseColor("#FFFFFF"));
        blank_paint.setAntiAlias(true);





    }

    /**
     * 此构造方法是为了让我们从xml布局文件里面初始化控件
     * @param context
     * @param attrs
     */
    public CustView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initPaint();
        initAttrs(context,attrs);

    }

    /**
     * 获取自定义属性列表(在attrs.xml文件里自己定义)
     */
    private void initAttrs(Context context, AttributeSet attrs) {

//        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.CustView);
//        circle_width = type.getInt(R.styleable.CustView_circleWidth,100);
//        //使用完了之后，切记必须回收TypedArray对象
//        type.recycle();

    }

    public CustView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
        initAttrs(context,attrs);
    }



//    /**
//     * 此方法是view的测量宽和高的方法
//     * @param widthMeasureSpec
//     * @param heightMeasureSpec
//     * 这两个参数代表了水平空间上父容器施加的要素和垂直空间上父容器施加的要素。说的通俗点，
//     * 就是它们包含了宽高的尺寸大小和测量模式。
//     * ----本人的理解，就是告诉子view，父view所占的宽高情况和测量模式
//     *
//     * 这两个参数就是父控件告诉子控件可获得的空间以及关于这个空间的约束条件
//     * （好比我在思考需要多大的碗盛菜的时候我要看一下碗柜里最大的碗有多大，
//     * 菜的分量不能超过这个容积，这就是碗对菜的约束），
//     * 子控件拿着这些条件就能正确的测量自身的宽高了。
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int result_width;
//        int result_height;
////        //先获取原先的测量结果
////        int measureWidth = getMeasuredWidth();
////        int measureHeight = getMeasuredHeight();
////        //利用原先的测量结果计算出新尺寸
//
//        //widthSpecSize是父控件留给当前自定义view的剩余空间
//        int defaultWidth = 100;
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        if(widthSpecMode == MeasureSpec.UNSPECIFIED){
//            //没有指定大小，设置默认大小.
//            result_width = defaultWidth;
//        }else if(widthSpecMode == MeasureSpec.AT_MOST){
//            //当前尺寸widthSpecSize是View能取得的最大尺寸。
//            if(defaultWidth>widthSpecSize){
//                result_width = widthSpecSize;
//            }
//        }else if(widthSpecMode == MeasureSpec.EXACTLY){
//            //父容器确定了当前View的具体尺寸，View被限制在该尺寸中。当前的尺寸就是View应该设置的尺寸大小。
//            if(widthSpecSize>defaultWidth){
//                result_width = widthSpecSize;
//            }
//        }
//
//
//
//
//
//
//
//        int defaultHeight = 100;
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//
//
//
//
//
//        //保存计算后的记过
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG,"getMeasuredWidth()="+getMeasuredWidth());
        Log.e(TAG,"getMeasuredHeight()="+getMeasuredHeight());
        //由于是圆形，宽高应保持一致
        int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = size / 2;
        setMeasuredDimension(size, size);
    }



    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }


//    private void drawText(Canvas mCanvas, float textAngle, String kinds, float needDrawAngle) {
//        Rect rect = new Rect();
//        mTextPaint.setTextSize(sp2px(15));
//        mTextPaint.getTextBounds(kinds, 0, kinds.length(), rect);
//        if (textAngle >= 0 && textAngle <= 90) { //画布坐标系第一象限(数学坐标系第四象限)
//            if (needDrawAngle < minAngle) { //如果小于某个度数,就把文字画在饼状图外面
//                mCanvas.drawText(kinds, (float) (mRadius * 1.2 * Math.cos(Math.toRadians(textAngle))), (float) (mRadius * 1.2 * Math.sin(Math.toRadians(textAngle)))+rect.height()/2, mTextPaint);
//            } else {
//                mCanvas.drawText(kinds, (float) (mRadius * 0.75 * Math.cos(Math.toRadians(textAngle))), (float) (mRadius * 0.75 * Math.sin(Math.toRadians(textAngle)))+rect.height()/2, mTextPaint);
//            }
//        } else if (textAngle > 90 && textAngle <= 180) { //画布坐标系第二象限(数学坐标系第三象限)
//            if (needDrawAngle < minAngle) {
//                mCanvas.drawText(kinds, (float) (-mRadius * 1.2 * Math.cos(Math.toRadians(180 - textAngle))), (float) (mRadius * 1.2 * Math.sin(Math.toRadians(180 - textAngle)))+rect.height()/2, mTextPaint);
//            } else {
//                mCanvas.drawText(kinds, (float) (-mRadius * 0.75 * Math.cos(Math.toRadians(180 - textAngle))), (float) (mRadius * 0.75 * Math.sin(Math.toRadians(180 - textAngle)))+rect.height()/2, mTextPaint);
//            }
//        } else if (textAngle > 180 && textAngle <= 270) { //画布坐标系第三象限(数学坐标系第二象限)
//            if (needDrawAngle < minAngle) {
//                mCanvas.drawText(kinds, (float) (-mRadius * 1.2 * Math.cos(Math.toRadians(textAngle - 180))), (float) (-mRadius * 1.2 * Math.sin(Math.toRadians(textAngle - 180)))+rect.height()/2, mTextPaint);
//            } else {
//                mCanvas.drawText(kinds, (float) (-mRadius * 0.75 * Math.cos(Math.toRadians(textAngle - 180))), (float) (-mRadius * 0.75 * Math.sin(Math.toRadians(textAngle - 180)))+rect.height()/2, mTextPaint);
//            }
//        } else { //画布坐标系第四象限(数学坐标系第一象限)
//            if (needDrawAngle < minAngle) {
//                mCanvas.drawText(kinds, (float) (mRadius * 1.2 * Math.cos(Math.toRadians(360 - textAngle))), (float) (-mRadius * 1.2 * Math.sin(Math.toRadians(360 - textAngle)))+rect.height()/2, mTextPaint);
//            } else {
//                mCanvas.drawText(kinds, (float) (mRadius * 0.75 * Math.cos(Math.toRadians(360 - textAngle))), (float) (-mRadius * 0.75 * Math.sin(Math.toRadians(360 - textAngle)))+rect.height()/2, mTextPaint);
//            }
//        }
//
//    }

    /**
     * 自定义绘制扇形
     */
    public void drawArcAction(Canvas mCanvas){
        Log.e(TAG,"当前view宽度(单位px)="+getWidth());
        Log.e(TAG,"当前view高度(单位px)="+getHeight());
        Log.e(TAG,"---getHeight()="+getHeight());
        Log.e(TAG,"---getWidth()="+getWidth());
        float view_width = getWidth();
        float view_height = getHeight();
        LinearLayout.LayoutParams lps = (LinearLayout.LayoutParams) getLayoutParams();
        /**
         * 下面还需要判断安卓系统的版本号，太麻烦
         */
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mCanvas.drawArc(lps.leftMargin,lps.topMargin,view_width,view_height,0,60,true,mPaint);
//            mCanvas.drawArc(lps.leftMargin,lps.topMargin,view_width,view_height,60,180,true,mPaint2);
//            mCanvas.drawArc(lps.leftMargin,lps.topMargin,view_width,view_height,240,120,true,mPaint3);
//        }else{
//            //所有版本通用
//            RectF ovel = new RectF(lps.leftMargin,lps.topMargin,view_width,view_height);
//            mCanvas.drawArc(ovel,0,60,true,mPaint);
//            mCanvas.drawArc(ovel,60,180,true,mPaint2);
//            mCanvas.drawArc(ovel,240,120,true,mPaint3);
//        }

        //这种方式不需要判断系统版本号，各版本通吃
        RectF ovel = new RectF(lps.leftMargin,lps.topMargin,view_width,view_height);
        mCanvas.drawArc(ovel,0,60,true,mPaint);
        mCanvas.drawArc(ovel,60,180,true,mPaint2);
        mCanvas.drawArc(ovel,240,120,true,mPaint3);


        //下面开始画指示线部分
        //mCanvas.drawLine(500,500,800,800,paint);


    }

    private void initAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 360);
        anim.setDuration(10000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {animatedValue = (float) valueAnimator.getAnimatedValue();

                invalidate();
            }
        });
        anim.start();
    }


    /**
     * 绘制原型图片的方法
     */
    public void drawImageAction(Canvas mCanvas){
        /**
         * 1.MIRROR 镜像模式
         * 2.CLAMP 夹子模式
         * 3.REPEAT 重复模式
         */
        //1.镜像渐变模式
        Shader shader1 = new LinearGradient(100,100,500,500, Color.parseColor("#3299CC"),
                Color.parseColor("#FF3746"),Shader.TileMode.MIRROR);
        //2.辐射渐变模式
        Shader shader2 = new RadialGradient(300,300,200,Color.parseColor("#3299CC"),
                Color.parseColor("#FF3746"),Shader.TileMode.MIRROR);
        //3.扫描渐变(以扫描圆心坐标为起点，顺时针360扫描一周)
        Shader shader3 = new SweepGradient(200,200,Color.parseColor("#3299CC"),
                Color.parseColor("#FF3746"));


        /**
         * 以下为比较特殊的情况,bitmap的shader(个人认为也是使用最多的一种
         * 比如绘制原型的logo就会用到这个)
         */
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.head2_img);
        Shader shader4 = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,Shader.TileMode.CLAMP);


        Log.e(TAG,"bitmap.getHeight(单位px)="+mBitmap.getHeight());
        Log.e(TAG,"bitmap.getWidth(单位px)="+mBitmap.getWidth());
        Log.e(TAG,"当前view宽度(单位px)="+getWidth());
        Log.e(TAG,"当前view高度(单位px)="+getHeight());

        //计算缩放比例
        mScale = mRadius*2.0f / Math.min(mBitmap.getHeight(), mBitmap.getWidth());
        Log.e(TAG,"当前mScale="+mScale);

        Matrix matrix = new Matrix();
        matrix.setScale(mScale,mScale);
        shader4.setLocalMatrix(matrix);

        mPaint.setShader(shader4);
        mCanvas.drawCircle(getWidth()/2,getWidth()/2,getWidth()/2,mPaint);
        //canvas.drawRect(new Rect(100,100,500,500),mPaint);

//        mPaint = new Paint();
//        mPaint.setColor(Color.RED);
//        mPaint.setColor(Color.parseColor("#FF3746"));
//        //mPaint.setARGB();此方法可以设置alpha值
//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(2f);//画笔边框的宽度为10px
//        mPaint.setTextSize(40f);
//        //canvas.drawRect(new Rect(200,0,700,200),mPaint);
//        canvas.drawRect(new Rect(100,100,520,520),mPaint);
        //canvas.drawCircle(200,200,50,mPaint);
//        //canvas.drawRect(new Rect(0, 75, 720, 120),mPaint);



//        Path path = new Path();
//        path.addCircle(AdvDataShare.SCREEN_WIDTH/2,AdvDataShare.SCREEN_HEIGHT/2,100,Path.Direction.CW);//设置绘制一个圆心坐标是（600,600），半径是300的空心圆
//        canvas.drawTextOnPath("我是一个好孩子我是一个好孩子我是一个好孩子我是一个好孩子我是一个好孩子",path
//                ,0,0,mPaint);
//
//        canvas.drawPath(path,mPaint);
//        drawLeftUp(canvas);
//        drawLeftDown(canvas);
//        drawRightUp(canvas);
//        drawRightDown(canvas);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Log.e(TAG,"***setImageBitmap执行");
        super.setImageBitmap(bm);
        mBitmap = bm;
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        Log.e(TAG,"***setImageDrawable执行");
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        Log.e(TAG,"***setImageResource执行");
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        invalidate();
    }

    @Override
    public void setImageURI(Uri uri) {
        Log.e(TAG,"***setImageURI执行");
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG,"onDraw方法执行");
       if(mBitmap!=null){
           Log.e(TAG,"mBitmap!=null方法执行");
           Log.e(TAG,"当前mBitmap="+mBitmap);
           Log.e(TAG,"当前mBitmap.width="+mBitmap.getWidth());
           Log.e(TAG,"当前mBitmap.height="+mBitmap.getHeight());
           //下面获取的宽度就是屏幕的宽度
           Log.e(TAG,"getWidth()="+getWidth());
           //下面获取的高度是扣掉头部状态栏高度和底部虚拟按键高度之后的高度
           //可以理解为可编辑区域的高度
           Log.e(TAG,"getHeight()="+getHeight());
           //以下是获取不可编辑区域的高度
           Log.e(TAG,"状态栏高度="+ Utils.getStatusBarHeight(mContext));
           Log.e(TAG,"底部虚拟按键区的高度="+Utils.getNavigationBarHeight(mContext));
           drawImageAction(canvas);
           //drawArcAction(canvas);
       }else{
           Log.e(TAG,"mBitmap==null方法执行");
           super.onDraw(canvas);
       }





    }

    private void drawLeftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

//    private void updateShaderMatrix() {
//                 float scale;
//                 float dx = 0;
//                 float dy = 0;
//                 mShaderMatrix.set(null);
//
//                 if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
//                         scale = mDrawableRect.height() / (float) mBitmapHeight;
//                         dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
//                     } else {
//                         scale = mDrawableRect.width() / (float) mBitmapWidth;
//                         dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
//                     }
//
//                 mShaderMatrix.setScale(scale, scale);
//                 mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);
//
//                 mBitmapShader.setLocalMatrix(mShaderMatrix);
//             }

    private void drawLeftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(0, getHeight() - roundHeight * 2, roundWidth * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeight);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight() - roundHeight * 2, getWidth(), getHeight()), -0, 90);
        path.close();
        canvas.drawPath(path, mPaint);


    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundWidth, 0);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(), roundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, mPaint);
    }




    /**
     * 自定义我对view的宽和高
     */
    public void getLocalSize(){



//        UNSPECIFIED：父容器没有对当前View强加任何限制，当前View可以显示任意尺寸大小。
//        EXACTLY：父容器确定了当前View的具体尺寸，View被限制在该尺寸中。当前的尺寸就是View应该设置的尺寸大小。
//        AT_MOST：当前尺寸是View能取得的最大尺寸。
    }

}
