package com.sm9i.eyes.widget

import android.content.Context
import android.graphics.Color
import android.os.Parcel
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
    //当前选中的
    private var mCurrentPosition = 0
    //tab集合
    private var mBottomItemLayouts = mutableListOf<BottomBarItemLayout>()

    //选项卡的监听
    private var mListener: TabSelectedListener? = null


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
     * 并且设置点击事件
     */
    fun addItem(bottomBarItem: BottomBarItem) {
        val bottomItemLayout = BottomBarItemLayout(context)
        bottomItemLayout.setTabPosition(mBottomItemLayouts.size)
        bottomItemLayout.setBottomItem(bottomBarItem)
        //添加到布局
        mTabLayout.addView(bottomItemLayout, mTabParams)
        mBottomItemLayouts.add(bottomItemLayout)

        bottomItemLayout.setOnClickListener {
            val position = bottomItemLayout.getTabPosition()
            if (position == mCurrentPosition) {
                mListener?.onTabReselected(position)
            } else {
                mListener?.onTabSelected(position, mCurrentPosition)
                bottomItemLayout.isSelected = true
                mListener?.onTabUnSelected(mCurrentPosition)
                mBottomItemLayouts[mCurrentPosition].isSelected = false
                mCurrentPosition = position
            }

        }

    }

    /**
     * 选项卡监听
     */
    interface TabSelectedListener {
        //选项卡被选择
        fun onTabSelected(position: Int, prePosition: Int)

        //没被选择
        fun onTabUnSelected(position: Int)

        //重复点击
        fun onTabReselected(position: Int)

    }

    fun setOnTabSelectedListener(tabSelectedListener: TabSelectedListener) {
        mListener = tabSelectedListener
    }

    ///状态保存的 SacedState

}