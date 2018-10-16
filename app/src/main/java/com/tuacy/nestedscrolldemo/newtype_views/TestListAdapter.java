package com.tuacy.nestedscrolldemo.newtype_views;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;

/**
 * Created by Administrator on 2018/9/10.
 */

public class TestListAdapter extends MyBaseAdapter<SaveData>{
    private static final String TAG = "TestListAdapter";

    public TestListAdapter(Context context) {
        super(context);
    }

    @Override
    protected View fillConvertView(int position, View convertView, ViewGroup parent) {
        HolderView holder = null;
        if(convertView==null){
            holder = new HolderView();
            convertView = mInflater.inflate(R.layout.new_choose_tixian_type_item_layout, null);
            holder.tixian_type_image = (ImageView)convertView.findViewById(R.id.tixian_type_image);
            holder.tixian_type_name = (TextView) convertView.findViewById(R.id.tixian_type_name);
            holder.tixian_type_choose_image = (ImageView)convertView.findViewById(R.id.tixian_type_choose_image);
            convertView.setTag(holder);
        }else{
            holder = (HolderView)convertView.getTag();
        }
        return convertView;
    }

    class HolderView {
        ImageView tixian_type_image;
        TextView tixian_type_name;
        ImageView tixian_type_choose_image;
    }
}
