package com.sm9i.eyes.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ProgressBar


/**
 * 竖着的progressbar
 */
class VerticalProgressBar : ProgressBar {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas) {
        //选咋混90d 竖起来
        canvas.rotate(-90f)
        //将经过旋转后得到的VerticalProgressBar移到正确的位置,注意经旋转后宽高值互换??
        canvas.translate(-height.toFloat(), 0f)
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //互换宽高
        setMeasuredDimension(heightMeasureSpec, widthMeasureSpec)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(h, w, oldw, oldh)
    }
}