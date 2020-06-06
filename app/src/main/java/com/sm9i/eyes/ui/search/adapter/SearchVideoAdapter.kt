package com.sm9i.eyes.ui.search.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content


class SearchVideoAdapter(data: List<Content>?) : BaseQuickAdapter<Content, BaseViewHolder>(data) {

    companion object {

        const val VIDEO_COLLECTION_WITH_BRIEF_TYPE = 0
        const val VIDEO_TYPE = 1

        const val VIDEO_COLLECTION_WITH_BRIEF = "videoCollectionWithBrief"
        const val VIDEO = "video"
    }

    init {
        multiTypeDelegate = object : MultiTypeDelegate<Content>() {
            override fun getItemType(content: Content?): Int {
                when (content?.type) {
                    VIDEO_COLLECTION_WITH_BRIEF -> return VIDEO_COLLECTION_WITH_BRIEF_TYPE
                    VIDEO -> return VIDEO_TYPE
                }
                return VIDEO_TYPE
            }
        }
        with(multiTypeDelegate) {
            registerItemType(VIDEO_COLLECTION_WITH_BRIEF_TYPE, R.layout.layout_collection_with_brief)
//            registerItemType(VIDEO_TYPE, R.layout.layout_single_video)
        }

    }

    override fun convert(helper: BaseViewHolder?, item: Content?) {
    }

}