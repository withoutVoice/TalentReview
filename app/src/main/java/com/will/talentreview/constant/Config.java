package com.will.talentreview.constant;

import android.os.Environment;

/**
 * 核心包配置
 *
 * @author chenwei
 * @time 2018-9-05
 */

public class Config {

    public static final boolean DEBUG_MODE = true;


    //通用列表每页数量
    public static final int PAGE_SIZE = 20;

    //批处理最大个数
    public static final int BATCH_PROCESSING_MAX_SIZE = 5;

    /**
     * 通用图片上传最大数量
     */
    public static final int COMMON_IMAGE_UPLOAD_SIZE = 5;

    /**
     * sd卡路径
     */
    public static String SD_CARD_PATH;
    /**
     * 是否开启日志
     */
    public final static boolean LOG = true;
    /**
     * 数据库名称
     */
    public final static String DATABASE_NAME = "talent_review.db";
    /**
     * 数据库版本
     */
    public final static int DATABASE_VERSION = 1;
    /**
     * 崩溃日志存储
     */
    public static String CRASH_FILE_PATH;
    /**
     * 更新apk存储路径
     */
    public static String APK_FILE_PATH;
    /**
     * 原图存储路径
     */
    public static String IMAGE_FILE_PATH;
    /**
     * 压缩图片路径
     */
    public static String THUMBNAIL_FILE_PATH;
    /**
     * 剪裁图片存储路径
     */
    public static String CROP_PIC_PATH;
    /**
     * 视频存储路径
     */
    public static String VIDEO_PATH;
    /**
     * 录音存储路径
     */
    public static String VOICE_PATH;
    /**
     * 默认sp文件名称
     */
    public final static String SP_NAME = "default_config";

    public final static int TOKEN_ERROR_CODE = 501;

}
