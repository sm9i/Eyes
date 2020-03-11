package com.sm9i.eyes.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sm9i.eyes.R
import kotlinx.android.synthetic.main.layout_loading_view.view.*


class NetLoadingView : FrameLayout {
    private lateinit var mRotationAnimator: ObjectAnimator

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_view, this)
        doInnerEyeAnimator()
    }

    /**
     * 执行内部的眼睛旋转动画
     */
    private fun doInnerEyeAnimator() {
        //旋转 360 读书
        mRotationAnimator = ObjectAnimator.ofFloat(iv_head_inner, "rotation", 0f, 360f)
        mRotationAnimator.duration = 1000
        //无限
        mRotationAnimator.repeatCount = -1
        mRotationAnimator.start()
    }

    //view 销毁的时候
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRotationAnimator.cancel()
    }
}