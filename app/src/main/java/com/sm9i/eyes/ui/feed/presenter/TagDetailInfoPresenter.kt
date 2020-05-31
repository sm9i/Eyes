package com.sm9i.eyes.ui.feed.presenter

import android.view.View
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.base.presenter.LoadMorePresenter
import com.sm9i.eyes.ui.feed.model.FeedModel
import com.sm9i.eyes.ui.feed.view.TagDetailInfoView
import com.uber.autodispose.autoDispose


class TagDetailInfoPresenter : LoadMorePresenter<AndyInfo, FeedModel, TagDetailInfoView>() {


    override var mBaseModel: FeedModel = FeedModel()


    /**
     * 获取tab栏下的信息
     */
    fun getDetailInfo(url: String) {
        mBaseModel.getDataInfoFromUrl(url).autoDispose(mScopeProvider).subscribe({
            mView?.showContent()
            mView?.showGetTabInfoSuccess(it)
            mNextPageUrl = it.nextPageUrl
            if (mNextPageUrl == null) mView?.showNoMore()
        }, {
            mView?.showNetError(View.OnClickListener {
                getDetailInfo(url)
            })
        })
    }

}