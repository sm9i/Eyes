package com.sm9i.eyes.ui.feed.model

import com.sm9i.eyes.entiy.*
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.rx.RxThreadHelper
import com.sm9i.eyes.rx.error.globalHandleError
import com.sm9i.eyes.ui.base.model.BaseModel
import io.reactivex.Observable


class FeedModel : BaseModel {


    /**
     * 获取tabBar
     */
    fun getDiscoverTab(): Observable<Tab> =
        Api.getDefault().getDiscoveryTab().compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取全部分类
     */
    fun loadAllCategoriesInfo(): Observable<AndyInfo> =
        Api.getDefault().getAllCategoriesInfo().compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取排行榜tab
     */
    fun getRankListTab(): Observable<Tab> =
        Api.getDefault().getRankListTab().compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取专栏信息
     */
    fun getTopicInfo(): Observable<AndyInfo> =
        Api.getDefault().getTopicInfo().compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取种类下的tab信息
     */
    fun getCategoryTabInfo(id: String): Observable<Category> =
        Api.getDefault().getCategoryTabInfo(id).compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())


}