package com.will.talentreview.constant;

/**
 * @author chenwei
 * @time 2018-11-29
 */

public class AppConstants {

    public class SpKey {
        public static final String LOGIN_INFO = "login_info";
        public static final String LOGIN_NAME = "login_name";
        public static final String LOGIN_METHOD = "login_method";
    }

    public class HomeDataType {
        //轮播
        public static final int TYPE1 = 1;
        //公告
        public static final int TYPE2 = 2;
        //新手
        public static final int TYPE3 = 3;
        //金融
        public static final int TYPE4 = 4;
        //平台
        public static final int TYPE5 = 5;
    }

    public class WebFromWhat {
        //轮播
        public static final int NOTICE = 1;
        //公告
        public static final int GALLERY = 2;
        //产品亮点
        public static final int BRIGHT_POINT = 3;
        //扫码
        public static final int SCAN_CODE = 4;
    }

    public class ProductLabel {
        //今日推荐
        public static final int RECOMMEND = 11;
        //新手专享
        public static final int NEW_PLAYER = 12;
    }

    /**
     * 登录方式常量
     * */
    public class LoginMethod {
        //验证码登录
        public static final int CODE = 1;
        //密码登录
        public static final int PASSWORD = 2;
    }

    /**
     * 认证状态
     * */
    public class AuthenticationStatus {
        //未认证
        public static final int STATUS_0 = 0;
        //待审核
        public static final int STATUS_1 = 1;
        //通过
        public static final int STATUS_2 = 2;
        //未通过
        public static final int STATUS_3 = 3;
    }

}
