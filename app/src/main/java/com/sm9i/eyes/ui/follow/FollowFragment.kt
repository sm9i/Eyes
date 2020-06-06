package com.sm9i.eyes.ui.follow

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.BaseAppCompatFragment
import com.sm9i.eyes.ui.base.BaseFragment
import com.sm9i.eyes.ui.base.adapter.BaseDataAdapter
import com.sm9i.eyes.ui.follow.presenter.FollowPresenter
import com.sm9i.eyes.ui.follow.view.FollowView
import com.sm9i.eyes.widget.CustomLoadMoreView
import kotlinx.android.synthetic.main.fragment_follow.*


class FollowFragment : BaseFragment<FollowView, FollowPresenter>(), FollowView {

    companion object {
        @JvmStatic
        fun newInstance(): FollowFragment = FollowFragment()
    }

    private var mAdapter: BaseDataAdapter? = null

    override fun initView(savedInstanceState: Bundle?) {
        mPresenter.getFollowInfo()
        rv_follow_recycler.refreshListener = { mPresenter.refresh() }
    }


    override fun loadFollowInfoSuccess(andyInfo: AndyInfo) {

        if (mAdapter == null) {
            mAdapter = BaseDataAdapter(andyInfo.itemList)
            mAdapter?.setLoadMoreView(CustomLoadMoreView())
            mAdapter?.setOnLoadMoreListener(
                { mPresenter.loadMoreInfo() },
                rv_follow_recycler.rootView
            )
            rv_follow_recycler.setAdapterAndLayoutManager(mAdapter!!, LinearLayoutManager(context))

        } else {
            mAdapter?.setNewData(andyInfo.itemList)
        }
    }

    override fun refreshSuccess(andyInfo: AndyInfo) {
        loadFollowInfoSuccess(andyInfo)
        rv_follow_recycler.refreshComplete()
    }

    override fun loadMoreSuccess(data: AndyInfo) {
        mAdapter?.addData(data.itemList)
        mAdapter?.loadMoreComplete()
    }

    override fun showNoMore() {
        mAdapter?.loadMoreEnd()
    }

    override fun showContent() {
        multiple_state_view.showContent()
    }

    override fun showNetError(onClickListener: View.OnClickListener) {
        multiple_state_view.showNetError(onClickListener)
    }

    override fun getContentViewLayoutId() = R.layout.fragment_follow


}