package com.sm9i.eyes.widget.font

import android.animation.ObjectAnimator
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.util.Property
import android.widget.TextView


/**
 * 文本打印
 */
class PrintSpanGroup constructor(printText: CharSequence, var printTime: Long = 0) {

    private var mSpans: MutableList<PrintSpan> = mutableListOf()

    private var spannableString: SpannableString = SpannableString(printText)
    private var mAlpha: Float = 255F

    companion object {
        const val DEFAULT_PRINT_TIME = 500L
    }

    private val TYPE_WRITER_GROUP_ALPHA_PROPERTY = object :
        Property<PrintSpanGroup, Float>(Float::class.java, "type_writer_group_alpha_property") {
        override fun get(printGroup: PrintSpanGroup): Float {
            return mAlpha
        }

        override fun set(printGroup: PrintSpanGroup?, value: Float) {
            setAlpha(value)
        }
    }

    init {
        buildPrintSpanGroup(0, printText.length - 1)
        printTime = if (printTime > 0) printTime else DEFAULT_PRINT_TIME
    }

    /**
     * 将text 拆分成单个span
     */
    private fun buildPrintSpanGroup(start: Int, end: Int) {
        for (i in start..end) {
            val printSpan = PrintSpan(Color.TRANSPARENT)
            spannableString.setSpan(printSpan, i, i + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            mSpans.add(printSpan)
        }

    }

    /**
     * 设置单个span的颜色
     */
    private fun setAlpha(alpha: Float) {
        val size = mSpans.size
        var total = size * 1f * alpha
        for (i in 0 until size) {
            val printSpan = mSpans[i]
            if (i <= total) {
                printSpan.printAlpha = 255
            } else {
                printSpan.printAlpha = 0
            }
        }
    }

    /**
     * 打印
     */
    fun startPrint(textView: TextView) {
        val objectAnimator = ObjectAnimator.ofFloat(this, TYPE_WRITER_GROUP_ALPHA_PROPERTY, 0f, 1f)
        objectAnimator.duration = printTime
        objectAnimator.start()
        objectAnimator.addUpdateListener {
            textView.text = spannableString
        }
    }


}