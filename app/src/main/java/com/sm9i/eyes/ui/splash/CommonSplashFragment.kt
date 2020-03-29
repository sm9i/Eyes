package com.sm9i.eyes.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.animation.doOnEnd
import com.sm9i.eyes.R
import com.sm9i.eyes.UserPreferences
import com.sm9i.eyes.ui.MainActivity
import com.sm9i.eyes.ui.base.BaseAppCompatFragment
import com.sm9i.eyes.utils.dip2px
import com.sm9i.eyes.utils.getDateString
import com.sm9i.eyes.utils.readyGoThenKillSelf
import kotlinx.android.synthetic.main.fragment_common_landing.*
import java.util.*


/**
 * 文字splash
 * 背景缩放动画
 * 图标上升 变色 旋转
 */
class CommonSplashFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): CommonSplashFragment = CommonSplashFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (!UserPreferences.getShowUserAnim()) {
            doUpAnimator()
            doBackgroundAnimator()
        } else {
            doScaleAnimator()
        }

        super.initView(savedInstanceState)
    }


    /**
     * 执行上升动画
     */
    private fun doUpAnimator() {
        val moveY = _mActivity.dip2px(100f)
        ObjectAnimator.ofFloat(ll_move_container, "translationY", 0f, -moveY.toFloat()).apply {
            //设置动画监听
            addUpdateListener {
                if (it.currentPlayTime in 600..1500) {
                    iv_head_outer.setImageResource(R.drawable.ic_eye_white_outer)
                    iv_head_inner.setImageResource(R.drawable.ic_eye_white_inner)
                    tv_name.setTextColor(resources.getColor(R.color.gray_B7B9B8))
                } else if (it.currentPlayTime in 1500..2000) {
                    iv_head_outer.setImageResource(R.drawable.ic_eye_black_outer)
                    iv_head_inner.setImageResource(R.drawable.ic_eye_black_inner)
                    tv_name.setTextColor(resources.getColor(R.color.black_444444))
                }
            }
            duration = 2000
        }.start()
    }

    /**
     * 在上升动画的同时 执行背景变白色 和字体变色
     */
    private fun doBackgroundAnimator() {
        ValueAnimator.ofArgb(0, 0xffffffff.toInt()).apply {
            addUpdateListener {
                rl_loading_container.setBackgroundColor(it.animatedValue as Int)
            }
            doOnEnd { doTextAnimator() }
            duration = 2000
        }.start()
    }

    private fun doTextAnimator() {
        ValueAnimator.ofArgb(0, 0xff444444.toInt()).apply {
            addUpdateListener {
                val color = it.animatedValue as Int
                setTextColor(tv_today, color)
                setTextColor(tv_date, color)
                setTextColor(tv_today_chose, color)
                tv_date.text = getDateString(Date(), "- yyyy/MM/dd -")
            }
            doOnEnd {
                doInnerEyeAnimator()
            }
        }.start()

    }


    /**
     * 内部眼睛旋转动画
     */
    private fun doInnerEyeAnimator() {
        //360 旋转
        ObjectAnimator.ofFloat(iv_head_inner, "rotation", 0f, 360f).apply {

            doOnEnd {
                readyGoThenKillSelf<MainActivity>()
                UserPreferences.saveShowUserAnim(true)
            }
            duration = 1000
        }.start()
    }

    /**
     * 在动画执行过程中设置字体颜色
     */
    private fun setTextColor(textView: TextView, color: Int) {
        textView.visibility = View.VISIBLE
        textView.setTextColor(color)
    }

    /**
     * 执行背景缩放 执行完后跳转到 Main 2000ms
     */
    private fun doScaleAnimator() {
        AnimatorSet().apply {
            val scaleX = ObjectAnimator.ofFloat(iv_background, "scaleX", 1f, 1.08f)
            val scaleY = ObjectAnimator.ofFloat(iv_background, "scaleY", 1f, 1.08f)
            //两个动画同时执行
            playTogether(scaleX, scaleY)
            doOnEnd {
                readyGoThenKillSelf<MainActivity>()
                UserPreferences.saveShowUserAnim(true)
            }
            duration = 2000
        }.start()
    }


    override fun getContentViewLayoutId(): Int = R.layout.fragment_common_landing

}