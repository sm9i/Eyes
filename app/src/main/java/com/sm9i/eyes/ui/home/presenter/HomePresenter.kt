package com.sm9i.eyes.ui.home.presenter

import android.view.View
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.home.model.HomeModel
import com.sm9i.eyes.ui.home.view.HomeView
import com.uber.autodispose.autoDispose


class HomePresenter : BasePresenter<HomeView>() {

    private var mHomeModel: HomeModel = HomeModel()
    private var mNetPageUrl: String? = null
    /**
     * 加载首页信息
     */
    fun loadCategoryData() {
        mView?.showLoading()
        //autoDispose 解决rxjava的内存泄露
        mHomeModel.loadCategoryInfo().autoDispose(mScopeProvider).subscribe({
            mView?.showContent()
            mNetPageUrl = it.nextPageUrl
            mView?.loadDataSuccess(it)
        }, {
            mView?.showNetError(View.OnClickListener {
                loadCategoryData()
            })
        })
    }

    /**
     * 刷新首页延迟1s
     */
    fun refreshCategoryData() {
        mHomeModel.refreshCategoryInfo().autoDispose(mScopeProvider).subscribe(
            {
                mView?.showContent()
                mNetPageUrl = it.nextPageUrl
                mView?.refreshDataSuccess(it)
            }, {
                mView?.showNetError(View.OnClickListener {
                    refreshCategoryData()
                })
            }
        )
    }

    fun loadMoreCategoryData() {
        if (mNetPageUrl != null) {
            mHomeModel.loadMoreAndyInfo(mNetPageUrl).autoDispose(mScopeProvider).subscribe(
                {
                    mView?.showContent()
                    if (it.nextPageUrl != null) {
                        mView?.showNoMore()
                    } else {
                        mNetPageUrl = it.nextPageUrl
                        mView?.loadMoreSuccess(it)
                    }
                }, {
                    mView?.showNetError(View.OnClickListener {
                        loadMoreCategoryData()
                    })
                }
            )
        }
    }

}