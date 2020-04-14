package com.sm9i.eyes.ui.home.model

import android.util.Log
import com.jennifer.andy.simpleeyes.rx.error.globalHandleError
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.entiy.JenniferInfo
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.rx.RxThreadHelper
import com.sm9i.eyes.ui.base.model.BaseModel
import io.reactivex.Observable
import io.reactivex.internal.schedulers.RxThreadFactory
import java.util.concurrent.TimeUnit


class HomeModel : BaseModel {


    /**
     * 获取首页信息
     */
    fun loadCategoryInfo(): Observable<AndyInfo> =
        Api.getDefault().getHomeInfo().compose(globalHandleError())
            .compose(RxThreadHelper.switchObservableThread())

    /**
     * 刷新首页 延迟1s
     */
    fun refreshCategoryInfo(): Observable<AndyInfo> = Api.getDefault().getHomeInfo()
        .delay(1000, TimeUnit.MILLISECONDS)
        .compose(globalHandleError())
        .compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取热门关键字
     */
    fun getHotWord(): Observable<MutableList<String>> =
        Api.getDefault()
            .getHotWord()
            .compose(globalHandleError())
            .compose(RxThreadHelper.switchObservableThread())

    /**
     * 根据关键字搜索
     */
    fun searchVideoByWord(word: String): Observable<AndyInfo> =
        Api.getDefault()
            .searchVideoByWord(word)
            .compose(globalHandleError())
            .compose(RxThreadHelper.switchObservableThread())

    /**
     * 获取每日精选
     */
    fun getDailyElite(): Observable<JenniferInfo> = Api
        .getDefault()
        .getDailyElite()
        .compose(globalHandleError())
        .compose(RxThreadHelper.switchObservableThread())


}