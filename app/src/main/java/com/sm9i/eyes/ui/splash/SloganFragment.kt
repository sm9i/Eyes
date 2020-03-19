package com.sm9i.eyes.ui.splash

import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment


class SloganFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = SloganFragment()
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_slogan


}