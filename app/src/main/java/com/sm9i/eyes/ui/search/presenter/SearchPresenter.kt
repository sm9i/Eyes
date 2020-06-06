package com.sm9i.eyes.ui.search.presenter

import android.view.View
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.presenter.LoadMorePresenter
import com.sm9i.eyes.ui.home.model.HomeModel
import com.sm9i.eyes.ui.search.view.SearchView
import com.uber.autodispose.autoDispose


class SearchPresenter : LoadMorePresenter<AndyInfo, HomeModel, SearchView>() {

    override var mBaseModel: HomeModel = HomeModel()


    /**
     * 获取热门搜索
     */
    fun searchHot() {
        mBaseModel.getHotWord().autoDispose(mScopeProvider).subscribe {
            mView?.getHotWordSuccess(it)
        }
    }

    /**
     * 关键词搜索
     */
    fun searchVideoByWord(word: String) {
        mView?.showLoading()
        mBaseModel?.searchVideoByWord(word).autoDispose(mScopeProvider).subscribe({
            mView?.showContent()
            mView?.showSearchSuccess(word, it)
            mNextPageUrl = it.nextPageUrl
        }, {
            mView?.showNetError(View.OnClickListener { searchVideoByWord(word) })
        })
    }


}
