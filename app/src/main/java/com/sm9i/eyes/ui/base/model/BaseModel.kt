package com.sm9i.eyes.ui.base.model

import com.sm9i.eyes.rx.error.globalHandleError
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.rx.RxThreadHelper
import io.reactivex.Observable
import io.reactivex.internal.schedulers.RxThreadFactory


/**
 * baseModel
 */
interface BaseModel {

    /**
     * 加载更多信息 ，AndyInfo
     */
    fun loadMoreAndyInfo(nextPageUrl: String?): Observable<AndyInfo> =
        Api.getDefault()
            .getMoreAndyInfo(nextPageUrl)
            .compose(globalHandleError())
            .compose(RxThreadHelper.switchObservableThread())


    /**
     * 根据url获取数据
     */
    fun getDataInfoFromUrl(url: String?): Observable<AndyInfo> =
        Api.getDefault().getDataInfoFromUrl(url).compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())
}