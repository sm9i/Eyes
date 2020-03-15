package com.sm9i.eyes.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView
import com.sm9i.eyes.player.MeasureHelper

/**
 * 全屏视频
 */
class FullScreenVideoView : VideoView {

    private var mMeasureHelper: MeasureHelper = MeasureHelper(this)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mMeasureHelper.measuredWidth, mMeasureHelper.measuredHeight)
    }

    fun setAspectRatio(aspectRatio: Int) {
        mMeasureHelper.setAspectRatio(aspectRatio)
        requestLayout()
    }


}