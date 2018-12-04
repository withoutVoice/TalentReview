package com.will.talentreview.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.ghnor.flora.Flora;
import com.ghnor.flora.callback.Callback;
import com.ghnor.flora.spec.decoration.Decoration;
import com.will.talentreview.constant.Config;
import com.will.talentreview.view.CustomDialogBottom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片工具类
 *
 * @author yanchenglong
 * @time 2018-06-07
 */

public class PhotoUtil {
    //拍摄照片
    public static final int REQUEST_TASK_PHOTO = 10;
    //选择照片
    public static final int REQUEST_TASK_PICTURE = 11;
    //剪裁照片
    public static final int REQUEST_CROP_PICTURE = 12;

    private Activity activity;
    private String photoPath;
    private String cropPath;

    private CustomDialogBottom dialogBottom;

    public PhotoUtil(Activity activity) {
        this.activity = activity;
    }

    /**
     * 添加照片提示，选择拍摄照片还是从相机选择照片
     */
    public void addPicture() {
        if (dialogBottom == null) {
            List<String> menuItems = new ArrayList<>();
            menuItems.add("拍摄照片");
            menuItems.add("选择照片");
            menuItems.add("取消");
            dialogBottom = new CustomDialogBottom(activity, menuItems);
            dialogBottom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    dialogBottom.dismiss();
                    if (i == 0) {
                        if (PermissionUtil.getCameraPermissions(activity, REQUEST_TASK_PHOTO)) {
                            takePhoto();
                        }
                    } else if (i == 1) {
                        pickPicture();
                    }
                }
            });
        }
        dialogBottom.show();
    }

    /**
     * 拍照
     */
    public void takePhoto() {
        photoPath = Config.IMAGE_FILE_PATH + System.currentTimeMillis() + ".png";
        File file = new File(Config.IMAGE_FILE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.DATA, photoPath);
            Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (uri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            }
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
        }
        activity.startActivityForResult(intent, REQUEST_TASK_PHOTO);
    }

    /**
     * 打开选择图片的界面
     */
    public void pickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        activity.startActivityForResult(intent, REQUEST_TASK_PICTURE);
    }

    /**
     * 获取拍照后图片路径
     *
     * @return 图片路径
     */
    public String getPhotoPath() {
        return photoPath;
    }

    /**
     * 剪裁头像
     *
     * @param orgUri   剪裁图片的原图uri
     */
    public void cropImageForAvatar(Uri orgUri) {
        if (orgUri == null) {
            return;
        }
        File folder = new File(Config.CROP_PIC_PATH);
        if (folder == null) {
            return;
        }
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File cropFile = new File(folder, System.currentTimeMillis() + ".jpg");
        try {
            if(cropFile.exists()){
                cropFile.createNewFile();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        cropPath = cropFile.getAbsolutePath();
        Intent intent = new Intent("com.android.camera.action.CROP");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues values = new ContentValues(1);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.DATA, cropFile.getAbsolutePath());
            Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            if (uri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoPath)));
        }
        intent.setDataAndType(orgUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, REQUEST_CROP_PICTURE);
    }

    public String getCropImagePath() {
        return cropPath;
    }

    /**
     * 压缩图片
     *
     * @param path     原图片路径
     * @param savePath 压缩后图片路径
     * @param callback 压缩回调
     */
    public void compressImage(final String path, String savePath, Callback<String> callback) {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {
            saveFile.mkdir();
        }
        final File file = new File(path);

        Flora.with(activity).addDecoration(new Decoration() {
            @Override
            public Bitmap onDraw(Bitmap bitmap) {
                return rotateImage(path, bitmap);
            }
        }).diskDirectory(new File(savePath)).load(file).compress(callback);
    }

    private Bitmap rotateImage(String photoPath, Bitmap bitmap) {
        //根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoPath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        int degree = 0;
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
            if (degree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(degree);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), m, true);
            }
        }
        return bitmap;
    }

    public String saveImageByCamera(Bitmap bitmap) {
        File folder = new File(Config.IMAGE_FILE_PATH);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(folder, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            file.delete();
            return "";
        } catch (IOException e) {
            file.delete();
            return "";
        }
        return file.getAbsolutePath();
    }


    /**
     * 将bitmap保存在本地
     *
     * @param filePath 文件路径
     * @param bitmap   bitmap
     * @return 保存结果
     */
    public boolean saveImageByBitmap(String fileName, String filePath, Bitmap bitmap) {
        File file = new File(filePath, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            file.delete();
            return false;
        } catch (IOException e) {
            file.delete();
            return false;
        }
        return true;
    }

}
