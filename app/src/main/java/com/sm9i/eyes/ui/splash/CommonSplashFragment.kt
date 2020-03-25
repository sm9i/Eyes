package com.sm9i.eyes.ui.splash

import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatFragment


class CommonSplashFragment : BaseAppCompatFragment() {


    companion object {
        @JvmStatic
        fun newInstance(): CommonSplashFragment = CommonSplashFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun getContentViewLayoutId(): Int = R.layout.fragment_common_landing

}