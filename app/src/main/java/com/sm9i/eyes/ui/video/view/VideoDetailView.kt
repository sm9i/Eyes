package com.sm9i.eyes.ui.video.view

import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.ui.base.view.BaseView


interface VideoDetailView : BaseView {


    /**
     * 获取相关视频成功
     */
    fun getRelatedVideoInfoSuccess(itemList: MutableList<Content>)

    /**
     * 获取相关视频失败
     */
    fun getRelatedVideoFail()

}