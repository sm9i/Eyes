package com.sm9i.eyes.widget.image.imageloader

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.facebook.drawee.view.SimpleDraweeView
import com.youth.banner.loader.ImageLoader

/**
 * banner 图片加载器
 */

class FrescoImageLoader : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        imageView?.setImageURI(Uri.parse(path as String))
    }

    override fun createImageView(context: Context?): ImageView {
        return SimpleDraweeView(context)
    }


}