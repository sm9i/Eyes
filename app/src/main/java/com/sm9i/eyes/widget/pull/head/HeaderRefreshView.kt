package com.sm9i.eyes.widget.pull.head

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.FrameLayout
import com.sm9i.eyes.R
import com.sm9i.eyes.utils.dip2px
import kotlinx.android.synthetic.main.refresh_category_header.view.*


class HeaderRefreshView : FrameLayout {

    private var mRotateAnimation: RotateAnimation? = null

    companion object {
        ///刷新阕值
        private const val REFRESH_THRESHOLD_VALUE = 50f
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
        LayoutInflater.from(context).inflate(R.layout.refresh_category_header, this, true)
        rl_refresh_container.background.alpha = 0
        iv_refresh.imageAlpha = 0
    }

    fun showRefreshCover(scrollValue: Int) {
        if (scrollValue in 1..getRefreshThresholdValue()) {
            val percent = (scrollValue.toFloat() / getRefreshThresholdValue())
            rl_refresh_container.background.alpha = (percent * 255).toInt()
            iv_refresh.scaleX = percent
            iv_refresh.scaleY = percent
        } else {
            rl_refresh_container.background.alpha = 255
            iv_refresh.imageAlpha = 255
            startRefreshAnimation()
        }
    }

    /**
     * 设置结束动画
     */
    fun hideRefreshCover() {
        iv_refresh.clearAnimation()
        mRotateAnimation = null
        ValueAnimator.ofFloat(1f, 0f).apply {
            duration = 500
            addUpdateListener {
                val animatedValue = (it.animatedValue) as Float
                rl_refresh_container.background.alpha = (animatedValue * 255).toInt()
                iv_refresh.imageAlpha = animatedValue.toInt()
            }
        }.start()
    }

    /**
     * 执行刷新动画
     */
    private fun startRefreshAnimation() {
        if (mRotateAnimation == null) {
            mRotateAnimation = RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                ///转动速度为匀速
                interpolator = LinearInterpolator()
                //重复
                repeatCount = -1
                duration = 1000
            }
            iv_refresh.startAnimation(mRotateAnimation)
        }
    }

    /**
     * 获取刷新阕值
     */
    fun getRefreshThresholdValue() = context.dip2px(REFRESH_THRESHOLD_VALUE)
}