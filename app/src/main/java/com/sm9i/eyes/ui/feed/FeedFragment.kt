package com.sm9i.eyes.ui.feed

import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment


class FeedFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): FeedFragment = FeedFragment()
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_feed
}