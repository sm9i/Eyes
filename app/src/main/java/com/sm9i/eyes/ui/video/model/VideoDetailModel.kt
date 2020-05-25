package com.sm9i.eyes.ui.video.model

import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.entiy.ContentBean
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.rx.RxThreadHelper
import com.sm9i.eyes.rx.error.globalHandleError
import com.sm9i.eyes.ui.base.model.BaseModel
import io.reactivex.Observable


class VideoDetailModel : BaseModel {

    /**
     * 获取相关视频信息
     */
    fun getRelateVideoInfo(id: String): Observable<AndyInfo> = Api.getDefault().getRelatedVideo(id)
        .compose(globalHandleError())
        .compose(RxThreadHelper.switchObservableThread())

    /**
     * 根据视频id获取视频信息
     */
    fun getVideoInfoById(id: String): Observable<ContentBean> =
        Api.getDefault().getVideoInfoById(id).compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

}