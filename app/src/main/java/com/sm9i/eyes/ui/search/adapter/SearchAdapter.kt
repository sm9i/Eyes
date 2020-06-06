package com.sm9i.eyes.ui.search.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sm9i.eyes.R


class SearchAdapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_hot_search, data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.tv_text, item)
    }

}