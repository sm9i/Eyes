package com.sm9i.eyes.player.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.sm9i.eyes.R
import com.sm9i.eyes.player.IjkMediaController


/**
 * 视频播放的错误页面
 * 点击后 回调 并且 旋转image
 */
class ErrorView(context: Context) : FrameLayout(context), View.OnClickListener {

    private val mIvReload: ImageView
    private lateinit var mController: IjkMediaController

    fun setController(controller: IjkMediaController) {
        this.mController = controller
    }


    init {
        LayoutInflater.from(context).inflate(R.layout.layout_video_error, this, true)
        mIvReload = findViewById(R.id.iv_reload)
        setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        //旋转360度
        val rotation = ObjectAnimator.ofFloat(mIvReload, "rotation", 0f, 360f)
        rotation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                mController.hide()
                mController.controllerListener?.onErrorViewClick()
            }
        })
        rotation.duration = 500
        rotation.start()
    }


}
