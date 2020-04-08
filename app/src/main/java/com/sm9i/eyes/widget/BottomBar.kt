package com.sm9i.eyes.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout


/**
 * 主页自定义底部导航栏
 *
 */
class BottomBar : LinearLayout {

    private lateinit var mTabLayout: LinearLayout
    private lateinit var mTabParams: LayoutParams

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
        //设置主轴放心为水平方向
        orientation = HORIZONTAL

        mTabLayout = LinearLayout(context)
        mTabLayout.setBackgroundColor(Color.WHITE)
        mTabLayout.orientation = HORIZONTAL
        mTabParams = LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        mTabParams.weight = 1f
        addView(mTabLayout, mTabParams)
    }


    /**
     * 添加一个底部bar
     */
    fun addItem(bottomBarItem: BottomBarItem) {


    }


}