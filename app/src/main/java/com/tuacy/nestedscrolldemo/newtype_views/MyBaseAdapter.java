package com.tuacy.nestedscrolldemo.newtype_views;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/10.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter{
    private static final String TAG = "MyBaseAdapter";
    protected LayoutInflater mInflater;
    private List<T> mDatas = new ArrayList<T>();
    protected Context mContext;

    public MyBaseAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = fillConvertView(position, convertView, parent);
        return convertView;
    }

    protected abstract View fillConvertView(int position, View convertView, ViewGroup parent);

    public void addMoreItems(List<T> newItems, boolean isFirstLoad) {
        if (isFirstLoad) {
            this.mDatas.clear();
        }
        this.mDatas.addAll(newItems);
        notifyDataSetChanged();
    }

    public void removeAllItems() {
        this.mDatas.clear();
        notifyDataSetChanged();
    }
}
