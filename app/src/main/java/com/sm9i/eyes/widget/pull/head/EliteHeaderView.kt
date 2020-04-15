package com.sm9i.eyes.widget.pull.head

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.sm9i.eyes.R
import com.sm9i.eyes.widget.pull.refresh.PullRefreshView
import kotlinx.android.synthetic.main.refresh_daily_elite_header.view.*
import kotlin.math.abs


/**
 * Author:  andy.xwt
 * Date:    2018/6/25 13:16
 * Description: 每日精选刷新头布局，包括内部眼睛的旋转，和文字的变色
 */

class EliteHeaderView : PullRefreshView {
    private var mRotationAnimator: ValueAnimator? = null

    private var mYDistance = 0f

    companion object {
        private const val ROTATION_DAMP = 2f//阻尼系数
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.refresh_daily_elite_header, this, true)
    }

    /**
     * 处理额外的下拉 dy
     */
    override fun handleExtraPullEvent(dy: Float) {
        //处理眼睛旋转
        iv_head_inner.rotation = iv_head_inner.rotation + (dy.toInt() / ROTATION_DAMP)

    }

    /**
     * 处理有效的下拉 dy
     */
    override fun handleValidPullEvent(dy: Float) {
        //处理文字透明度
        mYDistance += dy
        if (mYDistance >= height - tv_loading_msg.bottom && mYDistance <= height - tv_loading_msg.top) {
            val argbEvaluator = ArgbEvaluator()
            val fraction =
                abs(tv_loading_msg.bottom - height + mYDistance) / (tv_loading_msg.height)
            val textColor = argbEvaluator.evaluate(fraction, 0, 0xff444444.toInt()) as Int
            tv_loading_msg.setTextColor(textColor)
        }
    }


    override fun doRefresh() {
        doInnerEyeAnimator()
    }

    override fun getDoRefreshHeight() = iv_head_outer.height

    /**
     * 执行内部眼睛动画,执行之前，先停止之前的
     */
    private fun doInnerEyeAnimator() {
        reset()
        mRotationAnimator = ValueAnimator.ofFloat(0f, 360f)
        mRotationAnimator?.addUpdateListener { iv_head_inner.rotation += 10 }
        mRotationAnimator?.duration = 200
        mRotationAnimator?.repeatCount = -1
        mRotationAnimator?.start()
    }

    override fun reset() {
        if (mRotationAnimator != null) {
            mRotationAnimator?.cancel()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        reset()
    }
}