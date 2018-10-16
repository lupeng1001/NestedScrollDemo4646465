package com.tuacy.nestedscrolldemo;

import android.util.Log;

/**
 * Created by Administrator on 2018/10/15.
 */

public class Java2CJNI {
    private static final String TAG = "Java2CJNI";
    static {
        Log.e(TAG,"static部分代码开始执行33333");
        System.loadLibrary("java-lib");
    }
    public native String Java2C();
}
