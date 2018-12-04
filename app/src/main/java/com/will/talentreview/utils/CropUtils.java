package com.will.talentreview.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.will.talentreview.constant.Config;

import java.io.File;
import java.io.IOException;

/**
 * @author chenwei
 * @time 2018-11-30
 * 剪裁工具类
 */

public class CropUtils {

    private static File getFile() {
        File folder = new File(Config.CROP_PIC_PATH);
        if (folder == null || !folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, System.currentTimeMillis() + ".jpg");
        if(file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 调用系统照片的裁剪功能
     */
    public static Intent invokeSystemCrop(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getFile()));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        return intent;
    }
}
