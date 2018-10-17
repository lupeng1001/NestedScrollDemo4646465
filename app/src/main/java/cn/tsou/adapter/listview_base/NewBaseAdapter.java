package cn.tsou.adapter.listview_base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tuacy.nestedscrolldemo.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;



/**
 * 带上拉加载更多的listview基类adapter
 * @author lupeng
 */

public abstract class NewBaseAdapter<T> extends BaseAdapter {
    protected DecimalFormat fnum = new DecimalFormat("##0.00");
    private static final String TAG = "NewBaseAdapter";
    private static final int NORMAL_TYPE = 1;
    private static final int FOOT_VIEW_TYPE = 0;
    protected List<T> mData = new ArrayList<T>();
    protected Context mContext;
    protected LayoutInflater mInflater;
    //view的类型有几种(不包含加载view)
    protected int view_type_count = 1;

    public int getView_type_count() {
        return view_type_count;
    }

    public void setView_type_count(int view_type_count) {
        this.view_type_count = view_type_count;
    }

    //是否支持分页加载
    protected boolean can_load = true;
    //数据是否已经加载完毕
    protected boolean is_load_finish = false;


    public boolean isCan_load() {
        return can_load;
    }

    public void setCan_load(boolean can_load) {
        this.can_load = can_load;
    }

    public boolean is_load_finish() {
        return is_load_finish;
    }

    public void setIs_load_finish(boolean is_load_finish) {
        this.is_load_finish = is_load_finish;
    }

    public NewBaseAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void clearDataList(){
        this.mData.clear();
        notifyDataSetChanged();
    }

    public List<T> getmData() {
        return mData;
    }

    public void setmData(List<T> mData) {
        this.mData.addAll(mData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(can_load){
            return mData.size()+1;
        }
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if(can_load){
            if(position+1 == getCount()){
                return FOOT_VIEW_TYPE;
            }else{
                return NORMAL_TYPE;
            }
        }
        return NORMAL_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        if(can_load){
            return getView_type_count()+1;
        }
        return getView_type_count();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(getItemViewType(position)==NORMAL_TYPE){
            //普通类型的view
            convertView = convertNormalView(position,convertView,parent);
        }else if(getItemViewType(position)==FOOT_VIEW_TYPE){
            FooterViewHolder holder = null;
            //底部view类型
            if(convertView==null){
                convertView =mInflater.inflate(R.layout.new_layout_footer_list,null);
                holder = new FooterViewHolder();
                holder.load_progress = (ProgressBar) convertView.findViewById(R.id.load_progress);
                holder.load_txt = (TextView)convertView.findViewById(R.id.load_txt);
                convertView.setTag(holder);
            }else{
                holder = (FooterViewHolder)convertView.getTag();
            }

            if(is_load_finish){
                holder.load_progress.setVisibility(View.GONE);
                holder.load_txt.setText("-------- 我是有底线的 --------");
            }else{
                holder.load_progress.setVisibility(View.VISIBLE);
                holder.load_txt.setText("加载中...");
            }
        }
        return convertView;

    }

    protected abstract View convertNormalView(int position, View convertView, ViewGroup parent);


    class FooterViewHolder{
        ProgressBar load_progress;
        TextView load_txt;
    }
}
