package com.sm9i.eyes.ui.base

import android.os.Bundle
import android.view.View
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.base.view.BaseView
import com.sm9i.eyes.utils.getGenericInstance


/**
 * mvp 的base activity
 */
abstract class BaseActivity<V, T : BasePresenter<V>> : BaseAppCompatActivity(),
    BaseView {
    protected lateinit var mPresenter: T

    override fun onCreate(savedInstanceState: Bundle?) {
        // 0 view
        mPresenter = getGenericInstance(this, 1)
        lifecycle.addObserver(mPresenter)
        mPresenter.attachView(this as V, this)
        super.onCreate(savedInstanceState)
    }

    override fun showLoading() {
        mMultipleStateView.showLoading()
    }

    override fun showNetError(onClickListener: View.OnClickListener) {
        mMultipleStateView.showNetError(onClickListener)
    }

    override fun showEmpty(onClickListener: View.OnClickListener) {
        mMultipleStateView.showEmpty(onClickListener)
    }

    override fun showContent() {
        mMultipleStateView.showContent()
    }


}