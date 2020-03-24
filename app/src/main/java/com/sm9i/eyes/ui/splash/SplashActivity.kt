package com.sm9i.eyes.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseAppCompatActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        loadRootFragment(R.id.fl_container, VideoSplashFragment.newInstance())
    }

    override fun getContentViewLayoutId(): Int = R.layout.activity_splash



}
