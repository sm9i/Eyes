package com.sm9i.eyes.ui.base.view

import android.view.View


/**
 * view 基类
 *
 * 切换布局状态
 */
interface BaseView {
    fun showLoading()
    fun showContent()
    fun showNetError(onClickListener: View.OnClickListener)
    fun showEmpty(onClickListener: View.OnClickListener)
}