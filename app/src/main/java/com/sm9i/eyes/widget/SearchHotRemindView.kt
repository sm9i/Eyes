package com.sm9i.eyes.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.sm9i.eyes.R
import kotlinx.android.synthetic.main.layout_search_hot_remind_view.view.*


class SearchHotRemindView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_search_hot_remind_view, this)
    }


    /**
     * 设置搜索结果
     */
    fun setSearchResult(queryWord: CharSequence, count: Int) {
        tv_title.visibility = View.GONE
        if (count > 0) {
            tv_result.text = "- [ $queryWord ] 搜索结果共${count}个 -"
            tv_result.visibility = View.VISIBLE
        } else {
            tv_result.visibility = View.GONE
        }
    }
}