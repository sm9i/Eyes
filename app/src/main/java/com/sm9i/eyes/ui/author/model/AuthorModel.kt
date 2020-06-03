package com.sm9i.eyes.ui.author.model

import com.sm9i.eyes.entiy.Tab
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.rx.RxThreadHelper
import com.sm9i.eyes.rx.error.globalHandleError
import com.sm9i.eyes.ui.base.model.BaseModel
import io.reactivex.Observable


class AuthorModel : BaseModel {

    /**
     * 获取作者信息
     */
    fun getAuthorTagDetail(id: String): Observable<Tab> =
        Api.getDefault().getAuthorTagDetail(id).compose(globalHandleError()).compose(RxThreadHelper.switchObservableThread())

}