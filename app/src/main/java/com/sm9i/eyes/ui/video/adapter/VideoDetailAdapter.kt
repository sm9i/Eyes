package com.sm9i.eyes.ui.video.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.sm9i.eyes.entiy.Content


class VideoDetailAdapter(data: MutableList<Content>) :
    BaseQuickAdapter<Content, BaseViewHolder>(data) {
    companion object {
        const val TEXT_CARD = "textCard"
        const val VIDEO_SMALL_CARD = "videoSmallCard"
        const val TEXT_CARD_TYPE = 0
        const val VIDEO_SMALL_CARD_TYPE = 1
    }

    override fun convert(helper: BaseViewHolder?, item: Content?) {
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
        }
    }

}