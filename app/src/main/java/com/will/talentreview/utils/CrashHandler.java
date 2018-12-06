package com.will.talentreview.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.will.talentreview.constant.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常捕获类 捕获异常保存到手机本地SD卡中
 *
 * @author yanchenglong
 * @time 2018-03-01
 */
public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = CrashHandler.class.getSimpleName();
    // CrashHandler 实例
    private static volatile CrashHandler sInstance;
    // 程序的 Context 对象
    private Context context;
    // 系统默认的 UncaughtException 处理类
    private UncaughtExceptionHandler defaultHandler;
    // 用来存储设备信息和异常信息
    private Map<String, String> infoMap = new HashMap<>();
    // 用于格式化日期,作为日志文件名的一部分
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    // 格式化后的当前时间
    private String crashTime = "";

    /**
     * 获取 CrashHandler 实例 ,单例模式
     *
     * @return 实例
     */
    public static CrashHandler getInstance() {
        if (sInstance == null) {
            synchronized (CrashHandler.class) {
                if (sInstance == null) {
                    sInstance = new CrashHandler();
                }
            }
        }
        // 返回自己
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param ctx 上下文对象
     */
    public void init(Context ctx) {
        // 获取上下文对象
        context = ctx;
        // 获取异常
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 注册异常
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && defaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            defaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                // 等待3秒后 直接退出程序
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            // 退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            // 杀死进程
            System.exit(1);
        }

    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
     *
     * @param ex 异常
     * @return true:如果处理了该异常信息;否则返回false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 将时间格式化
        crashTime = formatter.format(new Date());
        // 收集设备参数信息
        collectDeviceInfo(context);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx 上下文对象
     */
    private void collectDeviceInfo(Context ctx) {
        if (ctx == null) {
            return;
        }
        infoMap.put("MODEL", Build.MODEL);
        infoMap.put("OS_VERSION", Build.VERSION.RELEASE);
        infoMap.put("CRASH_TIME", crashTime);
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 异常
     * @return 文件名称
     */
    private String saveCrashInfo2File(Throwable ex) {
        // 组装异常消息
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infoMap.entrySet()) {
            // 获取key
            String key = entry.getKey();
            // 获取value
            String value = entry.getValue();
            // 将key和value存入StringBuffer
            sb.append(key + ":" + value + "\n");
        }
        // 读取异常信息
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            // 异常文件名称
            String fileName = "crash-" + crashTime + ".log";
            // 保存文件
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // 创建路径
                File dir = new File(Config.CRASH_FILE_PATH);
                // 判断该路径是否存在
                if (!dir.exists()) {
                    // 如果不存在则创建
                    dir.mkdirs();
                }
                // 写入消息和文件
                FileOutputStream fos = new FileOutputStream(Config.CRASH_FILE_PATH + fileName);
                // 将异常文件写入
                fos.write(sb.toString().getBytes());
                // 关闭写入流
                fos.close();
            }
            // 返回文件名称
            return fileName;
        } catch (Exception e) {
        }
        return null;
    }
}
