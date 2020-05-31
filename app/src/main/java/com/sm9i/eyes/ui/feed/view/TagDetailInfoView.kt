package com.sm9i.eyes.ui.feed.view

import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.view.BaseView
import com.sm9i.eyes.ui.base.view.LoadMoreView


interface TagDetailInfoView : LoadMoreView<AndyInfo> {


    fun showGetTabInfoSuccess(andyInfo: AndyInfo)
}