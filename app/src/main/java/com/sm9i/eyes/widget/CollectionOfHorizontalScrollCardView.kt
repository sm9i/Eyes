package com.sm9i.eyes.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.utils.getElapseTimeForShow
import kotlinx.android.synthetic.main.layout_collection_of_horizontal_scroll_card.view.*


/**
 * 分类里 viewpage 的child
 * 图片
 * 文字
 */
class CollectionOfHorizontalScrollCardView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context)
            .inflate(R.layout.layout_collection_of_horizontal_scroll_card, this, true)
    }

    /**
     * 设置数据
     */
    fun setData(content: Content) {
        scroll_elite_view.setImageUrl(content.data.cover.feed)
        val description =
            "#${content.data.category}   /   ${getElapseTimeForShow(content.data.duration)}"
        scroll_tv_title.text = content.data.title
        scroll_tv_desc.text = description
        scroll_elite_view.setDailyVisible(content.data.library == "DAILY")
    }


}