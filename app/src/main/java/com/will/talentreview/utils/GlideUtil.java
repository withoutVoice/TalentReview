package com.will.talentreview.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.will.talentreview.R;

/**
 * @author chenwei
 * @time 2018-11-20
 */

public class GlideUtil {

    public static void showCommonImage(Context context, String url, ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context).load(url).centerCrop().error(R.mipmap.default_pic).into(imageView);
    }

//    public static void showAvatar(Context context, String url, ImageView imageView) {
//        if (context == null || imageView == null) {
//            return;
//        }
//        Glide.with(context).load(url).centerCrop().into(imageView);
//    }

    public static void showAvatar(Context context, String url, final ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }
        Glide.with(context).load(url).centerCrop().into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                if (resource == null) {
                    imageView.setImageResource(R.mipmap.img_default_avatar);
                } else {
                    imageView.setImageDrawable(resource);
                }
            }
        });
    }

}
