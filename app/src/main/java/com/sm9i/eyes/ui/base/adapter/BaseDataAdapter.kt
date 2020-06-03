package com.sm9i.eyes.ui.base.adapter

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Gravity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.ui.video.VideoDetailActivity
import com.sm9i.eyes.widget.EliteImageView
import com.sm9i.eyes.widget.ItemHeaderView
import com.sm9i.eyes.widget.font.CustomFontTextView
import com.sm9i.eyes.widget.font.FontType
import com.sm9i.eyes.widget.image.imageloader.FrescoImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import java.util.*


open class BaseDataAdapter(data: MutableList<Content>) :
    BaseQuickAdapter<Content, BaseViewHolder>(data) {


    companion object {
        const val BANNER_TYPE = 0
        const val FOLLOW_CARD_TYPE = 1
        const val HORIZONTAL_SCROLL_CARD_TYPE = 2
        const val VIDEO_COLLECTION_WITH_COVER_TYPE = 3
        const val SQUARE_CARD_COLLECTION_TYPE = 4
        const val VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD_TYPE = 5
        const val VIDEO_COLLECTION_WITH_BRIEF_TYPE = 6
        const val TEXT_CARD_TYPE = 7
        const val BRIEF_CARD_TYPE = 8
        const val BLANK_CARD_TYPE = 9
        const val SQUARE_CARD_TYPE = 10
        const val RECTANGLE_CARD_TYPE = 11
        const val VIDEO_TYPE = 12
        const val VIDEO_BANNER_THREE_TYPE = 13
        const val VIDEO_SMALL_CARD_TYPE = 14

        const val BANNER = "banner"
        const val FOLLOW_CARD = "followCard"
        const val HORIZONTAL_CARD = "horizontalScrollCard"
        const val VIDEO_COLLECTION_WITH_COVER = "videoCollectionWithCover"
        const val SQUARE_CARD_COLLECTION = "squareCardCollection"
        const val VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD =
            "videoCollectionOfHorizontalScrollCard"
        const val VIDEO_COLLECTION_WITH_BRIEF = "videoCollectionWithBrief"
        const val TEXT_CARD = "textCard"
        const val BRIEF_CARD = "briefCard"
        const val BLANK_CARD = "blankCard"
        const val SQUARE_CARD = "squareCard"
        const val RECTANGLE_CARD = "rectangleCard"
        const val VIDEO = "video"
        const val VIDEO_BANNER_THREE = "banner3"
        const val VIDEO_SMALL_CARD = "videoSmallCard"
    }

    init {

        val hasMap: Set<String> = data.map { it.type }.toSet()
        Log.i("BaseDataAdapter", "当前列表所需type$hasMap")

        //设置布局类型
        multiTypeDelegate = object : MultiTypeDelegate<Content>() {
            override fun getItemType(t: Content?): Int {
                when (t?.type) {
                    BANNER -> return BANNER_TYPE
                    FOLLOW_CARD -> return FOLLOW_CARD_TYPE
                    HORIZONTAL_CARD -> return HORIZONTAL_SCROLL_CARD_TYPE
                    VIDEO_COLLECTION_WITH_COVER -> return VIDEO_COLLECTION_WITH_COVER_TYPE
                    SQUARE_CARD_COLLECTION -> return SQUARE_CARD_COLLECTION_TYPE
                    VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD -> return VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD_TYPE
                    VIDEO_COLLECTION_WITH_BRIEF -> return VIDEO_COLLECTION_WITH_BRIEF_TYPE
                    TEXT_CARD -> return TEXT_CARD_TYPE
                    BRIEF_CARD -> return BRIEF_CARD_TYPE
                    BLANK_CARD -> return BLANK_CARD_TYPE
                    SQUARE_CARD -> return SQUARE_CARD_TYPE
                    RECTANGLE_CARD -> return RECTANGLE_CARD_TYPE
                    VIDEO -> return VIDEO_TYPE
                    VIDEO_BANNER_THREE -> return VIDEO_BANNER_THREE_TYPE
                    VIDEO_SMALL_CARD -> return VIDEO_SMALL_CARD_TYPE
                }
                return FOLLOW_CARD_TYPE
            }
        }

        with(multiTypeDelegate) {
            //首页list 布局
            registerItemType(FOLLOW_CARD_TYPE, R.layout.layout_follow_card)
            //banner图
            registerItemType(HORIZONTAL_SCROLL_CARD_TYPE, R.layout.layout_horizontal_scroll_card)
            //文字 ，标题
            registerItemType(TEXT_CARD_TYPE, R.layout.layout_single_text)
            //横向的list  全部排行
            registerItemType(SQUARE_CARD_COLLECTION_TYPE, R.layout.layout_square_collection)

            registerItemType(VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD_TYPE, R.layout.item_collection_of_horizontal_scroll_card)
        }
    }


    override fun convert(helper: BaseViewHolder?, item: Content) {
        when (helper?.itemViewType) {
            FOLLOW_CARD_TYPE -> setFollowCardInfo(helper, item)
            HORIZONTAL_SCROLL_CARD_TYPE -> setHorizonTalScrollCrdInfo(helper, item)
            TEXT_CARD_TYPE -> setTextCardInfo(helper, item)
            SQUARE_CARD_COLLECTION_TYPE -> setSquareCardInfo(helper, item)

        }
    }

    /**
     * 矩形卡片信息
     */
    private fun setSquareCardInfo(helper: BaseViewHolder, item: Content) {
        val itemList: MutableList<Content> = item.data.itemList
        with(helper) {
            getView<RecyclerView>(R.id.rv_square_recycler).apply {
                //取消单独滑动效果  意思就是随父布局滑动
                isNestedScrollingEnabled = false
                layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = SquareCollectionAdapter(itemList).apply {
                    onItemClickListener = OnItemClickListener { _, _, position ->
                        ARouter.getInstance()
                            .build(Uri.parse(itemList[position].data.actionUrl))
                            .navigation()
                    }
                }
            }
            item.data.header?.let {
                getView<ItemHeaderView>(R.id.item_header_view).setHeader(it, item.type)
            }
        }
    }

    /**
     * 设置文字信息
     */
    private fun setTextCardInfo(helper: BaseViewHolder, item: Content) {
        helper.getView<CustomFontTextView>(R.id.tv_text).apply {
            text = item.data.text
            when (item.data.type) {
                "header1" -> {
                    setFontType(item.data.header?.font, FontType.LOBSTER)
                    gravity = Gravity.CENTER
                }
                "header2" -> {
                    setFontType(item.data.header?.font, FontType.BOLD)
                    gravity = Gravity.CENTER
                }
                else -> {
                    gravity = Gravity.START
                    setFontType(item.data.header?.font, FontType.NORMAL)
                }
            }
        }
    }

    /**
     * 水平滚动卡片
     */
    private fun setHorizonTalScrollCrdInfo(helper: BaseViewHolder, item: Content) {
        helper.getView<Banner>(R.id.banner).apply {
            setImageLoader(FrescoImageLoader())
            setImages(getHorizonTalCardUrl(item.data.itemList))
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
            setIndicatorGravity(BannerConfig.CENTER)
            isAutoPlay(true)
            start()
            setDelayTime(5000)
            setOnBannerListener {
                //跳转到页面？？
                mContext.startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(item.data.itemList[it].data.actionUrl)
                    addCategory(Intent.CATEGORY_DEFAULT)
                    addCategory(Intent.CATEGORY_BROWSABLE)
                })
            }
        }
    }

    /**
     * 获取水平卡片图片地址
     */
    private fun getHorizonTalCardUrl(itemList: MutableList<Content>) =
        itemList.map { it.data.image }


    /**
     * 首页布局
     */
    private fun setFollowCardInfo(helper: BaseViewHolder, item: Content) {
        with(helper) {
            val info = item.data
            info.header?.let {
                getView<ItemHeaderView>(R.id.item_header_view).setHeader(it, info.type)
            }
            getView<EliteImageView>(R.id.item_elite_view).apply {
                setImageUrl(info.content!!.data.cover.feed)
                setDailyVisible(info.content!!.data.library == "DAILY")
                setOnClickListener {
                    //跳转到视频详情
                    VideoDetailActivity.start(
                        mContext!!,
                        item.data.content?.data!!,
                        mData as ArrayList,
                        mData.indexOf(item)
                    )
                }
            }
        }

    }

}