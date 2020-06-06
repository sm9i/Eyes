package com.sm9i.eyes.ui.base.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.utils.getElapseTimeForShow


/**
 * 作者栏目 横向listview
 */
class CollectionBriefAdapter(data: MutableList<Content>) :
    BaseQuickAdapter<Content, BaseViewHolder>(R.layout.item_collection_brief, data) {
    override fun convert(helper: BaseViewHolder, item: Content) {
        with(helper) {
            getView<SimpleDraweeView>(R.id.iv_image).setImageURI(item.data.cover.feed)
            setText(R.id.tv_title, item.data.title)
            val description =
                "#${item.data.category}   /   ${getElapseTimeForShow(item.data.duration)}"
            setText(R.id.tv_desc, description)
        }
    }

}