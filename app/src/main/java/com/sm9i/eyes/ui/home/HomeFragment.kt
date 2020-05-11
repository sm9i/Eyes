package com.sm9i.eyes.ui.home

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.BaseFragment
import com.sm9i.eyes.ui.base.adapter.BaseDataAdapter
import com.sm9i.eyes.ui.home.presenter.HomePresenter
import com.sm9i.eyes.ui.home.view.HomeView
import com.sm9i.eyes.utils.getScreenHeight
import com.sm9i.eyes.utils.getScreenWidth
import com.sm9i.eyes.widget.CustomLoadMoreView
import com.sm9i.eyes.widget.pull.head.HomePageHeaderView
import com.sm9i.eyes.widget.pull.zoom.PullToZoomBase
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment<HomeView, HomePresenter>(), HomeView {


    private lateinit var mHomePageHeaderView: HomePageHeaderView
    private var mCateGoryAdapter: BaseDataAdapter? = null


    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        rv_home_recycler.setOnPullZoomListener(object : PullToZoomBase.OnPullZoomListener {
            override fun onPullZooming(scrollValue: Int) {
                mHomePageHeaderView.showRefreshCover(scrollValue)
            }

            override fun onPullZoomEnd() {
                if (mHomePageHeaderView.judgeCanRefresh()) {
                    mPresenter.refreshCategoryData()
                } else {
                    mHomePageHeaderView.hideRefreshCover()
                }
            }

        })
        mPresenter.loadCategoryData()
    }

    override fun loadDataSuccess(andyInfo: AndyInfo) {
        if (mCateGoryAdapter == null) {
            setHeaderInfo(andyInfo)
            setAdapterAndListener(andyInfo)
        } else {
            mCateGoryAdapter?.setNewData(andyInfo.itemList)
        }
    }

    private fun setAdapterAndListener(andyInfo: AndyInfo) {
        val recyclerView = rv_home_recycler.getPullRootView()
        recyclerView.setItemViewCacheSize(10)
        mCateGoryAdapter = BaseDataAdapter(andyInfo.itemList)
        mCateGoryAdapter?.setOnLoadMoreListener({
            mPresenter.loadMoreCategoryData()
        }, recyclerView)
        mCateGoryAdapter?.setLoadMoreView(CustomLoadMoreView())
        rv_home_recycler.setAdapterAndLayoutManager(
            mCateGoryAdapter!!,
            LinearLayoutManager(_mActivity)
        )

    }

    override fun refreshDataSuccess(andyInfo: AndyInfo) {
        mHomePageHeaderView.hideRefreshCover()
    }

    override fun loadMoreSuccess(andyInfo: AndyInfo) {
        mCateGoryAdapter?.addData(andyInfo.itemList)
        mCateGoryAdapter?.loadMoreComplete()
    }

    override fun showNoMore() {
        mCateGoryAdapter?.loadMoreEnd()

    }

    private fun setHeaderInfo(andyInfo: AndyInfo) {
        mHomePageHeaderView = HomePageHeaderView(context!!)
        val lp = ViewGroup.LayoutParams(context!!.getScreenWidth(), context!!.getScreenHeight() / 2)
        rv_home_recycler.setHeaderViewLayoutParams(LinearLayout.LayoutParams(lp))
        mHomePageHeaderView.setHeaderInfo(andyInfo.topIssue, andyInfo.topIssue.data.itemList, this)
        rv_home_recycler.setHeaderView(mHomePageHeaderView)
    }


    /**
     * 滑动到顶部
     */
    fun scrollTop() {
        rv_home_recycler.scrollToTop()
    }


    override fun getContentViewLayoutId(): Int = R.layout.fragment_home

}