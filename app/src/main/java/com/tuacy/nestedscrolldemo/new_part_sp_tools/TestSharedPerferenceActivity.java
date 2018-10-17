package com.tuacy.nestedscrolldemo.new_part_sp_tools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tuacy.nestedscrolldemo.R;

import java.util.HashMap;
import java.util.Map;

import cn.tsou.util.SharedPreferencedUtil;

public class TestSharedPerferenceActivity extends AppCompatActivity {
    private static final String TAG = "TestSharedPerference";
    private SharedPreferencedUtil sp_util;
    private String userId;
    private String userName;
    private String userKey;

    private Map<String,String> key_map = new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_shared_perference);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testSpAction();
            }
        });
    }

    public void testSpAction(){
        sp_util = SharedPreferencedUtil.getInstance(getApplicationContext());
        userId = sp_util.getSpData("userId");
        userName = sp_util.getSpData("userName");
        userKey = sp_util.getSpData("userKey");
        Log.e(TAG,"userId="+userId);
        Log.e(TAG,"userName="+userName);
        Log.e(TAG,"userKey="+userKey);
        key_map.clear();
        if(userId.equals("")){
            key_map.put("userId","101");
        }
        if(userName.equals("")){
            key_map.put("userName","lupeng");
        }
        if(userKey.equals("")){
            key_map.put("userKey","123456");
        }
        int result = sp_util.saveLocalDataMap(key_map);
        switch (result){
            case 1:
                Log.e(TAG,"sharedpreference数据写入成功");
                break;

            case 0:
                Log.e(TAG,"sharedpreference数据不需要写入");
                break;
            case -1:
                Log.e(TAG,"sharedpreference数据写入失败");
                break;
        }

    }

}
