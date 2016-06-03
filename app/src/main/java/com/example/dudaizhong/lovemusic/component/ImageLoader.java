package com.example.dudaizhong.lovemusic.component;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Dudaizhong on 2016/5/27.
 * Glide图片加载库
 */
public class ImageLoader {

    //加载图片
    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context)
                .load(imageRes)//可以加载多个参数，如网络URL的String参数,resource/file/gifUrl/视频
                .crossFade()//动画淡入淡出的效果
                .into(view);//设置目标View,加载上去
    }

    //记载图片并加入到缓存中
    public static void loadAndDiskCache(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context)
                .load(imageRes)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)//加入到磁盘缓存
                .into(view);
    }

    //清理缓存
    public static void clear(){

    }
}
