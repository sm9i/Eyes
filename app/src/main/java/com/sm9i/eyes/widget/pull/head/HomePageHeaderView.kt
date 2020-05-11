package com.sm9i.eyes.widget.pull.head

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.entiy.TopIssue
import com.sm9i.eyes.ui.base.BaseFragment
import com.sm9i.eyes.ui.home.DailyEliteActivity
import com.sm9i.eyes.ui.search.SearchHotActivity
import com.sm9i.eyes.utils.readyGo
import com.sm9i.eyes.widget.image.imageloader.FrescoImageLoader
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.layout_category_head_view.view.*


class HomePageHeaderView : FrameLayout {


    private var mScrollValue = 0
    private var currentPosition = -1
    private lateinit var mTopIssue: TopIssue
    private lateinit var mBaseFragment: BaseFragment<*, *>

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        LayoutInflater.from(context).inflate(R.layout.layout_category_head_view, this, true)
        init()
    }

    private fun init() {
        head_banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (currentPosition != position) {
                    tv_title.printText(mTopIssue.data.itemList[position].data.title)
                    tv_title.printText(mTopIssue.data.itemList[position].data.slogan)
                    currentPosition = position
                }

            }

        })
        iv_search.setOnClickListener {
            mBaseFragment.readyGo<SearchHotActivity>()
        }
        rl_more_container.setOnClickListener {
            mBaseFragment.readyGo<DailyEliteActivity>()
        }
    }

    /**
     * 设置头部信息
     */
    fun setHeaderInfo(
        topIssue: TopIssue, videoListInfo: MutableList<Content>, baseFragment: BaseFragment<*, *>
    ) {
        mTopIssue = topIssue
        mBaseFragment = baseFragment
        head_banner.apply {
            setImageLoader(FrescoImageLoader())
            setImages(getTopIssueCardUrl(topIssue.data.itemList))
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            setIndicatorGravity(BannerConfig.CENTER)
            isAutoPlay(true)
            start()
            setDelayTime(6000)
            setOnBannerListener {
                val ite = mTopIssue.data.itemList[it]
                ///跳转到视频页面
            }
        }

    }

    /**
     * 显示遮罩
     */
    fun showRefreshCover(scrollValue: Int) {
        mScrollValue = scrollValue
        head_refresh.showRefreshCover(scrollValue)
        head_banner.stopAutoPlay()
    }

    /**
     * 关闭遮罩
     */
    fun hideRefreshCover() {
        head_refresh.hideRefreshCover()
        head_banner.startAutoPlay()
    }

    /**
     * 获取顶部图片地址
     */
    private fun getTopIssueCardUrl(itemList: MutableList<Content>) =
        itemList.map { it.data.cover.feed }

    /**
     * 判断是否到刷新阕值
     */
    fun judgeCanRefresh() = mScrollValue >= head_refresh.getRefreshThresholdValue()
}