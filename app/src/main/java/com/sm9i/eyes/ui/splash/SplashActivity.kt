package com.sm9i.eyes.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.UserPreferences
import com.sm9i.eyes.ui.base.BaseAppCompatActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseAppCompatActivity() {
    override fun initView(savedInstanceState: Bundle?) {

        if(UserPreferences.getUserIsFirstLogin()){
            loadRootFragment(R.id.fl_container, VideoSplashFragment.newInstance())
        }else{
            loadRootFragment(R.id.fl_container, CommonSplashFragment.newInstance())
        }

    }

    override fun getContentViewLayoutId(): Int = R.layout.activity_splash



}
