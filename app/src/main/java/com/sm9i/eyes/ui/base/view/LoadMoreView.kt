package com.sm9i.eyes.ui.base.view


interface LoadMoreView<T> : BaseView {

    /**
     * 加载更多成功
     */
    fun loadMoreSuccess(data: T)

    /**
     * 没有更多
     */
    fun showNoMore()
}