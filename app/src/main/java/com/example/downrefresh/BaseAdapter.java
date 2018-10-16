package com.example.downrefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * recycleview通用基类adapter
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder>  {
    protected DecimalFormat fnum = new DecimalFormat("##0.00");
    private List<T> mList = new ArrayList<>();
    private int layoutId;
    protected Context mContext;
    protected Boolean isFootView = false;//是否添加了FootView
    private String footViewText = "";//FootView的内容

    //两个final int类型表示ViewType的两种类型
    private final int NORMAL_TYPE = 0;
    private final int FOOT_TYPE = 1111;

    public BaseAdapter(int layoutId, List<T> list,Context context){
        this.mContext = context;
        this.layoutId=layoutId;
        this. mList.addAll(list);
    }

    public List<T> getmList() {
        return mList;
    }

    public void setmList(List<T> mList) {
        this.mList = mList;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 0;
    }

    //创建一个方法来设置footView中的文字
    public void setFootViewText(String footViewText) {
        isFootView = true;
        this.footViewText = footViewText;
    }

    @Override
    public int getItemViewType(int position) {
        if (position+1 == getItemCount()) {
            return FOOT_TYPE;
        }
        return NORMAL_TYPE;
    }


    //onCreateViewHolder用来给rv创建缓存
    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //参数3 判断条件 true  1.打气 2.添加到paraent
        // false 1.打气 （参考parent的宽度）
        View view =  LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        BaseHolder holder = new BaseHolder(view);
        return holder;
    }
    //onBindViewHolder给缓存控件设置数据
    @Override
    public void onBindViewHolder(BaseHolder holder, int position) {
        T item = mList.get(position);
        convert(holder,item,position);
    }
    protected void convert(BaseHolder holder, T item,int position) {
        //什么都没有做
    }

}
