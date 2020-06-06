package com.sm9i.eyes.ui.follow.model

import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.rx.RxThreadHelper
import com.sm9i.eyes.rx.error.globalHandleError
import com.sm9i.eyes.ui.base.model.BaseModel
import io.reactivex.Observable


class FollowModel : BaseModel {


    /**
     * 获取关注首页
     */
    fun getFollowInfo(): Observable<AndyInfo> =
        Api.getDefault().getFollowInfo().compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取全部作者
     */
    fun getAllAuthor(): Observable<AndyInfo> =
        Api.getDefault().getAllAuthor().compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())
}