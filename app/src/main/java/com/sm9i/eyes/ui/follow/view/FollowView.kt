package com.sm9i.eyes.ui.follow.view

import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.view.BaseView
import com.sm9i.eyes.ui.base.view.LoadMoreView


interface FollowView : LoadMoreView<AndyInfo> {

    fun loadFollowInfoSuccess(andyInfo: AndyInfo)

    fun refreshSuccess(andyInfo: AndyInfo)
}