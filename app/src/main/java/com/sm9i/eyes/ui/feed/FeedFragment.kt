package com.sm9i.eyes.ui.feed

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.TabInfo
import com.sm9i.eyes.ui.base.BaseAppCompatFragment
import com.sm9i.eyes.ui.base.BaseFragment
import com.sm9i.eyes.ui.base.BaseFragmentItemAdapter
import com.sm9i.eyes.ui.feed.presenter.FeedPresenter
import com.sm9i.eyes.ui.feed.view.FeedView
import kotlinx.android.synthetic.main.fragment_feed.*

/**
 * 发现页面
 */

class FeedFragment : BaseFragment<FeedView, FeedPresenter>(), FeedView {

    companion object {
        @JvmStatic
        fun newInstance(): FeedFragment = FeedFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        mPresenter.getDiscoverTab()

        iv_search.setOnClickListener {

        }
        tv_all_category.setOnClickListener {

        }
    }

    override fun loadTabSuccess(tabInfo: TabInfo) {
        view_pager.adapter = BaseFragmentItemAdapter(
            childFragmentManager,
            initFragments(tabInfo),
            initTitles(tabInfo)
        )
        view_pager.offscreenPageLimit = tabInfo.tabList.size
        tab_layout.setupWithViewPager(view_pager)

    }


    private fun initFragments(tabInfo: TabInfo): MutableList<Fragment> {
        val fragments = mutableListOf<Fragment>()
        for (i in tabInfo.tabList.indices) {
            fragments.add(TagDetailInfoFragment.newInstance(tabInfo.tabList[i].apiUrl))
        }
        return fragments
    }

    private fun initTitles(tabInfo: TabInfo): MutableList<String> {
        val titles = mutableListOf<String>()
        for (i in tabInfo.tabList.indices) {
            titles.add(tabInfo.tabList[i].name)
        }
        return titles
    }

    override fun showNetError(onClickListener: View.OnClickListener) {
        multiple_state_view.showNetError(onClickListener)
    }

    override fun showContent() {
        multiple_state_view.showContent()

    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_feed

}