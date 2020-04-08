package com.sm9i.eyes.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout


/**
 * 底部bar的单独view
 */
class BottomBarItemLayout : LinearLayout {
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

    }
}