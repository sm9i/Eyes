package com.sm9i.eyes.widget.font

import android.text.TextPaint
import android.text.style.MetricAffectingSpan


class PrintSpan(var printAlpha: Int) : MetricAffectingSpan() {
    override fun updateMeasureState(textPaint: TextPaint) {
        textPaint.alpha = printAlpha
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.alpha = printAlpha
    }

}