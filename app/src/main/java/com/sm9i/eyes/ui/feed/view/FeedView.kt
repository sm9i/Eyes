package com.sm9i.eyes.ui.feed.view

import com.sm9i.eyes.entiy.TabInfo
import com.sm9i.eyes.ui.base.view.BaseView


interface FeedView : BaseView {
    fun loadTabSuccess(tabInfo: TabInfo)
}