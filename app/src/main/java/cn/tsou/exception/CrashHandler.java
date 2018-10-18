package cn.tsou.exception;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;


import cn.tsou.constant.NetworkConstant;

/**
 * 通用crash异常捕获，用于搜集用户crash日志信息
 * @auther lupeng
 * @date 2018-10-18
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private String exception_str = "";
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;

    private static final String PATH = Environment.getExternalStorageDirectory().getPath() + "/ryg_test/log/";
    private static final String FILE_NAME = "crash";

    private static final String FILE_NAME_SUFFIX = ".trace";

    private static CrashHandler sInstance = new CrashHandler();

    private UncaughtExceptionHandler mDefaultCrashHandler;

    private Context mContext;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        //获取系统默认的异常处理器  
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }


    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultCrashHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Log.e(TAG, "error : ", e);
            }
            // 退出程序
            Process.killProcess(Process.myPid());
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        final String ex_str = ex.toString();
        if (ex == null) {
            return false;
        }
        // 必须新开线程来执行，后面主线程会随时挂掉，挂掉的时机不可控
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉,程序出现异常即将退出.", Toast.LENGTH_LONG).show();
                submitExceptionToServer(ex_str);//提交crash异常到远程服务器
                Looper.loop();
            }
        }.start();
        return true;
    }


    public void submitExceptionToServer(String ex_str){
        String collect_list_url = NetworkConstant.JIEKOU_SERVE + "Error/SetLog";
        try {
            URL url = new URL(collect_list_url);
            URLConnection rulConnection;// 此处的urlConnection对象实际上是根据URL的
                    rulConnection = url.openConnection();
                    HttpURLConnection connection = (HttpURLConnection) rulConnection;
            // 请求方式
            connection.setRequestMethod("POST");
            // 超时时间
            connection.setConnectTimeout(3000);
            // 设置是否输出
            connection.setDoOutput(true);
            // 设置是否读入
            connection.setDoInput(true);
            // 设置是否使用缓存
            connection.setUseCaches(false);
            // 设置此 HttpURLConnection 实例是否应该自动执行 HTTP 重定向
            connection.setInstanceFollowRedirects(true);
            // 设置使用标准编码格式编码参数的名-值对
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            // 连接
            connection.connect();
            String params = "content="+ex_str;
            OutputStream out = connection.getOutputStream();
            out.write(params.getBytes());
            out.flush();
            out.close();
            Log.e(TAG,"post请求执行");
            int code = connection.getResponseCode();
            Log.e(TAG,"***code="+code);
            connection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e){

        }
    }

    /**
     * 用于将exception信息保存到sdk里，这里暂时没有用到，以后备用
     * @param ex
     * @throws IOException
     */
    private void dumpExceptionToSDCard(Throwable ex) throws IOException {
        Log.e("ex", ex.toString());
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        //SubmitExceptionTask(result.toString());
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }

        File dir = new File(PATH);
        if (!dir.exists()) {
        	Log.e(TAG, "建立文件夹执行啦");
            dir.mkdirs();
        }
        long current = System.currentTimeMillis();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file = new File(PATH + FILE_NAME + time + FILE_NAME_SUFFIX);
        StringBuffer sb = new StringBuffer();

        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            pw.println(time);
            //导出手机信息
            dumpPhoneInfo(pw);
            pw.println();
            //导出异常的调用栈信息
            ex.printStackTrace(pw);
            exception_str = ex.toString();
            Log.e(TAG, "exception_str="+exception_str);
            pw.close();

        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed");
        }
    }

    /**
     * 记录手机机型信息
     * @param pw
     * @throws NameNotFoundException
     */
    private void dumpPhoneInfo(PrintWriter pw) throws NameNotFoundException {
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
        pw.print("App Version: ");
        pw.print(pi.versionName);
        pw.print('_');
        pw.println(pi.versionCode);

        pw.print("OS Version: ");
        pw.print(Build.VERSION.RELEASE);
        pw.print("_");
        pw.println(Build.VERSION.SDK_INT);

        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);

        //手机型号  
        pw.print("Model: ");
        pw.println(Build.MODEL);

        //cpu架构  
        pw.print("CPU ABI: ");
        pw.println(Build.CPU_ABI);
    }

}
