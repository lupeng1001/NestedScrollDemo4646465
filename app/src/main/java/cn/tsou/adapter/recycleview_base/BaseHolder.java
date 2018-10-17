package cn.tsou.adapter.recycleview_base;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

/**
 * recycleview adapter通用holderview基类
 */

public class BaseHolder extends RecyclerView.ViewHolder{
    //不写死控件变量，而采用Map方式
    private HashMap<Integer, View> mViews = new HashMap<>();
    public BaseHolder(View itemView) {
        super(itemView);
    }

    public HashMap<Integer, View> getmViews() {
        return mViews;
    }

    public void setmViews(HashMap<Integer, View> mViews) {
        this.mViews = mViews;
    }

    /**
     *获取控件的方法
     */
    public<T> T getView(Integer viewId){
        //根据保存变量的类型 强转为该类型
        View view = mViews.get(viewId);
        if(view==null){
            view= itemView.findViewById(viewId);
            //缓存
            mViews.put(viewId,view);
        }
        return (T)view;
    }
    /**
     *传入文本控件id和设置的文本值，设置文本
     */
    public BaseHolder setText(Integer viewId, String value){
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setText(value);
        }
        return this;
    }
    /**
     * 传入图片控件id和资源id，设置图片
     */
    public BaseHolder setImageResource(Integer viewId, Integer resId) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
        return this;
    }


    public BaseHolder setMRelativeLayout(Integer viewId){
        RelativeLayout relativeLayout = getView(viewId);
        return this;
    }
    public BaseHolder setMLinearLayout(Integer viewId){
        LinearLayout linearLayout = getView(viewId);
        return this;
    }
}
