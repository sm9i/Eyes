package com.sm9i.eyes.widget.pull.head

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.sm9i.eyes.R
import com.sm9i.eyes.UserPreferences
import com.sm9i.eyes.utils.getElapseTimeForShow
import kotlinx.android.synthetic.main.layout_video_detail_head.view.*


/**
 * 视频底部的详情
 */
class VideoDetailHeadView : FrameLayout, View.OnClickListener {

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
        LayoutInflater.from(context).inflate(R.layout.layout_video_detail_head, this, true)
        tv_favorite.setOnClickListener(this)
        tv_share.setOnClickListener(this)
        tv_reply.setOnClickListener(this)
        tv_download.setOnClickListener(this)
    }

    /**
     * 从上到下的动画
     */
    fun startScrollAnimation() {
        val widthSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        val heightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        measure(widthSpec, heightSpec)
        ObjectAnimator.ofFloat(this, "translationY", -measuredHeight.toFloat(), 0f).apply {
            duration = 300
            start()
        }
    }

    override fun onClick(view: View) {
        val userIsLogin = UserPreferences.getUserIsLogin()
        if (!userIsLogin)
            return
        else {
            when (view.id) {
                R.id.tv_favorite -> addFavorite()
                R.id.tv_share -> showShare()
                R.id.tv_reply -> addReply()
                R.id.tv_download -> download()
            }
        }
    }

    private fun addFavorite() {}
    private fun showShare() {}
    private fun addReply() {}
    private fun download() {}

    ///设置布局
    fun setFavoriteCount(count: String) {
        tv_favorite.text = count
    }

    fun setShareCount(count: String) {
        tv_share.text = count
    }

    fun setReplayCount(count: String) {
        tv_reply.text = count
    }

    fun setTitle(title: String) {
        tv_title.printText(title)
    }

    fun setDescription(description: String) {
        tv_desc.printText(description)
    }

    fun setCategoryAndTime(category: String, duration: Int) {
        tv_time.text = "#$category  /   ${getElapseTimeForShow(duration)}"
    }

}