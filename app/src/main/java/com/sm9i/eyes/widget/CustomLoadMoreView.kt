package com.sm9i.eyes.widget

import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.sm9i.eyes.R


class CustomLoadMoreView : LoadMoreView() {
    override fun getLayoutId() = R.layout.layout_load_more_view

    override fun getLoadingViewId() = R.id.ll_load_more_loading_view

    override fun getLoadEndViewId() = R.id.rl_load_end_view

    override fun getLoadFailViewId() = R.id.fl_load_more_load_fail_view

}