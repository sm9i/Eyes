package com.sm9i.eyes.ui.follow.presenter

import android.view.View
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.base.presenter.LoadMorePresenter
import com.sm9i.eyes.ui.follow.model.FollowModel
import com.sm9i.eyes.ui.follow.view.FollowView
import com.uber.autodispose.autoDispose


class FollowPresenter : LoadMorePresenter<AndyInfo, FollowModel, FollowView>() {

    override var mBaseModel: FollowModel = FollowModel()

    fun getFollowInfo() {
        mBaseModel.getFollowInfo().autoDispose(mScopeProvider).subscribe({
            mView?.showContent()
            mNextPageUrl = it.nextPageUrl
            mView?.loadFollowInfoSuccess(it)
        }, {
            mView?.showNetError(View.OnClickListener { getFollowInfo() })
        })
    }

    fun refresh() {
        mBaseModel.getFollowInfo().autoDispose(mScopeProvider).subscribe({
            mView?.showContent()
            mNextPageUrl = it.nextPageUrl
            mView?.refreshSuccess(it)
        }, {
            mView?.showNetError(View.OnClickListener {
                refresh()
            })
        })
    }

}