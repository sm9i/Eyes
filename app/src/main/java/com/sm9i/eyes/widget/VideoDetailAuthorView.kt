package com.sm9i.eyes.widget

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.AuthorBean
import com.sm9i.eyes.net.Extras
import kotlinx.android.synthetic.main.layout_video_author_head.view.*


/**
 * 视频作者item
 * 头像 名称 简洁  关注
 */
class VideoDetailAuthorView : FrameLayout, View.OnClickListener {

    private lateinit var mTitle: String
    private lateinit var mId: String

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
        LayoutInflater.from(context).inflate(R.layout.layout_video_author_head, this, true)
        setOnClickListener(this)
    }


    override fun onClick(view: View) {
        val bundle = Bundle().apply {
            putString(Extras.TAB_INDEX, "0")
            putString(Extras.TITLE, mTitle)
            putString(Extras.ID, mId)
        }
        ARouter.getInstance().build("/pgc/detail").with(bundle).navigation()
    }

    /**
     * 设置作者信息
     */
    fun setVideoAuthorInfo(author: AuthorBean) {
        iv_image.setImageURI(author.icon)
        tv_title.text = author.name
        tv_desc.text = author.description
        mTitle = author.name
        mId = author.id

    }

}