package com.sm9i.eyes.ui.base.presenter

import android.view.View
import com.sm9i.eyes.ui.base.model.BaseModel
import com.sm9i.eyes.ui.base.view.LoadMoreView
import com.uber.autodispose.autoDispose


/**
 * 加载更多Presenter
 * @param T 数据类型
 * @param M 对应Model
 * @param V view
 */
open class LoadMorePresenter<T, M : BaseModel, V : LoadMoreView<T>> : BasePresenter<V>() {

    protected var mNextPageUrl: String? = null
    protected open lateinit var mBaseModel: M
    fun loadMoreInfo() {
        if (mNextPageUrl != null) {
            mBaseModel.loadMoreAndyInfo(mNextPageUrl).autoDispose(mScopeProvider).subscribe(
                {
                    mView?.showContent()
                    if (mNextPageUrl == null) {
                        mView?.showNoMore()
                    } else {
                        mNextPageUrl = it.nextPageUrl
                        mView?.loadMoreSuccess(it as T)
                    }
                }, {
                    mView?.showNetError(View.OnClickListener {
                        loadMoreInfo()
                    })
                }
            )
        } else {
            mView?.showNoMore()
        }
    }


}