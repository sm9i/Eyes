package com.sm9i.eyes.ui.base.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.widget.EliteImageView
import com.sm9i.eyes.widget.ItemHeaderView


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
            //            registerItemType(BANNER_TYPE, R.layout.layout_card_banner)
            registerItemType(FOLLOW_CARD_TYPE, R.layout.layout_follow_card)
//            registerItemType(HORIZONTAL_SCROLL_CARD_TYPE, R.layout.layout_horizontal_scroll_card)
//            registerItemType(
//                VIDEO_COLLECTION_WITH_COVER_TYPE,
//                R.layout.layout_collection_with_cover
//            )
//            registerItemType(SQUARE_CARD_COLLECTION_TYPE, R.layout.layout_square_collection)
//            registerItemType(
//                VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD_TYPE,
//                R.layout.item_collection_of_horizontal_scroll_card
//            )
//            registerItemType(
//                VIDEO_COLLECTION_WITH_BRIEF_TYPE,
//                R.layout.layout_collection_with_brief
//            )
//            registerItemType(TEXT_CARD_TYPE, R.layout.layout_single_text)
//            registerItemType(BRIEF_CARD_TYPE, R.layout.layout_brife_card)
//            registerItemType(BLANK_CARD_TYPE, R.layout.layout_blank_card)
//            registerItemType(SQUARE_CARD_TYPE, R.layout.item_square_card)
//            registerItemType(RECTANGLE_CARD_TYPE, R.layout.item_square_card)
//            registerItemType(VIDEO_TYPE, R.layout.layout_single_video)
//            registerItemType(VIDEO_BANNER_THREE_TYPE, R.layout.layout_follow_card)
//            registerItemType(VIDEO_SMALL_CARD_TYPE, R.layout.layout_video_small_card)
        }
    }


    override fun convert(helper: BaseViewHolder?, item: Content) {
        when (helper?.itemViewType) {
//            BANNER_TYPE -> setBannerInfo(helper, item)
            FOLLOW_CARD_TYPE -> setFollowCardInfo(helper, item)
//            HORIZONTAL_SCROLL_CARD_TYPE -> setHorizontalScrollCardInfo(helper, item.data.itemList)
//            VIDEO_COLLECTION_WITH_COVER_TYPE -> setCollectionCardWithCoverInfo(helper, item.data)
//            SQUARE_CARD_COLLECTION_TYPE -> setSquareCollectionInfo(helper, item.data)
//            VIDEO_COLLECTION_OF_HORIZONTAL_SCROLL_CARD_TYPE -> setCollectionOfHorizontalScrollCardInfo(helper, item)
//            VIDEO_COLLECTION_WITH_BRIEF_TYPE -> setCollectionBriefInfo(helper, item)
//            TEXT_CARD_TYPE -> setSingleText(helper, item)
//            BRIEF_CARD_TYPE -> setBriefCardInfo(helper, item)
//            BLANK_CARD_TYPE -> setBlankCardInfo(helper, item)
//            SQUARE_CARD_TYPE -> setSquareCardInfo(helper, item.data)
//            RECTANGLE_CARD_TYPE -> setRectangleCardInfo(helper, item.data)
//            VIDEO_TYPE -> setSingleVideoInfo(helper, item)
//            VIDEO_BANNER_THREE_TYPE -> setBanner3Info(helper, item.data)
//            VIDEO_SMALL_CARD_TYPE -> setSmallCardInfo(helper, item.data)
        }
    }

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
                }
            }
        }

    }

}