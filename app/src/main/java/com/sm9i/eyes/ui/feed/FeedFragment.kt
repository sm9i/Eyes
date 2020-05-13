package com.sm9i.eyes.ui.feed

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment
import kotlinx.android.synthetic.main.fragment_feed.*


class FeedFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): FeedFragment = FeedFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_feed
}