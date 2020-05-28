package com.sm9i.eyes.ui.video.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.facebook.drawee.view.SimpleDraweeView
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.utils.getElapseTimeForShow


class VideoDetailAdapter(data: MutableList<Content>) :
    BaseQuickAdapter<Content, BaseViewHolder>(data) {
    companion object {
        const val TEXT_CARD = "textCard"
        const val VIDEO_SMALL_CARD = "videoSmallCard"
        const val TEXT_CARD_TYPE = 0
        const val VIDEO_SMALL_CARD_TYPE = 1
    }

    override fun convert(helper: BaseViewHolder?, item: Content) {
        when (helper?.itemViewType) {
            TEXT_CARD_TYPE -> setTextCardInfo(helper, item)
            VIDEO_SMALL_CARD_TYPE -> setVideoSmallCardInfo(helper, item)
        }
    }

    init {
        multiTypeDelegate = object : MultiTypeDelegate<Content>() {
            override fun getItemType(t: Content): Int {
                when (t.type) {
                    TEXT_CARD -> return TEXT_CARD_TYPE
                    VIDEO_SMALL_CARD -> return VIDEO_SMALL_CARD_TYPE
                }
                return -1
            }
        }
        with(multiTypeDelegate) {
            registerItemType(TEXT_CARD_TYPE, R.layout.item_video_text_card)
            registerItemType(VIDEO_SMALL_CARD_TYPE, R.layout.item_video_samll_card)
        }
    }

    /**
     * 设置卡片信息
     */
    private fun setTextCardInfo(helper: BaseViewHolder, item: Content) {
        helper.setText(R.id.tv_text, item.data.text)
    }

    /**
     * 设置视频信息
     */
    private fun setVideoSmallCardInfo(helper: BaseViewHolder, item: Content) {
        val imageView = helper.getView<SimpleDraweeView>(R.id.iv_image)
        imageView.setImageURI(item.data.cover.feed)
        helper.setText(R.id.tv_title, item.data.title)
        val description = "#${item.data.category}   /   ${getElapseTimeForShow(item.data.duration)}"
        helper.setText(R.id.tv_time, description)

    }
}