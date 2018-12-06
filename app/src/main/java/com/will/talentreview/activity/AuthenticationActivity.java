package com.will.talentreview.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codbking.widget.OnSureLisener;
import com.ghnor.flora.callback.Callback;
import com.tamic.novate.RxApiManager;
import com.will.talentreview.R;
import com.will.talentreview.constant.AppConstants;
import com.will.talentreview.constant.Config;
import com.will.talentreview.constant.IntentKey;
import com.will.talentreview.entity.Authentication;
import com.will.talentreview.entity.ImageUpload;
import com.will.talentreview.entity.RequestParam;
import com.will.talentreview.entity.RequestResult;
import com.will.talentreview.request.CustomRequestCallback;
import com.will.talentreview.utils.DateUtils;
import com.will.talentreview.utils.FileUtils;
import com.will.talentreview.utils.GlideUtil;
import com.will.talentreview.utils.PermissionUtil;
import com.will.talentreview.utils.PhotoUtil;
import com.will.talentreview.utils.RequestUtil;
import com.will.talentreview.utils.StringUtils;
import com.will.talentreview.utils.UriUtil;
import com.will.talentreview.view.CommonTitle;

import java.util.Date;
import java.util.List;

/**
 * @author chenwei
 * @time 2018-11-29
 * 实名认证
 */

public class AuthenticationActivity extends BaseActivity implements View.OnClickListener, OnSureLisener {

    private final String TAG_SUBMIT = TAG + "_submit";
    private final String TAG_UPLOAD = TAG + "_upload";
    private final String TAG_DETAIL = TAG + "_detail";

    private TextView mtvStatus;
    private EditText metIdCard;
    private EditText metIdName;
    private TextView mtvDate;
    private CardView mcvContainer1;
    private CardView mcvContainer2;
    private ImageView mivPhoto1;
    private ImageView mivPhoto2;
    private TextView mtvSubmit;
    private TextView mtvCancel;
    private RelativeLayout mrlPhoto1;
    private RelativeLayout mrlPhoto2;

    private PhotoUtil photoUtil;
    private int photoType;

    private String photoPath1;
    private String photoPath2;

    private String photoId1;
    private String photoId2;


    @Override
    protected int getContentView() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void initView() {
        CommonTitle commonTitle = new CommonTitle(this, findViewById(R.id.include), CommonTitle.TITLE_TYPE_4);
        commonTitle.setTitleCenter("实名认证");
        commonTitle.setTitleLeftListener();

        mtvStatus = findViewById(R.id.tv_status);
        metIdCard = findViewById(R.id.et_id_card);
        metIdName = findViewById(R.id.et_name);
        mtvDate = findViewById(R.id.tv_effective_date);
        mcvContainer1 = findViewById(R.id.cv_image_container1);
        mcvContainer2 = findViewById(R.id.cv_image_container2);
        mivPhoto1 = findViewById(R.id.iv_card_cover1);
        mivPhoto2 = findViewById(R.id.iv_card_cover2);
        mtvSubmit = findViewById(R.id.tv_submit);
        mrlPhoto1 = findViewById(R.id.rl_card1);
        mrlPhoto2 = findViewById(R.id.rl_card2);
        mtvCancel=findViewById(R.id.tv_cancel);

        mtvSubmit.setOnClickListener(this);
        mtvDate.setOnClickListener(this);
        mrlPhoto1.setOnClickListener(this);
        mrlPhoto2.setOnClickListener(this);
        mtvCancel.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        int status = getIntent().getIntExtra(IntentKey.STATUS, 0);
        switch (status) {
            case AppConstants.AuthenticationStatus.STATUS_0:
                mtvStatus.setText("未认证");
                break;
            case AppConstants.AuthenticationStatus.STATUS_1:
                mtvStatus.setText("待审核");
                mtvSubmit.setVisibility(View.GONE);
                mtvCancel.setVisibility(View.INVISIBLE);
                getDetail();
                break;
            case AppConstants.AuthenticationStatus.STATUS_2:
                mtvSubmit.setVisibility(View.GONE);
                mtvStatus.setText("认证成功");
                mtvStatus.setTextColor(Color.parseColor("#00cc99"));
                mtvCancel.setVisibility(View.INVISIBLE);
                getDetail();
                break;
            case AppConstants.AuthenticationStatus.STATUS_3:
                mtvStatus.setText("认证失败，请重新提交");
                break;
        }
        photoUtil = new PhotoUtil(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_effective_date:
                showDatePicker(this);
                break;
            case R.id.rl_card1:
                photoType = 1;
                photoUtil.addPicture();
                break;
            case R.id.rl_card2:
                photoType = 2;
                photoUtil.addPicture();
                break;
            case R.id.tv_submit:
                if (judgeParams()) {
                    compressImage(true);
                }
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onSure(Date date) {
        mtvDate.setText(DateUtils.getYyyyMMdd(date.getTime()));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtil.OnRequestPermissionsResultCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, List<String> perms, boolean isAllGranted) {
                photoUtil.takePhoto();
            }

            @Override
            public void onPermissionsDenied(int requestCode, List<String> perms, boolean isAllDenied) {
                showShortToast("请允许拍照相关权限");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PhotoUtil.REQUEST_TASK_PHOTO) {
                String path = photoUtil.getPhotoPath();
                if (path == null) {
                    return;
                }
                if (photoType == 1) {
                    photoPath1 = path;
                } else {
                    photoPath2 = path;
                }
                showPhoto(path);
            } else if (requestCode == PhotoUtil.REQUEST_TASK_PICTURE) {
                if (data.getData() == null) {
                    return;
                }
                Uri uri = data.getData();
                String path = UriUtil.getRealPathFromUri(activity, uri);
                if (photoType == 1) {
                    photoPath1 = path;
                } else {
                    photoPath2 = path;
                }
            }
        }
    }

    private void showPhoto(String path) {
        if (photoType == 1) {
            mcvContainer1.setVisibility(View.VISIBLE);
            GlideUtil.showCommonImage(this, path, mivPhoto1);
            mivPhoto1.setTag(path);
        } else {
            mcvContainer2.setVisibility(View.VISIBLE);
            GlideUtil.showCommonImage(this, path, mivPhoto2);
            mivPhoto2.setTag(path);
        }
    }

    private boolean judgeParams() {
        if (TextUtils.isEmpty(metIdCard.getText().toString())) {
            showShortToast("请输入身份证号码");
            return false;
        }
        if (TextUtils.isEmpty(metIdName.getText().toString())) {
            showShortToast("请输入姓名");
            return false;
        }
        if (TextUtils.isEmpty(mtvDate.getText().toString())) {
            showShortToast("请选择有效期");
            return false;
        }
        if (TextUtils.isEmpty(photoPath1)) {
            showShortToast("请上传身份证正面照");
            return false;
        }
        if (TextUtils.isEmpty(photoPath2)) {
            showShortToast("请上传身份证反面照");
            return false;
        }
        return true;
    }

    private void compressImage(final boolean isFirst) {
        showProgressDialog("正在上传图片，请稍后...", true);
        photoUtil.compressImage(isFirst ? photoPath1 : photoPath2, Config.THUMBNAIL_FILE_PATH, new Callback<String>() {
            @Override
            public void callback(String s) {
                if (TextUtils.isEmpty(s)) {
                    closeProgressDialog();
                    showShortToast("上传失败，请重试");
                    return;
                }
                uploadImage(isFirst, s);
            }
        });
    }

    private void uploadImage(final boolean isFirst, String path) {
        RequestUtil.getInstance(this).uploadFile(TAG_UPLOAD, path, new CustomRequestCallback<ImageUpload>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                if (requestResult.isSuccess()) {
                    ImageUpload upload = (ImageUpload) requestResult.getData();
                    if (isFirst) {
                        compressImage(false);
                        photoId1 = upload.getId();
                    } else {
                        closeProgressDialog();
                        photoId2 = upload.getId();
                        submit();
                    }
                } else {
                    closeProgressDialog();
                    showShortToast("上传失败，请重试");
                }
            }
        });
    }

    private void submit() {
        showProgressDialog("正在提交，请稍后...", true, TAG_SUBMIT);
        RequestParam param = new RequestParam();
        param.addParam("cardNo", metIdCard.getText().toString());
        param.addParam("userName", metIdName.getText().toString());
        param.addParam("validity", mtvDate.getText().toString());
        param.addParam("fontPicId", photoId1);
        param.addParam("behindPicId", photoId2);
        RequestUtil.getInstance(this).submitAuthentication(TAG_SUBMIT, param, new CustomRequestCallback<String>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    showShortToast("提交成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "提交失败，请重试"));
                }
            }
        });
    }

    private void getDetail() {
        showProgressDialog("加载中，请稍后...", true, TAG_DETAIL);
        RequestParam param = new RequestParam();
        RequestUtil.getInstance(this).getAuthenticationInfo(TAG_DETAIL, param, new CustomRequestCallback<Authentication>() {
            @Override
            public void onRequestFinished(RequestResult requestResult) {
                closeProgressDialog();
                onTokenInvalid(requestResult.getCode());
                if (requestResult.isSuccess()) {
                    Authentication authentication = (Authentication) requestResult.getData();
                    if (authentication != null) {
                        metIdCard.setText(StringUtils.excludeNull(authentication.getCardNo(), "未知"));
                        metIdCard.setEnabled(false);
                        metIdName.setText(StringUtils.excludeNull(authentication.getUserName(), "未知"));
                        metIdName.setEnabled(false);
                        mtvDate.setText(StringUtils.excludeNull(authentication.getValidity(), "未知"));
                        mtvDate.setEnabled(false);
                        mrlPhoto1.setClickable(false);
                        mrlPhoto2.setClickable(false);
                        mcvContainer1.setVisibility(View.VISIBLE);
                        mcvContainer2.setVisibility(View.VISIBLE);
                        GlideUtil.showCommonImage(activity, authentication.getFontPicUrl(), mivPhoto1);
                        GlideUtil.showCommonImage(activity, authentication.getBehindPicUrl(), mivPhoto2);

                    }
                } else {
                    showShortToast(StringUtils.excludeNull(requestResult.getMsg(), "获取信息失败"));
                }
            }
        });
    }

    @Override
    protected void onProgressDialogCancel(String tag) {
        RxApiManager.get().cancel(tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxApiManager.get().cancel(TAG_SUBMIT);
        RxApiManager.get().cancel(TAG_SUBMIT);
        FileUtils.deleteAll(Config.IMAGE_FILE_PATH);
        FileUtils.deleteAll(Config.THUMBNAIL_FILE_PATH);
    }
}
