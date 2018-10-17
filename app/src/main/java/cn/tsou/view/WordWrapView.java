package cn.tsou.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自动能换行的横向view容器(可应用于一些商品属性选择部分显示场景)
 * @author lupeng
 */
public class WordWrapView extends ViewGroup {
	private static final String TAG = "WordWrapView";
	private static final int VIEW_VERTICAL_MARGIN = 15;//各子view之间纵向间距(单位px)
	private static final int VIEW_HORIZONTAL_MARGIN = 15;//各子view之间横向间距(单位px)
	  /**
	   * @param context
	   */
	  public WordWrapView(Context context) {
	    super(context);
	    
	  }
	  
	  
	  /**
	   * @param context
	   * @param attrs
	   * @param defStyle
	   */
	  public WordWrapView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	  }



	  /**
	   * @param context
	   * @param attrs
	   */
	  public WordWrapView(Context context, AttributeSet attrs) {
	    super(context, attrs);
	  }



	  @Override
	  protected void onLayout(boolean changed, int l, int t, int r, int b) {
	    int childCount = getChildCount();
	    int autualWidth = r - l;
	    int x = 0;// 横坐标开始
	    int y = 0;//纵坐标开始
	    int rows = 1;
	    for(int i=0;i<childCount;i++){
	      View view = getChildAt(i);
			int width = view.getMeasuredWidth();
	      int height = view.getMeasuredHeight();
			Log.e(TAG,"width="+width);
			Log.e(TAG,"height="+height);
			if(width>autualWidth){
				width = autualWidth;
			}
			x += width;
			Log.e(TAG,"onLayout当前x="+x);
	      if(x>autualWidth-VIEW_HORIZONTAL_MARGIN){
	        x = width;
			  rows++;
	      }
	      y = rows*(height);
			//将子view按照坐标位置进行放置
			if(x==getMeasuredWidth()){//如果正好达到viewgroup的宽度,则右边需要留出VIEW_HORIZONTAL_MARGIN
				view.layout(x-width+VIEW_HORIZONTAL_MARGIN, y-height, x-VIEW_HORIZONTAL_MARGIN, y);
			}else{//没有达到viewgroup宽度,则正常放置即可
				Log.e(TAG,"***onLayout当前x="+x);
				view.layout(x-width+VIEW_HORIZONTAL_MARGIN, y-height, x, y);
			}

		}
	  };

	  @Override
	  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int x = 0;//横坐标
	    int y = 0;//纵坐标
	    int rows = 1;//总行数
	    int specWidth = MeasureSpec.getSize(widthMeasureSpec);//通过父类获取到的测量宽度
		  Log.e(TAG,"specWidth="+specWidth);
		  int actualWidth = specWidth;//实际宽度
		  Log.e(TAG,"actualWidth="+actualWidth);
	    int childCount = getChildCount();//计算出当前子view的数量
		  Log.e(TAG,"childCount="+childCount);
	    for(int index = 0;index<childCount;index++){
	      View child = getChildAt(index);
	      child.setPadding(0, VIEW_VERTICAL_MARGIN, 0, 0);
	      child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	      int width = child.getMeasuredWidth();
	      int height = child.getMeasuredHeight();
			Log.e(TAG,"当前子view的测量width="+width);
			Log.e(TAG,"当前子view的测量height="+height);
			if(width>actualWidth){
				width = actualWidth;
			}
			x += width;//x坐标累加 (子view的宽度加上固定的左右间距值)
			Log.e(TAG,"当前x="+x);
	      if(x>actualWidth){//如果超过了actualWidth就执行换行
	        x = width;
	        rows++;
	      }
			Log.e(TAG,"当前rows="+rows);
	      y = rows*(height)+VIEW_VERTICAL_MARGIN;
		}
		Log.e(TAG,"测量完成后的viewgroup的宽度="+actualWidth);
		  Log.e(TAG,"测量完成后的viewgroup的高度="+y);
	    setMeasuredDimension(actualWidth, y);
	  }
}
