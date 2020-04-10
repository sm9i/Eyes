package com.sm9i.eyes.ui.follow

import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment


class FollowFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): FollowFragment = FollowFragment()
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_follow
}