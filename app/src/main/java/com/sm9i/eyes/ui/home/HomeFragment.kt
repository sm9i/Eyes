package com.sm9i.eyes.ui.home

import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment


class HomeFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_home
}