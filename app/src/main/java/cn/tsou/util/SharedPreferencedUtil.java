package cn.tsou.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * 新版本sharedpreferenced单例管理类
 * @auther lupeng
 * @date 2018-10-17
 */

public class SharedPreferencedUtil {
    private static final String TAG = "NewSharedPreferencedUtil";
    private static SharedPreferences sp;
    private static SharedPreferencedUtil instance;
    private static Context mContext;//这里需要传入的是applicationcontext
    public static SharedPreferences.Editor editor;


    //以下为需要缓存的数据部分
    private String userId;
    private String userName;
    private String userKey;


    private SharedPreferencedUtil(Context context){
        mContext = context;
        //获取SharedPreferences对象
        sp = mContext.getSharedPreferences("save_user_data",Context.MODE_PRIVATE);
        //获取editor对象
        editor = sp.edit();
        loadInitData();
    }

    /**
     * 即时保存缓存数据
     * @param key_map
     */
    public synchronized int saveLocalDataMap(Map<String,String> key_map){
        int result = 0;
        if(key_map.size()>0){
            for(Map.Entry<String,String> entry:key_map.entrySet()){
                editor.putString(entry.getKey(),entry.getValue());
            }
            try{
                editor.commit();
                result = 1;
            }catch (Exception e){
                result = -1;
            }
        }
        return result;
    }




    /**
     * 加载初始保存的数据
     */
    public void loadInitData() {
        userId = sp.getString("userId","");
        userName = sp.getString("userName","");
        userKey = sp.getString("userKey","");
    }


    public String getSpData(String key){
        return sp.getString(key,"");
    }



    public static SharedPreferencedUtil getInstance(Context context){
        if(instance==null){
            synchronized (SharedPreferencedUtil.class){
                if(instance==null){
                    instance = new SharedPreferencedUtil(context);
                }
            }
        }
        return instance;
    }



}
