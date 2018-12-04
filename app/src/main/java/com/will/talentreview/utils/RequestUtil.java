package com.will.talentreview.utils;

import android.content.Context;
import android.text.TextUtils;

import com.tamic.novate.Novate;
import com.tamic.novate.RxApiManager;
import com.tamic.novate.callback.ResponseCallback;
import com.tamic.novate.callback.RxStringCallback;
import com.tamic.novate.download.UpLoadCallback;
import com.tamic.novate.request.NovateRequestBody;
import com.tamic.novate.util.Utils;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.Config;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscription;

/**
 * @author chenwei
 * @time 2018-11-22
 */

public class RequestUtil {

    private final static String BASE_URL = "http://210.29.36.181:8081";
    private final static String UPLOAD_FILE = "/renren-api/api/file/upload";
    //登录
    private final static String DO_LOGIN = "/renren-api/api/user/login";
    //找回密码
    private final static String FIND_PASSWORD = "/renren-api/api/user/findPwd";
    //退出登录
    private final static String DO_LOGIN_OUT = "/renren-api/api/user/logout";
    //发送验证码
    private final static String SEND_CODE = "/renren-api/api/user/sendSms";
    //修改生日或爱好
    private final static String UPDATE_BIRTHDAY = "/renren-api/api/user/updateBirthOrHobby";
    //修改密码
    private final static String UPDATE_PASSWORD = "/renren-api/api/user/updatePwd";
    //修改手机号码
    private final static String UPDATE_MOBILE = "/renren-api/api/user/updateMobile";
    //获取用户信息
    private final static String GET_USER_INFO = "/renren-api/api/user/userInfo";
    //获取认证信息
    private final static String GET_AUTHENTICATION_INFO = "/renren-api/api/realname/getRealNameInfo";
    //获取公告列表
    private final static String GET_NOTICE_LIST = "/renren-api/api/index/getListByPage";
    //提交实名验证
    private final static String SUBMIT_AUTHENTICATION = "/renren-api/api/realname/approve";
    //收藏产品
    private final static String COLLECT_PRODUCT = "/renren-api/api/project/collectProject";
    //购买产品
    private final static String BUY_PRODUCT = "/renren-api/api/project/saveOrder";
    //获取留言列表
    private final static String GET_TALK_LIST = "/renren-api/api/project/getMessageListByPage";
    //提交留言
    private final static String SUBMIT_TALK = "/renren-api/api/project/saveMessage";
    //获取产品列表
    private final static String GET_PRODUCT_LIST = "/renren-api/api/project/getProjectListByPage";
    //修改头像
    private final static String UPDATE_AVATAR = "/renren-api/api/user/updatePhoto";
    //获取轮播
    private final static String GET_GALLERY = "/renren-api/api/index/getListByPage";
    //查字典
    private final static String GET_DICT_LIST = "/renren-api/api/project/getSysDicListByDicType";

    private static RequestUtil instance;

    public synchronized static RequestUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (RequestUtil.class) {
                instance = new RequestUtil(context);
            }
        }
        return instance;
    }

    private Novate mNovate;

    private RequestUtil(Context context) {
        HashMap<String, Object> headers = new HashMap<>();
        mNovate = new Novate.Builder(context).addHeader(headers).baseUrl(BASE_URL).addLog(Config.DEBUG_MODE).build();
    }

    private void doPost(Object cancelTag, String url, RequestParam params, ResponseCallback callBack) {
        Subscription subscription = (Subscription) mNovate.rxPost(url, params.getParams(), callBack);
        if (!TextUtils.isEmpty(cancelTag.toString())) {
            RxApiManager.get().add(cancelTag, subscription);
        }
    }

    public void cancelRequest(String cancelTag) {
        RxApiManager.get().cancel(cancelTag);
    }

    /**
     * 登录
     */
    public void doLogin(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, DO_LOGIN, params, callBack);
    }

    /**
     * 获取用户信息
     */
    public void getLoginInfo(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_USER_INFO, params, callBack);
    }
    /**
     * 获取认证信息
     */
    public void getAuthenticationInfo(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_AUTHENTICATION_INFO, params, callBack);
    }

    /**
     * 退出登录
     */
    public void doLogout(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, DO_LOGIN_OUT, params, callBack);
    }

    /**
     * 找回密码
     */
    public void findPassword(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, FIND_PASSWORD, params, callBack);
    }

    /**
     * 发送验证码
     */
    public void sendCode(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, SEND_CODE, params, callBack);
    }

    /**
     * 获取公告列表
     */
    public void getNoticeList(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_NOTICE_LIST, params, callBack);
    }

    /**
     * 提交实名验证
     */
    public void submitAuthentication(String cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, SUBMIT_AUTHENTICATION, params, callBack);
    }

    /**
     * 提交生日
     */
    public void submitBirthday(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, UPDATE_BIRTHDAY, params, callBack);
    }

    /**
     * 提交生日
     */
    public void updatePassword(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, UPDATE_PASSWORD, params, callBack);
    }

    /**
     * 提交生日
     */
    public void updatePhone(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, UPDATE_MOBILE, params, callBack);
    }

    /**
     * 收藏产品
     */
    public void collectProduct(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, COLLECT_PRODUCT, params, callBack);
    }

    /**
     * 购买产品
     */
    public void buyProduct(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, BUY_PRODUCT, params, callBack);
    }

    /**
     * 获取留言列表
     */
    public void getTalkList(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_TALK_LIST, params, callBack);
    }

    /**
     * 获取产品列表
     */
    public void getProductList(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_PRODUCT_LIST, params, callBack);
    }

    /**
     * 提交留言
     */
    public void submitTalk(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, SUBMIT_TALK, params, callBack);
    }

    /**
     * 提交留言
     */
    public void updateAvatar(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, UPDATE_AVATAR, params, callBack);
    }

    /**
     * 获取首页轮播
     */
    public void getHomeGallery(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_GALLERY, params, callBack);
    }

    /**
     * 查字典
     */
    public void getDictList(Object cancelTag, RequestParam params, ResponseCallback callBack) {
        doPost(cancelTag, GET_DICT_LIST, params, callBack);
    }

    public void uploadFile(String cancelTag, String filePath, CustomRequestCallback callback) {
        File file = new File(filePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), file);
        final NovateRequestBody requestBody = Utils.createNovateRequestBody(requestFile, new UpLoadCallback() {
            @Override
            public void onProgress(Object tag, int progress, long speed, boolean done) {
            }
        });
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("imgFile", file.getName(), requestBody);
        //请将image改成你和服务器约定好的key
        Subscription subscription = (Subscription) mNovate.RxUploadWithPart(UPLOAD_FILE, body2, callback);
        if (!TextUtils.isEmpty(cancelTag)) {
            RxApiManager.get().add(cancelTag, subscription);
        }
    }

}
