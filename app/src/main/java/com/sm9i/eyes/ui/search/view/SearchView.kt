package com.sm9i.eyes.ui.search.view

import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.view.LoadMoreView
import com.sm9i.eyes.ui.home.model.HomeModel


interface SearchView : LoadMoreView<AndyInfo> {
    /**
     * 获取热门关键词成功
     */
    fun getHotWordSuccess(hotList: MutableList<String>)

    /**
     * 搜索成功
     */
    fun showSearchSuccess(queryWord: String, andyInfo: AndyInfo)

}