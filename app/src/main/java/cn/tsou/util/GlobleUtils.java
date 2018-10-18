package cn.tsou.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import cn.tsou.constant.DeviceInfo;

/**
 * 综合工具类
 * @auther lupeng
 * @date 2018-10-18
 */
public class GlobleUtils {
    private static final String CPU_INFO_ARM = "armv";
    private static final String CPU_INFO_FEATURES = "features";
    /**
     * 判断当前是否有网络链接
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {// 逐一查找状态为已连接的网络
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    /**
     * 获取Cpu版本
     *
     * @param cupInfo
     * @return
     */
    public static int getCpuVersion(String cupInfo) {
        Integer cupI = 7;
        int cupStartIndex = cupInfo.indexOf(CPU_INFO_ARM);
        String cpuVersionTmp = cupInfo.substring(cupStartIndex + 4,
                cupStartIndex + 6).trim();
        int digitLength = -1;
        for (int i = 0; i < cpuVersionTmp.length(); i++) {
            if (Character.isDigit(cpuVersionTmp.charAt(i))) {
                digitLength = i;
            }
        }
        // digitLength从0开始,需要加1
        digitLength += 1;
        String cpuVersion = null;
        if (digitLength > 0) {
            cpuVersion = cpuVersionTmp.substring(0, digitLength);
            try {
                cupI = Integer.valueOf(cpuVersion);
            } catch (NumberFormatException nfe) {
            }
        }
        return cupI;
    }

    /**
     * 获取参数按照ascii值排序的json字符串
     *
     * @return
     */
    public static String GetParamJsonStr(Map<String, String> map) {
        StringBuffer jiami_json_sb = new StringBuffer();
        String jiami_json_str = "";
        for (Map.Entry<String, String> n : map.entrySet()) {
            jiami_json_sb.append(n.getValue() + "&");
        }
        jiami_json_str = jiami_json_sb.toString();
        jiami_json_str = jiami_json_str.substring(0, jiami_json_str.length() - 1);
        //Log.e("GetParamJsonStr", "jiami_json_str=" + jiami_json_str);
        return jiami_json_str;
    }

    /**
     * 将当前时间转化为时间戳 19:40
     *
     * @return
     */
    public static String FormateLocalTime() {
        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND);    // 秒
        Log.e("FormateLocalTime", "seconds=" + seconds);
        Log.e("mileseconds", System.currentTimeMillis() / 1000 + "");
        Long mileseconds = System.currentTimeMillis() / 1000 - seconds;
        return mileseconds + "";
    }

    /**
     * 获取Cpu信息
     *
     * @return
     */
    public static String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader bufferedReader = new BufferedReader(fr, 8192);
            String lowerCase = null;
            while ((str2 = bufferedReader.readLine()) != null) {
                lowerCase = str2.toLowerCase();
                if (lowerCase.contains(CPU_INFO_ARM)) {
                    cpuInfo[0] = lowerCase;
                } else if (lowerCase.contains(CPU_INFO_FEATURES)) {
                    cpuInfo[1] = lowerCase;
                }

            }
            bufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }



    /**
     * 获取手机信息
     * @return
     */
    public static DeviceInfo getDeviceInfo(Activity context) {
        DeviceInfo deviceInfo = new DeviceInfo();
        //DisplayMetrics 一个描述普通显示信息的结构，例如显示大小、密度、字体尺寸
        DisplayMetrics displaysMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay()
                .getMetrics(displaysMetrics);
        deviceInfo.screenWidth = displaysMetrics.widthPixels;// 屏幕宽
        deviceInfo.screenHeight = displaysMetrics.heightPixels;// 屏幕高
        deviceInfo.screenType = DeviceInfo.SCREEN_TYPE_PHONE;
        //sdk版本
        deviceInfo.sdkVersion = Build.VERSION.SDK_INT;
        //cpu版本
        String[] cpuInfo = GlobleUtils.getCpuInfo();
        deviceInfo.machineType = DeviceInfo.MACHINE_TYPE_ANDROID_PHONE;
        deviceInfo.manufacturer = Build.MANUFACTURER;
        deviceInfo.model = Build.MODEL;

        return deviceInfo;
    }

    /**
     * 将图片的 Bitmap 转换成 byte[]
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    // /**
    // * 将两个byte[]数组装在一个byte[]数组中
    // */
    // public static byte[] getTwoByteAdd(byte[] byte1, byte[] byte2)
    // throws IOException {
    // byte[] byte3 = new byte[byte1.length + byte2.length];
    // for (int i = 0; i < byte1.length; i++) {
    // byte3[i] = byte1[i];
    // }
    // for (int i = 0; i < byte2.length; i++) {
    // byte3[byte1.length + i] = byte2[i];
    // }
    // return byte3;
    // }

    /**
     * 返回当前程序版本名
     */
    public static int getAppVersionName(Context context) {
        String versionName = "";
        int versionCode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versionCode = pi.versionCode;

            System.out.println("version  info " + versionCode + "     "
                    + versionName);

            if (versionCode <= 0) {
                versionCode = 0;
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    /**
     * 获取手机的 IMEI 唯一标示
     *
     * @param context
     * @return
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static String getAppName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        CharSequence c = null;
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                c = pm.getApplicationLabel(pm.getApplicationInfo(
                        info.processName, PackageManager.GET_META_DATA));
                if (c != null)
                    return c.toString();
            } catch (Exception e) {
                // Name Not FOund Exception
            }
        }
        return null;
    }
}
