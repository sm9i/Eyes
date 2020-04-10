package com.sm9i.eyes.ui

import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatActivity
import com.sm9i.eyes.ui.feed.FeedFragment
import com.sm9i.eyes.ui.follow.FollowFragment
import com.sm9i.eyes.ui.home.HomeFragment
import com.sm9i.eyes.ui.profile.ProfileFragment
import com.sm9i.eyes.widget.BottomBar
import com.sm9i.eyes.widget.BottomBarItem
import com.sm9i.eyes.widget.BottomBarItemLayout
import kotlinx.android.synthetic.main.activity_main.*
import me.yokeyword.fragmentation.SupportFragment

class MainActivity : BaseAppCompatActivity() {

    private val mFragments = arrayOfNulls<SupportFragment>(4)

    companion object {
        private const val FIRST = 0
        private const val SECOND = 1
        private const val THIRD = 2
        private const val FOURTH = 3

    }

    override fun initView(savedInstanceState: Bundle?) {
        mFragments[FIRST] = HomeFragment.newInstance()
        mFragments[SECOND] = FeedFragment.newInstance()
        mFragments[THIRD] = FollowFragment.newInstance()
        mFragments[FOURTH] = ProfileFragment.newInstance()
        loadMultipleRootFragment(R.id.fl_container, FIRST, *mFragments)
        initBottomNavigation()
    }

    /**
     * 初始化底部的四个bar
     */
    private fun initBottomNavigation() {
        val home =
            BottomBarItem(R.drawable.ic_tab_strip_icon_feed_selected, getString(R.string.home))
        home.setUnSelectedDrawable(R.drawable.ic_tab_strip_icon_feed)
        val discover = BottomBarItem(
            R.drawable.ic_tab_strip_icon_category_selected,
            getString(R.string.discover)
        )
        discover.setUnSelectedDrawable(R.drawable.ic_tab_strip_icon_category)
        val focus =
            BottomBarItem(R.drawable.ic_tab_strip_icon_follow_selected, getString(R.string.focus))
        focus.setUnSelectedDrawable(R.drawable.ic_tab_strip_icon_follow)
        val mine =
            BottomBarItem(R.drawable.ic_tab_strip_icon_profile_selected, getString(R.string.mine))
        mine.setUnSelectedDrawable(R.drawable.ic_tab_strip_icon_profile)

        with(bottom_navigation_bar) {
            //添加四个item
            addItem(home)
            addItem(discover)
            addItem(focus)
            addItem(mine)
            setOnTabSelectedListener(object : BottomBar.TabSelectedListener {
                override fun onTabSelected(position: Int, prePosition: Int) {
                }

                override fun onTabUnSelected(position: Int) {
                }

                override fun onTabReselected(position: Int) {
                }

            })
        }

    }

    override fun getContentViewLayoutId(): Int = R.layout.activity_main


}
