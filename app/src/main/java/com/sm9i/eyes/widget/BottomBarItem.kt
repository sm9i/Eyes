package com.sm9i.eyes.widget

import android.graphics.drawable.Drawable

/**
 * 底部bar的实体类
 */
class BottomBarItem {


    var mSelectDrawable: Drawable? = null
    var mSelectResource = -1

    var mTitleResource = -1
    var mTitle: String? = null

    var mUnSelectedDrawable: Drawable? = null
    var mUnSelectedResource = -1

    constructor(selectDrawable: Drawable, title: String) {
        this.mSelectDrawable = selectDrawable
        this.mTitle = title
    }

    constructor(selectResource: Int, title: String) {
        this.mSelectResource = selectResource
        this.mTitle = title
    }

    constructor(mSelectDrawable: Drawable, mTitleResource: Int) {
        this.mSelectDrawable = mSelectDrawable
        this.mTitleResource = mTitleResource
    }

    constructor(mSelectResource: Int, mTitleResource: Int) {
        this.mSelectResource = mSelectResource
        this.mTitleResource = mTitleResource
    }

    fun setUnSelectedDrawable(unSelectedDrawable: Drawable) {
        this.mUnSelectedDrawable = unSelectedDrawable
    }

    fun setUnSelectedDrawable(unSelectedResource: Int) {
        this.mUnSelectedResource = unSelectedResource
    }

}