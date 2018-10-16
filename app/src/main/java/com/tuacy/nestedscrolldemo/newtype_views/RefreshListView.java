package com.tuacy.nestedscrolldemo.newtype_views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;

/**
 * Created by Administrator on 2018/9/10.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener{
    private static final String TAG = "RefreshListView";
    private Context mContext;
    private View header;
    private int headerHeight;
    //当前界面第一个可见的item的位置
    int firstVisibleItem;
    //标志，是在当前显示的listView是在listView最顶端时按下额
    boolean isFlag;
    //用户按下的Y坐标
    int startY;
    /**
     * 当前的下拉状态值
     */
    int state;//当前状态
     final int NONE = 0;//正常状态
     final int PULL = 1;//提示下拉状态
     final int RELEASE = 2;//提示释放状态
     final int REFRESH = 3;//提示正在刷新状态

    //当前scroll状态s
    private int scrollState;



    public RefreshListView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.e(TAG,"onScrollStateChanged执行");
        this.scrollState = scrollState;
        Log.e(TAG,"scrollState="+scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e(TAG,"onScroll执行");
        this.firstVisibleItem = firstVisibleItem;
    }

    private void initView(){
        //LayoutInflater作用是加载布局
        setOnScrollListener(this);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        header = inflater.inflate(R.layout.header_layout, null);
        //定义测量规则手动测量header的宽高,告诉listview如何去摆放和显示headview
        measureView(header);
        //测量完毕之后就取到header的测量高度
        headerHeight = header.getMeasuredHeight();
        topPadding(-headerHeight);
        //将headview加入到listview上
        this.addHeaderView(header);
    }
    /**
     * 设置顶部布局的上边距
     * @param topPadding
     */
    private void topPadding(int topPadding){
        //设置顶部提示的边距
        //除了顶部用参数值topPadding外，其他三个用header默认的值
        header.setPadding(header.getPaddingLeft(), topPadding,
                header.getPaddingRight(), header.getPaddingBottom());
        //使header无效，将来调用onDraw()重绘View
        header.invalidate();
    }



    /**
     * 通知父布局，占用的宽和高的一些摆放显示信息
     */
    private void measureView(View view){
        //LayoutParams are used by views to tell their parents
        //how they want to be laid out.
        //LayoutParams被view用来告诉它们的父布局它们应该被怎样安排
        ViewGroup.LayoutParams p = view.getLayoutParams();
        if(p==null){
            //两个参数:width,height
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //如果p==null，此时p.width = -1;p.height = -2;



        //getChildMeasureSpec:获取子View的widthMeasureSpec、heightMeasureSpec值
        //特别注意：这里第一个参数表示headview的父控件listview传递给子view的测量要求
        int width = ViewGroup.getChildMeasureSpec(MeasureSpec.UNSPECIFIED, 0, p.width);
        int height;
        int tempHeight = p.height;
        //判断当前的高度是否是具体的数值
        if(tempHeight>0){
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        }else{
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        //这个方法一调用，父控件就可以知道当前指定子view的宽高信息了
        view.measure(width, height);
    }

    /**
     * 加载完成
     */
    public void stopFresh(){
        state = NONE;
        refreshViewByState();
    }


    private void onMove(MotionEvent ev){
        //如果不是最顶端按下，则直接返回
        if(!isFlag){
            return;
        }
        int currentY = (int)ev.getY();//用户当前的Y坐标
        int space = currentY - startY;//用户向下拉的距离
        int topPadding = space - headerHeight;//顶部提示View距顶部的距离值
        switch (state) {
            //正常状态
            case NONE:
                Log.e(TAG,"NONE执行");
                if(space>0){
                    state = PULL;//下拉的距离大于0，则将状态改为PULL(提示下拉更新)
                    refreshViewByState();//根据状态的不同更新View
                }
                break;
            case PULL:
                Log.e(TAG,"PULL执行");
                Log.e(TAG,"headerHeight="+headerHeight);
                Log.e(TAG,"space="+space);
                Log.e(TAG,"topPadding="+topPadding);
                topPadding(topPadding);
                if(space>headerHeight+30//下拉的距离大于header的高度加30且用户滚动屏幕，手指仍在屏幕上
                        &&scrollState == SCROLL_STATE_TOUCH_SCROLL ){
                    state = RELEASE;//将状态改为RELEASE(提示松开更新)
                    refreshViewByState();
                }
                break;
            case RELEASE:
                Log.e(TAG,"RELEASE执行");
                topPadding(topPadding);
                if(space<headerHeight+30){//用户下拉的距离不够
                    state = PULL;         //将状态改为PULL
                    refreshViewByState();
                }else if(space<=0){  //用户下拉的距离为非正值
                    state = NONE;    //将状态改为NONE
                    isFlag = false;  //标志改为false
                    refreshViewByState();
                }
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                              //listview第一个可见view的item是0，才执行onTouchEvent
                             if(firstVisibleItem == 0){
                                 isFlag = true;//ListView最顶端按下，标志值设为真
                                 //记录用户当前按下的y坐标
                                 startY = (int)ev.getY();
                             }
                            break;
                     case MotionEvent.ACTION_MOVE:
                             onMove(ev);
                             break;
                     case MotionEvent.ACTION_UP:
                         //用户手指离开屏幕
                             if(state == RELEASE){
                                 //如果当前是提示释放状态
                                     state = REFRESH;
                                     //加载数据
                                     refreshViewByState();
                                     iRefreshlistener.onRefresh();
                                 }else if(state == PULL){
                                 //如果当前是提示下拉状态
                                     state = NONE;
                                     isFlag = false;
                                     refreshViewByState();
                                 }
                             break;
        }
        return super.onTouchEvent(ev);
    }


    IRefreshListener iRefreshlistener;//刷新数据的接口

    public void setInterface(IRefreshListener listener){
        this.iRefreshlistener = listener;
    }
    /**
     * 定义刷新数据接口
     * @author lenovo
     *
     */
    public interface IRefreshListener{
        public void onRefresh();
    }

    /**
     * 根据当前状态state,改变界面显示
     * state:
     *      NONE：无操作
     *      PULL：下拉可以刷新
     *      RELEASE：松开可以刷新
     *      REFREASH：正在刷新
     */
    private void refreshViewByState(){
        //提示
        TextView tips = (TextView)header.findViewById(R.id.tips);
        //箭头
        ImageView arrow = (ImageView)header.findViewById(R.id.arrow);
        //进度条
        ProgressBar progress = (ProgressBar)header.findViewById(R.id.progress);
        //箭头的动画效果1，由0度转向180度，即箭头由朝下转为朝上
        RotateAnimation animation1 = new RotateAnimation(0, 180,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation1.setDuration(500);
        animation1.setFillAfter(true);
        //箭头的动画效果2，由180度转向0度，即箭头由朝上转为朝下
        RotateAnimation animation2 = new RotateAnimation(180, 0,
                RotateAnimation.RELATIVE_TO_SELF,0.5f,
                RotateAnimation.RELATIVE_TO_SELF,0.5f);
        animation2.setDuration(500);
        animation2.setFillAfter(true);

        switch (state) {
            case NONE:                     //正常状态
                arrow.clearAnimation();    //清除箭头动画效果
                topPadding(-headerHeight); //设置header距离顶部的距离
                break;

            case PULL:                                //下拉状态
                arrow.setVisibility(View.VISIBLE);    //箭头设为可见
                progress.setVisibility(View.GONE);    //进度条设为不可见
                tips.setText("下拉可以刷新");           //提示文字设为"下拉可以刷新"
                arrow.clearAnimation();               //清除之前的动画效果，并将其设置为动画效果2
                arrow.setAnimation(animation2);
                break;

            case RELEASE:                            //下拉状态
                arrow.setVisibility(View.VISIBLE);   //箭头设为可见
                progress.setVisibility(View.GONE);   //进度条设为不可见
                tips.setText("松开可以刷新");          //提示文字设为"松开可以刷新"
                arrow.clearAnimation();              //清除之前的动画效果，并将其设置为动画效果2
                arrow.setAnimation(animation1);
                break;

            case REFRESH:                             //更新状态
                topPadding(50);                       //距离顶部的距离设置为50
                arrow.setVisibility(View.GONE);       //箭头设为不可见
                progress.setVisibility(View.VISIBLE); //进度条设为可见
                tips.setText("正在刷新...");            //提示文字设为""正在刷新..."
                arrow.clearAnimation();                //清除动画效果
                break;

        }
    }

}
