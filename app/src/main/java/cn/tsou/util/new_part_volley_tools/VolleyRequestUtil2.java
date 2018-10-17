package cn.tsou.util.new_part_volley_tools;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tencent.weibo.sdk.android.component.sso.tools.MD5Tools;

import java.util.Map;
import java.util.TreeMap;

import ckb.android.tsou.tuils.Utils;
import screenvideo.android.tsou.entity.AdvDataShare;

/**
 * 新版本volley框架二次封装工具类
 * @author lupeng
 * @date 2018-10-10
 *
 */
public class VolleyRequestUtil2 {
    private static RequestQueue mRequestQueue;//volley请求的队列
    private Context mCtx;

    /**
     * 建议传入applicationContext对象，防止内存泄漏
     * @param context
     */
    private VolleyRequestUtil2(Context context) {
        mCtx = context;
		mRequestQueue = getRequestQueue();
	}


    private static VolleyRequestUtil2 singleton;

    public static VolleyRequestUtil2 getInstance(Context context) {
        if (singleton == null) {
            synchronized (VolleyRequestUtil2.class) {//锁住这段代码，保证同一时间只能有一个线程执行
                 //这样做可以保证只有一个VolleyRequestUtil2的实例在内存中
                if (singleton == null) {
                    singleton = new VolleyRequestUtil2(context);
                }
            }
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
           mRequestQueue = Volley.newRequestQueue(mCtx);
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * 取消所有的请求
     * @param tag
     */
    public void cancelRequest(final Object tag){
    	getRequestQueue().cancelAll(tag);
    }
    /**
     * 封装volley的post请求数据(没有特定请求时间限制)
     * @param url
     * @param TAG
     * @param para_map
     */
    public void requestPost(String url, final String TAG, final Map<String,String> para_map, VolleyInterface listener) {
        //保险起见，每次请求将当前TAG的request对象全部cancel
        cancelRequest(TAG);
        //备注：这里request对象不能作为VolleyRequestUtil2的成员变量，否则会产生内存泄漏
        Request request = new StringRequest(Request.Method.POST, url,listener.loadingListener(),listener.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.e(TAG,"接收到的para_map="+para_map);
                //附加参数的封装处理
                Map<String, String> map = new TreeMap<>();
                try {
                    //基本参数部分添加
                    map.put("mid", AdvDataShare.userId);
                    map.put("clienttype", AdvDataShare.clienttype);
                    map.put("timestamp", System.currentTimeMillis() / 1000 + "");
                    map.put("ver", AdvDataShare.version_mini);
                    //额外参数部分添加
                    if(null!=para_map&&para_map.size()>0){
                        Log.e(TAG,"para_map.size>0执行");
                        for(Map.Entry<String, String> entry: para_map.entrySet()) {
                            map.put(entry.getKey(), entry.getValue());
                            Log.e(TAG,"Key: "+ entry.getKey()+ " Value: "+entry.getValue());
                        }
                    }
                    map.put("sign", MD5Tools.toMD5((Utils.GetParamJsonStr(map) + "&" + AdvDataShare.jiami_key)
                            .getBytes("UTF-8")).toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                Gson gson = new Gson();
                Log.e(TAG,"***map.json="+gson.toJson(map));
                return map;
            }
        };
        request.setTag(TAG);
        //默认30秒超市，不自动重复发起请求
        request.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 0f));
        addToRequestQueue(request);
    }

    /**
     * 封装volley的post请求数据(有特定请求时间限制time_mills)
     * @param url
     * @param TAG
     * @param para_map
     * @param time_mills
     */
    public void requestPostLimitTime(String url, final String TAG, final Map<String,String> para_map, int time_mills,VolleyInterface listener) {
        //先清除当前没有请求完的TAG的请求
        cancelRequest(TAG);
        Request request = new StringRequest(Request.Method.POST, url,listener.loadingListener(),listener.errorListener()){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //附加参数的封装处理
                Map<String, String> map = new TreeMap<>();
                try {
                    //基本参数部分添加
                    map.put("mid", AdvDataShare.userId);
                    map.put("clienttype", AdvDataShare.clienttype);
                    map.put("timestamp", System.currentTimeMillis() / 1000 + "");
                    map.put("ver", AdvDataShare.version_mini);
                    //额外参数部分添加
                    if(null!=para_map&&para_map.size()>0){
                        for(Map.Entry<String, String> entry: map.entrySet()) {
                            map.put(entry.getKey(), entry.getValue());
                            Log.e(TAG,"Key: "+ entry.getKey()+ " Value: "+entry.getValue());
                        }
                    }
                    map.put("sign", MD5Tools.toMD5((Utils.GetParamJsonStr(map) + "&" + AdvDataShare.jiami_key)
                            .getBytes("UTF-8")).toLowerCase());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                return map;

            }
        };
        request.setTag(TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(time_mills * 1000, 0, 0f));
        addToRequestQueue(request);
    }

    /**
     * 封装volley的get请求数据（没有特定请求时间限制）
     * @param url
     * @param TAG
     */
    public void requestGet(String url, final String TAG,VolleyInterface listener) {
        cancelRequest(TAG);
        Request request = new StringRequest(Request.Method.GET, url,listener.loadingListener(),listener.errorListener());
        request.setTag(TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(30 * 1000, 0, 0f));
        addToRequestQueue(request);
    }

    /**
     * 封装volley的get请求数据（有特定请求时间限制time_mills）
     * @param url
     * @param TAG
     * @param time_mills
     */
    public void requestGetLimitTime(String url, final String TAG,int time_mills,VolleyInterface listener) {
        cancelRequest(TAG);
        Request request = new StringRequest(Request.Method.GET, url,listener.loadingListener(),listener.errorListener());
        request.setTag(TAG);
        request.setRetryPolicy(new DefaultRetryPolicy(time_mills * 1000, 0, 0f));
        addToRequestQueue(request);
    }
}
