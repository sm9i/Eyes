package com.sm9i.eyes.widget

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.drawee.generic.RoundingParams
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.ContentBean
import com.sm9i.eyes.entiy.Header
import com.sm9i.eyes.utils.getTimeStr
import com.sm9i.eyes.widget.font.FontType
import kotlinx.android.synthetic.main.layout_common_text.view.*
import java.util.*


class ItemHeaderView : FrameLayout {
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
        LayoutInflater.from(context).inflate(R.layout.layout_common_text, this, true)
    }


    fun setHeader(header: Header, type: String?) {
        with(header) {
            //标题
            title?.let {
                if (type == "videoCollectionOfHorizontalScrollCard") {
                    tv_title.gravity = Gravity.CENTER
                    iv_source.visibility = View.GONE
                    iv_more.visibility = View.VISIBLE
                } else {
                    tv_title.gravity = getGravity(header.textAlign)
                }
                tv_title.visibility = View.VISIBLE
                tv_title.text = it
                tv_title.setFontType(font)
            }
            //副标题
            subTitle?.let {
                if (type == "videoCollectionOfHorizontalScrollCard") {
                    tv_sub_title.gravity = Gravity.CENTER
                    iv_source.visibility = View.GONE
                } else {
                    tv_sub_title.gravity = getGravity(header.textAlign)
                }
                tv_sub_title.visibility = View.VISIBLE
                tv_sub_title.text = it
                tv_sub_title.setFontType(subTitleFont)
            }
            //设置图片
            icon?.let {
                with(iv_source) {
                    visibility = View.VISIBLE
                    when (iconType) {
                        //设置圆形
                        "round" -> hierarchy.roundingParams = RoundingParams.asCircle()
                        //正方形带
                        "squareWithPlayButton" -> hierarchy.setOverlayImage(context.getDrawable(R.drawable.icon_cover_play_button))
                        else -> hierarchy.roundingParams?.roundAsCircle = false
                    }
                    setImageURI(it)
                }
            }
            //发布时间
            time?.let {
                tv_date.visibility = View.VISIBLE
                tv_date.text = getTimeStr(Date(time!!))
            }
            //关注
            follow?.let {
                tv_focus.visibility = View.VISIBLE
            }
            //描述
            description?.let {
                tv_desc.visibility = View.VISIBLE
                tv_desc.text = it
            }
            //跳转页面
            actionUrl?.let {
                setOnClickListener {
                    ARouter.getInstance().build(Uri.parse(header.actionUrl)).navigation()
                }
            }


        }
    }

    fun setAuthorHeader(content: ContentBean) {
        with(content) {
            //标题
            title?.let {
                tv_title.visibility = View.VISIBLE
                tv_title.text = it
                tv_title.setFontType(FontType.BOLD)
            }
            //图片
            icon?.let {
                with(iv_source) {
                    visibility = View.VISIBLE
                    when (iconType) {
                        //圆形图片
                        "round" -> iv_source.hierarchy.roundingParams = RoundingParams.asCircle()
                        ////正方形音乐带播放按钮类型
                        "squareWithPlayButton" -> hierarchy.setOverlayImage(context.getDrawable(R.drawable.icon_cover_play_button))
                        else -> hierarchy.roundingParams?.roundAsCircle = false
                    }
                    setImageURI(icon)
                }
            }
            follow?.let {
                tv_focus.visibility = View.VISIBLE
            }
            description?.let {
                tv_desc.visibility = View.VISIBLE
                tv_desc.text = it
            }
            actionUrl?.let {
                setOnClickListener {
                    ARouter.getInstance().build(Uri.parse(content.actionUrl)).navigation()
                }
            }
        }
    }

    private fun getGravity(textAlign: String?): Int {
        if (textAlign != null) {
            when (textAlign) {
                "right" -> return Gravity.END
                "left" -> return Gravity.START
                "middle" -> return Gravity.CENTER
            }
        }
        return Gravity.START
    }

}