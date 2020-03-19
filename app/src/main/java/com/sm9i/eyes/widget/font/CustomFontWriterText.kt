package com.sm9i.eyes.widget.font

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView


class CustomFontWriterText : CustomFontTextView {

    private lateinit var mSloganSpanGroup: PrintSpanGroup

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    /**
     * 打印文字
     */
    fun printText(text: String?, printTime: Long = 0) {
        if (!TextUtils.isEmpty(text)) {
            mSloganSpanGroup = PrintSpanGroup(text!!, printTime)
            mSloganSpanGroup.startPrint(this)
        }
    }

}