package com.sm9i.eyes.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.utils.dip2px
import kotlinx.android.synthetic.main.layout_margin_with_indicator_pager.view.*

/**
 * 带分割的banner
 */
class MarginWithIndicatorViewPager : FrameLayout {

    private lateinit var mItemList: MutableList<Content>
    lateinit var pageViewClickListener: (position: Int) -> Unit


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context)
            .inflate(R.layout.layout_margin_with_indicator_pager, this, true)
    }

    fun setData(itemList: MutableList<Content>) {
        mItemList = itemList
        setAdapter()
    }

    private fun setAdapter() {
        vp_indicator_pager.adapter = MarginWidthViewPagerAdapter()
        vp_indicator_pager.offscreenPageLimit = 3
        vp_indicator_pager.pageMargin = context.dip2px(10f)
        vp_indicator_pager.currentItem = 10000 * mItemList.size
        pageIndicatorView.count = mItemList.size
        vp_indicator_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                pageIndicatorView.setSelected(position % mItemList.size)
            }

            override fun onPageSelected(position: Int) {
            }

        })

    }


    /**
     * 内部类 adapter 无限滚动适配
     */
    inner class MarginWidthViewPagerAdapter : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val cardView =
                CollectionOfHorizontalScrollCardView(this@MarginWithIndicatorViewPager.context)
            val newPosition = position % mItemList.size
            cardView.setOnClickListener { pageViewClickListener(newPosition) }
            cardView.setData(mItemList[newPosition])
            container.addView(cardView)
            return cardView

        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View?)
        }

        override fun getCount() = Int.MAX_VALUE

    }
}