package com.tuacy.nestedscrolldemo;

/**
 * Created by Administrator on 2018/10/15.
 */

public class Java2CJNI {
    static {
        System.loadLibrary("java-lib");
    }
    public native String Java2C();
}
