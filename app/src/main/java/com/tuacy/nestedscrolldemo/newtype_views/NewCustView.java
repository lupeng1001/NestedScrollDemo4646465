package com.tuacy.nestedscrolldemo.newtype_views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.request.target.SquaringDrawable;
import com.tuacy.nestedscrolldemo.AdvDataShare;
import com.tuacy.nestedscrolldemo.R;

/**
 * Created by Administrator on 2018/9/5.
 */

public class NewCustView extends ImageView{
    private static final String TAG = "NewCustView";
    private Context mContext;
    private Paint mPaint;
    private int[] colors = new int[]{R.color.black,R.color.blue,R.color.red,
    R.color.orange,R.color.green};

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    private int view_width;
    private int view_height;
    private float radius = 100;//绘制圆形的半径

    private Bitmap mBitmap;

    private DisplayMetrics dm;


    public NewCustView(Context context) {
        super(context);
        mContext = context;
        mPaint = new Paint();
    }

    public NewCustView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mPaint = new Paint();
    }

    public NewCustView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        view_width = getWidth();
        view_height = getHeight();
        Log.e(TAG,"view_width="+view_width);
        Log.e(TAG,"view_height="+view_height);
        Log.e(TAG,"onDraw方法被调用");
        Drawable drawable = getDrawable();
        if(drawable!=null){
            Log.e(TAG,"drawable!=null执行");
            Bitmap bitmap = null;
            if(drawable.getClass()==GlideBitmapDrawable.class){
                GlideBitmapDrawable mdrawable=(GlideBitmapDrawable) drawable;
                bitmap = mdrawable.getBitmap();
            }else if(drawable.getClass()==BitmapDrawable.class){
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }else if(drawable.getClass()==SquaringDrawable.class){
                SquaringDrawable mdrawable=(SquaringDrawable) drawable;
                bitmap = ((GlideBitmapDrawable) mdrawable.getCurrent()).getBitmap();
            }
            Bitmap b = getRoundCornerBitmap(bitmap, 20);
            Log.e(TAG,"b.getWidth()="+b.getWidth());
            Log.e(TAG,"b.getHeight()="+b.getHeight());
            Log.e(TAG,"getWidth()="+getWidth());
            Log.e(TAG,"getHeight()="+getHeight());
            final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
            final Rect rectDest = new Rect(0,0,getWidth(),getHeight());
            //paint对象回复默认值
            mPaint.reset();
            //下面这个方法是将处理好的bitmap控制在view的中心位置显示
            canvas.drawBitmap(b, rectSrc, rectDest, mPaint);
            //canvas.drawBitmap(bitmap, rectSrc, rectDest, mPaint);

            //再绘制圆角矩形
//            ((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
//            AdvDataShare.SCREEN_WIDTH = dm.widthPixels;// 获取当前终端的屏幕宽度信息，记录下来
//            AdvDataShare.SCREEN_HEIGHT = dm.heightPixels;// 获取当前终端的屏幕高度信息，记录下来
//            AdvDataShare.SCREEN_DENSITY_DPI = dm.densityDpi;
//            RectF rectf = new RectF(10*AdvDataShare.SCREEN_DENSITY_DPI/160,10*AdvDataShare.SCREEN_DENSITY_DPI/160,
//                    110*AdvDataShare.SCREEN_DENSITY_DPI/160,210*AdvDataShare.SCREEN_DENSITY_DPI/160);
//            mPaint.setColor(Color.parseColor("#000000"));
//            mPaint.setStrokeWidth(2);
//            mPaint
//            canvas.drawRoundRect(rectf,10,10,mPaint);

            //继续添加黑色的边框
//            mPaint.setColor(Color.parseColor("#000000"));
//            mPaint.setStyle(Paint.Style.STROKE);
//            mPaint.setAntiAlias(true);
//            canvas.drawRoundRect(new RectF(rectSrc), 20, 20, mPaint);
        }else{
            Log.e(TAG,"drawable==null执行");
            super.onDraw(canvas);
        }

    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        Log.e(TAG,"onDraw方法执行啦");
//        if(mBitmap==null){
//            Log.e(TAG,"mBitmap==null执行啦");
//            return;
//        }
//        if(mBitmap!=null){
//            Log.e(TAG,"mBitmap!=null执行");
//            Bitmap local_bitmap = getRoundBitmap(mBitmap);
//            int view_width = getWidth();
//            int view_height = getHeight();
//            int bitmap_width = local_bitmap.getWidth();
//            int bitmap_height = local_bitmap.getHeight();
//            Log.e(TAG,"view_width="+view_width);
//            Log.e(TAG,"view_height="+view_height);
//            Log.e(TAG,"bitmap_width="+bitmap_width);
//            Log.e(TAG,"bitmap_height="+bitmap_height);
//            Rect src_rectf = new Rect(0,0,bitmap_width,bitmap_height);
//            Rect dest_rectf = new Rect(0,0,100,100);
//            //将之前paint的设置都清空
//            mPaint.reset();
//            //重新在系统的画布上绘制新的bitmap
//            canvas.drawBitmap(local_bitmap,src_rectf,dest_rectf,mPaint);
//        }else{
//            Log.e(TAG,"bitmap==null执行");
//            super.onDraw(canvas);
//        }
//    }


//    @Override
//    public void setImageBitmap(Bitmap bm) {
//        Log.e(TAG,"***setImageBitmap执行");
//        super.setImageBitmap(bm);
//        mBitmap = bm;
//        invalidate();
//    }
//
//    @Override
//    public void setImageDrawable(Drawable drawable) {
//        Log.e(TAG,"***setImageDrawable执行");
//        super.setImageDrawable(drawable);
//        mBitmap = getBitmapFromDrawable(drawable);
//        invalidate();
//    }
//
//    @Override
//    public void setImageResource(int resId) {
//        Log.e(TAG,"***setImageResource执行");
//        super.setImageResource(resId);
//        mBitmap = getBitmapFromDrawable(getDrawable());
//        invalidate();
//    }
//
//    @Override
//    public void setImageURI(Uri uri) {
//        Log.e(TAG,"***setImageURI执行");
//        super.setImageURI(uri);
//        mBitmap = getBitmapFromDrawable(getDrawable());
//        invalidate();
//    }
//
//    private Bitmap getBitmapFromDrawable(Drawable drawable) {
//        if (drawable == null) {
//            return null;
//        }
//
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
//        try {
//            Bitmap bitmap;
//            if (drawable instanceof ColorDrawable) {
//                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
//            } else {
//                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
//                        BITMAP_CONFIG);
//            }
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//            drawable.draw(canvas);
//            return bitmap;
//        } catch (OutOfMemoryError e) {
//            return null;
//        }
//    }

    /**
     * 将普通的矩形bitmap转化成圆形bitmap输出
     * @param bitmap
     * @param round_px
     * @return
     */
    public Bitmap getRoundBitmap(Bitmap bitmap){
        //先以目标bitmap的宽和高来创建一个空白的bitmap
        Bitmap out_bitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        //以这个bitmap为基础创建canvas对象,之后的操作都是在canvas上对这个bitmap进行操作
        final Canvas canvas = new Canvas(out_bitmap);
        //画笔消除锯齿
        mPaint.setAntiAlias(true);
        //先绘制圆角矩形
        Rect round_rect = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        RectF round_rectf = new RectF(round_rect);
        //canvas.drawRoundRect(round_rectf,round_px,round_px,mPaint);
        canvas.drawCircle(bitmap.getWidth()/2,bitmap.getHeight()/2,bitmap.getHeight()/2,mPaint);
        //设置模式为叠加后取交集，显示后图
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,round_rect,round_rect,mPaint);
        return out_bitmap;
    }

    /**
     * 将普通的矩形bitmap转化成圆角bitmap输出(任意矩形都可以)
     * @param bitmap
     * @param round_px
     * @return
     */
    public Bitmap getRoundCornerBitmap(Bitmap bitmap,int round_px){
        Bitmap out_bitmap = Bitmap.createBitmap(view_width,view_height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(out_bitmap);
        //先绘制圆角矩形
        Rect round_rect = new Rect(0,0,view_width,view_height);
        RectF round_rectf = new RectF(round_rect);
        mPaint.setAntiAlias(true);
        //绘制指定角度的圆角矩形
        canvas.drawRoundRect(round_rectf,round_px,round_px,mPaint);
        //设置paint的
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap,round_rect,round_rect,mPaint);
        return out_bitmap;
    }


}
