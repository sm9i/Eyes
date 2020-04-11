package com.sm9i.eyes.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.sm9i.eyes.R
import kotlinx.android.synthetic.main.layout_bottom_item.view.*


/**
 * 底部bar的单独view
 */
class BottomBarItemLayout : LinearLayout {

    ///设置选中和未选中的文字颜色
    private val mSelectedTextColor = resources.getColor(R.color.colorPrimaryDark)
    private val mUnSelectedTextColor = resources.getColor(R.color.SecondaryText)

    //设置选中和未选中的图片
    private var mSelectedDrawable: Drawable? = null
    private var mUnSelectedDrawable: Drawable? = null
    //文字
    private var mTitle: String? = null
    //当前tab的position
    private var mTabPosition = -1


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
        //设置布局
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_item, this, true)
        gravity = Gravity.CENTER
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        //设置view 选中时候的图片的文字颜色
        if (selected) {
            if (mSelectedDrawable != null) {
                iv_image.setImageDrawable(mSelectedDrawable)
                tv_title.setTextColor(mSelectedTextColor)
            }
        } else {
            if (mUnSelectedDrawable != null) {
                iv_image.setImageDrawable(mUnSelectedDrawable)
                tv_title.setTextColor(mUnSelectedTextColor)
            }
        }
    }

    /**
     *  设置当前tab的position
     */
    fun setTabPosition(position: Int) {
        mTabPosition = position
        if (position == 0) {
            isSelected = true
        }
    }

    /**
     * 获取当前tab的position
     */
    fun getTabPosition() = mTabPosition


    /**
     * 设置一个bottomItem
     *
     * 如果 res >-1 表示存在
     * 不存在就 drawable
     */
    fun setBottomItem(bottomBarItem: BottomBarItem) {
        //设置选中的图片
        if (bottomBarItem.mSelectResource > -1) {
            mSelectedDrawable = resources.getDrawable(bottomBarItem.mSelectResource)
        } else if (bottomBarItem.mSelectDrawable != null) {
            mSelectedDrawable = bottomBarItem.mSelectDrawable!!
        }
        //设置未选中的图片
        if (bottomBarItem.mUnSelectedResource > -1) {
            mUnSelectedDrawable = resources.getDrawable(bottomBarItem.mUnSelectedResource)
        } else if (bottomBarItem.mUnSelectedDrawable != null) {
            mUnSelectedDrawable = bottomBarItem.mUnSelectedDrawable!!
        }
        //设置标题
        if (bottomBarItem.mTitleResource > -1) {
            mTitle = resources.getString(bottomBarItem.mTitleResource)
        } else if (bottomBarItem.mTitle != null) {
            mTitle = bottomBarItem.mTitle!!
        }
        //赋值
        iv_image.setImageDrawable(mUnSelectedDrawable)
        tv_title.text = mTitle
    }
}