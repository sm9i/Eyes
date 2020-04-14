package com.sm9i.eyes.ui.home

import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.BaseFragment
import com.sm9i.eyes.ui.home.presenter.HomePresenter
import com.sm9i.eyes.ui.home.view.HomeView


class HomeFragment : BaseFragment<HomeView, HomePresenter>(), HomeView {

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        ///初始化后请求数据
        mPresenter.loadCategoryData()
    }

    override fun loadDataSuccess(andyInfo: AndyInfo) {

    }

    override fun refreshDataSuccess(andyInfo: AndyInfo) {
    }

    override fun loadMoreSuccess(andyInfo: AndyInfo) {
    }

    override fun showNoMore() {
    }

    /**
     * 滑动到顶部
     */
    fun scrollTop() {

    }


    override fun getContentViewLayoutId(): Int = R.layout.fragment_home

}