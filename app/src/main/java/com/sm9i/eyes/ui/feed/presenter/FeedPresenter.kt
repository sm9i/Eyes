package com.sm9i.eyes.ui.feed.presenter

import android.view.View
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.feed.model.FeedModel
import com.sm9i.eyes.ui.feed.view.FeedView
import com.uber.autodispose.autoDispose


class FeedPresenter : BasePresenter<FeedView>() {


    private val mFeedModel: FeedModel = FeedModel()

    /**
     * 获取导航栏信息
     */
    fun getDiscoverTab() {
        mFeedModel.getDiscoverTab().autoDispose(mScopeProvider).subscribe(
            {
                mView?.showContent()
                mView?.loadTabSuccess(it.tabInfo)
            }, {
                mView?.showNetError(View.OnClickListener { getDiscoverTab() })
            }
        )


    }
}