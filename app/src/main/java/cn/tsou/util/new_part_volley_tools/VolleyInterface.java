package cn.tsou.util.new_part_volley_tools;

import com.android.volley.Response;
import com.android.volley.VolleyError;


/**
 * volley请求回调接口方法的封装
 * @author lupeng
 * @date 2018-10-10
 */

public abstract class VolleyInterface {
    public Response.Listener<String> listener;
    public Response.ErrorListener errorListener;


    public VolleyInterface() {
    }

    public abstract void onMySuccess(String result);

    public abstract void onMyError(VolleyError error);

    public Response.Listener<String> loadingListener() {
        listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                onMySuccess(s);
            }
        };
        return listener;
    }

    public Response.ErrorListener errorListener() {
        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onMyError(volleyError);
            }
        };
        return errorListener;
    }




}
