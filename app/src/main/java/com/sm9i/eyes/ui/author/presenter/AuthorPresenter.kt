package com.sm9i.eyes.ui.author.presenter

import android.view.View
import com.sm9i.eyes.ui.author.model.AuthorModel
import com.sm9i.eyes.ui.author.view.AuthorView
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.uber.autodispose.autoDispose


class AuthorPresenter : BasePresenter<AuthorView>() {


    private val mAuthorModel: AuthorModel = AuthorModel()

    fun getAuthorTagDetail(id: String) {
        mAuthorModel.getAuthorTagDetail(id).autoDispose(mScopeProvider).subscribe({
            mView?.showContent()
            mView?.loadInfoSuccess(it)
        }, {
            mView?.showNetError(View.OnClickListener { getAuthorTagDetail(id) })
        })
    }
}