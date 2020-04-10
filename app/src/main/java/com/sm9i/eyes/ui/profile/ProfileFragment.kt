package com.sm9i.eyes.ui.profile

import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment


class ProfileFragment : BaseAppCompatFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_profile
}