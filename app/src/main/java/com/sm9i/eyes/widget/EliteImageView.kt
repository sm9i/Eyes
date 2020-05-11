package com.sm9i.eyes.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.sm9i.eyes.R
import kotlinx.android.synthetic.main.layout_choiceness.view.*


class EliteImageView : FrameLayout {
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
        LayoutInflater.from(context).inflate(R.layout.layout_choiceness, this, true)
    }

    /**
     * 设置箭头可见
     */
    fun setArrowVisible(visible: Boolean) {
        iv_arrow.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 设置精选是否可见
     */
    fun setDailyVisible(visible: Boolean) {
        iv_daily.visibility = if (visible) View.VISIBLE else View.GONE
    }

    /**
     * 设置翻译
     */
    fun setTranslateText(text: String) {
        if (!TextUtils.isEmpty(text)) {
            tv_translate.visibility = View.VISIBLE
            tv_translate.text = text
        } else {
            tv_translate.visibility = View.GONE
        }
    }

    fun setImageUrl(url: String) {
        iv_elite_image.setImageURI(url)
    }

}