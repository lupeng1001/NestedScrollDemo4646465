package com.example.downrefresh;

/**
 * Created by Administrator on 2018/10/16.
 */

public class TestNdkUtils {
    static{
        System.loadLibrary("newlib");
    }
    public native String helloWorld();
}
