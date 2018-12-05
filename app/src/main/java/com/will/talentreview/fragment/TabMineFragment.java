package com.will.talentreview.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.tamic.novate.util.FileUtil;
import com.will.talentreview.R;
import com.will.talentreview.activity.AuthenticationActivity;
import com.will.talentreview.activity.LoginActivity;
import com.will.talentreview.activity.MyCollectionListActivity;
import com.will.talentreview.activity.MyMessageActivity;
import com.will.talentreview.activity.SettingActivity;
import com.will.talentreview.application.MyApplication;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.constant.RequestKey;
import com.will.talentreview.entity.ImageUpload;
import com.will.talentreview.entity.LoginInfo;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.CropUtils;
import com.will.talentreview.utils.DateUtils;
import com.will.talentreview.utils.GlideUtil;
import com.will.talentreview.utils.PermissionUtil;
import com.will.talentreview.utils.PhotoUtil;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.utils.UriUtil;
import com.will.talentreview.view.CircleImageView;
import com.will.talentreview.view.CommonTitle;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import okhttp3.Call;

/**
 * @author chenwei
 * @time 2018-11-19
 */

public class TabMineFragment extends BaseFragment implements View.OnClickListener {

    public static synchronized TabMineFragment getInstance() {
        return new TabMineFragment();
    }

    private final String TAG_SUBMIT_BIRTHDAY = TAG + "_submit_birthday";
    private final String TAG_UPLOAD = TAG + "_upload";
    private final String TAG_LOGIN_INFO = TAG + "_login_info";

    private final int REQUEST_CODE_AUTHENTICATION = 1;
    private final int REQUEST_CODE_SETTING = 2;

    private CircleImageView mivAvatar;
    private TextView mtvName;
    private TextView mtvAuthentication;
    private TextView mtvBirthday;
    private LinearLayout mllMyBought;
    private LinearLayout mllMyCollection;
    private LinearLayout mllMyBirthDay;
    private LinearLayout mllNotice;
    private LinearLayout mllSetting;


    private DatePickDialog datePickDialog;
    private PhotoUtil photoUtil;

    @Override
    protected int getContentView() {
        return R.layout.fragment_tab_mine;
    }

    @Override
    protected void initView(View contentView) {
        CommonTitle commonTitle=new CommonTitle(getActivity(),contentView.findViewById(R.id.include),CommonTitle.TITLE_TYPE_5);
        commonTitle.setTitleCenter("个人中心");
        mivAvatar = contentView.findViewById(R.id.iv_avatar);
        mtvName = contentView.findViewById(R.id.tv_name);
        mtvAuthentication = contentView.findViewById(R.id.tv_authentication);
        mtvBirthday = contentView.findViewById(R.id.tv_my_birthday);

        mivAvatar.setOnClickListener(this);
        mtvAuthentication.setOnClickListener(this);
        contentView.findViewById(R.id.ll_my_bought).setOnClickListener(this);
        contentView.findViewById(R.id.ll_my_collection).setOnClickListener(this);
        contentView.findViewById(R.id.ll_my_birthday).setOnClickListener(this);
        contentView.findViewById(R.id.ll_notice).setOnClickListener(this);
        contentView.findViewById(R.id.ll_setting).setOnClickListener(this);
    }

    @Override
    protected void initData() {

        photoUtil = new PhotoUtil(activity);
        setLoginInfo();
    }

    private void setLoginInfo() {
        LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
        if (loginInfo == null) {
            mivAvatar.setImageResource(R.mipmap.img_default_avatar);
            mtvName.setText("未登录");
            mtvBirthday.setText("");
            mtvAuthentication.setText("未认证");
        } else {
            String birthday = loginInfo.getBirthday();
            if (!TextUtils.isEmpty(birthday)) {
                birthday = birthday.split(" ")[0];
            } else {
                birthday = "";
            }
            GlideUtil.showAvatar(getActivity(), loginInfo.getHeadUrl(), mivAvatar);
            mtvName.setText(StringUtils.excludeNull(loginInfo.getNickName(), StringUtils.excludeNull(loginInfo.getMobilePhone(),"未知")));
            mtvBirthday.setText(birthday);
            switch (loginInfo.getStatus()) {
                case AppConstants.AuthenticationStatus.STATUS_0:
                    mtvAuthentication.setText(StringUtils.excludeNull(loginInfo.getStatusStr(), "未认证")+" >");
                    break;
                case AppConstants.AuthenticationStatus.STATUS_1:
                    mtvAuthentication.setText(StringUtils.excludeNull(loginInfo.getStatusStr(), "待审核")+" >");
                    break;
                case AppConstants.AuthenticationStatus.STATUS_2:
                    mtvAuthentication.setText(StringUtils.excludeNull(loginInfo.getStatusStr(), "已认证")+" >");
                    mtvAuthentication.setTextColor(Color.parseColor("#00cc99"));
                    break;
                case AppConstants.AuthenticationStatus.STATUS_3:
                    mtvAuthentication.setText(StringUtils.excludeNull(loginInfo.getStatusStr(), "认证失败")+" >");
                    break;
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_avatar:
                if (!judgeLogin()) {
                    return;
                }
                photoUtil.addPicture();
                break;
            case R.id.tv_authentication:
                if (!judgeLogin()) {
                    return;
                }
                int status = MyApplication.getInstance().getLoginInfo().getStatus();
                Intent intentAuthentication = new Intent(getActivity(), AuthenticationActivity.class);
                intentAuthentication.putExtra(IntentKey.STATUS, status);
                startActivityForResult(intentAuthentication, REQUEST_CODE_AUTHENTICATION);
                break;
            case R.id.ll_my_collection:
                if (!judgeLogin()) {
                    return;
                }
                startActivity(new Intent(getActivity(), MyCollectionListActivity.class));
                break;
            case R.id.ll_my_bought:
                if (!judgeLogin()) {
                    return;
                }
                Intent intent = new Intent(getActivity(), MyCollectionListActivity.class);
                intent.putExtra(IntentKey.IS_COLLECTION, false);
                startActivity(intent);
                break;
            case R.id.ll_my_birthday:
                if (!judgeLogin()) {
                    return;
                }
                showDatePicker();
                break;
            case R.id.ll_notice:
                if (!judgeLogin()) {
                    return;
                }
                startActivity(new Intent(getActivity(), MyMessageActivity.class));
                break;
            case R.id.ll_setting:
                if (!judgeLogin()) {
                    return;
                }
                startActivityForResult(new Intent(getActivity(), SettingActivity.class), REQUEST_CODE_SETTING);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            getLoginInfo();
        }
    }

    /**
     * 判断是否登录
     */
    private boolean judgeLogin() {
        if (MyApplication.getInstance().getLoginInfo() == null) {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), LoginActivity.REQUEST_CODE_LOGIN);
            return false;
        }
        return true;
    }

    protected void showDatePicker() {
        if (datePickDialog == null) {
            datePickDialog = new DatePickDialog(getActivity());
            //设置上下年分限制
            datePickDialog.setYearLimt(100);
            //设置标题
            datePickDialog.setTitle("选择时间");
            //设置类型
            datePickDialog.setType(DateType.TYPE_YMD);
            //设置消息体的显示格式，日期格式
            datePickDialog.setMessageFormat(DateType.TYPE_YMD.getFormat());
            //设置选择回调
            datePickDialog.setOnChangeLisener(null);
        } else {
            if (datePickDialog.isShowing()) {
                datePickDialog.dismiss();
            }
        }
        Date startDate = null;
        if (TextUtils.isEmpty(mtvBirthday.getText().toString())) {
            startDate = new Date();
        } else {
            try {
                DateUtils.FORMAT3.parse(mtvBirthday.getText().toString());
            } catch (ParseException e) {
                startDate = new Date();
                e.printStackTrace();
            }
        }
        datePickDialog.setStartDate(startDate);
        //设置点击确定按钮回调
        datePickDialog.setOnSureLisener(onSureLisener);
        datePickDialog.show();
    }

    private OnSureLisener onSureLisener = new OnSureLisener() {
        @Override
        public void onSure(Date date) {
            mtvBirthday.setText(DateUtils.getYyyyMMdd(date.getTime()));
            submitBirthDay();
        }
    };

    private void submitBirthDay() {
        showProgressDialog("正在修改生日，请稍后...", true, TAG_SUBMIT_BIRTHDAY);
        RequestParam param = new RequestParam();
        param.addParam("birth", mtvBirthday.getText().toString());
        RequestUtil.getInstance(getActivity()).submitBirthday(TAG_SUBMIT_BIRTHDAY, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    showShortToast("修改成功");
                    LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
                    loginInfo.setBirthday(mtvBirthday.getText().toString());
                    MyApplication.getInstance().saveLoginInfo(loginInfo);
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "修改失败，请重试"));
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtil.OnRequestPermissionsResultCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, List<String> perms, boolean isAllGranted) {
                if (requestCode == PhotoUtil.REQUEST_TASK_PHOTO) {
                    photoUtil.takePhoto();
                }
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms, boolean isAllDenied) {
                if (requestCode == PhotoUtil.REQUEST_TASK_PHOTO) {
                    showShortToast("请允许拍照相关权限");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SETTING) {
                setLoginInfo();
            } else if (requestCode == REQUEST_CODE_AUTHENTICATION) {
                getLoginInfo();
            } else if (requestCode == LoginActivity.REQUEST_CODE_LOGIN) {
                setLoginInfo();
            } else if (requestCode == PhotoUtil.REQUEST_TASK_PHOTO) {
                String path = photoUtil.getPhotoPath();
                if (path == null) {
                    return;
                }
                photoUtil.cropImageForAvatar(UriUtil.getUriFromFile(activity, path));
            } else if (requestCode == PhotoUtil.REQUEST_TASK_PICTURE) {
                if (data.getData() == null) {
                    return;
                }
                Uri uri = data.getData();
                photoUtil.cropImageForAvatar(uri);
            } else if (requestCode == PhotoUtil.REQUEST_CROP_PICTURE) {
                String path = photoUtil.getCropImagePath();
                uploadImage(path);
            }
        }
    }

    private void uploadImage(String path) {
        showProgressDialog("正在上传头像，请稍后...", true, TAG_UPLOAD);
        RequestUtil.getInstance(getActivity()).uploadFile(TAG_UPLOAD, path, new CustomRequestCallback<ImageUpload>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                if (requestResult.isSuccess()) {
                    ImageUpload image = (ImageUpload) requestResult.getData();
                    if (image != null) {
                        updateAvatar(image);
                    }
                } else {
                    closeProgressDialog();
                    showShortToast("上传失败，请重试");
                }
            }
        });
    }

    private void updateAvatar(final ImageUpload image) {
        RequestParam param = new RequestParam();
        param.addParam("picId", image.getId());
        RequestUtil.getInstance(getActivity()).updateAvatar(TAG_UPLOAD, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    showShortToast("头像修改成功");
                    GlideUtil.showAvatar(activity, image.getUrl(), mivAvatar);
                    LoginInfo loginInfo = MyApplication.getInstance().getLoginInfo();
                    loginInfo.setHeadUrl(image.getUrl());
                    MyApplication.getInstance().saveLoginInfo(loginInfo);
                } else {
                    showShortToast("上传失败，请重试");
                }
            }
        });
    }

    private void getLoginInfo() {
        if(MyApplication.getInstance().getLoginInfo()==null){
            return;
        }
        RequestParam param = new RequestParam();
        RequestUtil.getInstance(getActivity()).getLoginInfo(TAG_LOGIN_INFO, param, new CustomRequestCallback<LoginInfo>() {
            @Override
            public void onRequestFinished(RequestResult<LoginInfo> requestResult) {
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    LoginInfo loginInfo = requestResult.getData();
                    if (loginInfo != null) {
                        loginInfo.setToken(MyApplication.getInstance().getLoginInfo().getToken());
                        MyApplication.getInstance().saveLoginInfo(loginInfo);
                    }
                    setLoginInfo();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FileUtil.deleteFile(Config.IMAGE_FILE_PATH);
        FileUtil.deleteFile(Config.CROP_PIC_PATH);
    }
}
