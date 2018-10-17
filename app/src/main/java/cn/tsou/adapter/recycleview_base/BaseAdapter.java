package cn.tsou.adapter.recycleview_base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



/**
 * recycleview通用基类adapter （自带上拉加载更多功能）
 * @author lupeng
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder>  {
    protected DecimalFormat fnum = new DecimalFormat("##0.00");
    private List<T> mList = new ArrayList<>();
    private int layoutId;
    protected Context mContext;

    //两个final int类型表示ViewType的两种类型
    protected int NORMAL_TYPE = 0;
    protected int FOOT_TYPE = 1111;

    protected Boolean isFootView = false;//是否添加了FootView
    protected String footViewText = "";//FootView的内容
    //是否加载完毕
    private boolean is_load_finish;

    public String getFootViewText() {
        return footViewText;
    }

    public void setFootViewText(String footViewText) {
        this.footViewText = footViewText;
    }

    public boolean is_load_finish() {
        return is_load_finish;
    }

    public void setIs_load_finish(boolean is_load_finish) {
        this.is_load_finish = is_load_finish;
    }

    public BaseAdapter(int layoutId, List<T> list, Context context){
        this.mContext = context;
        this.layoutId=layoutId;
        this.mList.addAll(list);
    }

    public List<T> getmList() {
        return mList;
    }

    /**
     * 清除所有数据
     */
    public void clearDataList(){
        this.mList.clear();
        notifyDataSetChanged();
    }


    public void setmList(List<T> mList) {
        this.mList.addAll(mList);
    }

    //onCreateViewHolder用来给rv创建缓存
    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //参数3 判断条件 true  1.打气 2.添加到paraent
        // false 1.打气 （参考parent的宽度）
        View view = null;
        if(viewType==NORMAL_TYPE){
            view =  LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        }else if(viewType==FOOT_TYPE){
            view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footer_list, parent, false);
        }
        BaseHolder holder = new BaseHolder(view);
        return holder;
    }
    //onBindViewHolder给缓存控件设置数据
    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        if(getItemViewType(position)==NORMAL_TYPE){
            T item = mList.get(position);
            convert(holder,item,position);
        }else{
            convert(holder,null,position);
        }

    }
    private void convert(BaseHolder holder, T item,int position) {
        if(getItemViewType(position)==FOOT_TYPE){
            //上拉加载更多显示的布局内容(通用)
            if(!is_load_finish()){
                ((ProgressBar)holder.getView(R.id.load_progress)).setVisibility(View.VISIBLE);
                ((TextView)holder.getView(R.id.load_txt)).setText("正在加载");
            }else{
                ((ProgressBar)holder.getView(R.id.load_progress)).setVisibility(View.GONE);
                ((TextView)holder.getView(R.id.load_txt)).setText("-----我是有底线的-----");
            }

        }else if(getItemViewType(position)==NORMAL_TYPE){
            normalConvert(holder,item,position);
        }
    }

    protected void normalConvert(BaseHolder holder, T item,int position){

    }

    @Override
    public int getItemCount() {
        if(!footViewText.equals("")){//是否支持上拉加载更多
            return mList != null ? mList.size() + 1 : 0;
        }else{
            return mList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(!footViewText.equals("")){//是否支持上拉加载更多
            if (position+1 == getItemCount()) {
                return FOOT_TYPE;
            }
            return NORMAL_TYPE;
        }else{
            return NORMAL_TYPE;
        }
    }

}
