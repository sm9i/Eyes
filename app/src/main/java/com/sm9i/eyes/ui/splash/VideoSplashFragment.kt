package com.sm9i.eyes.ui.splash

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager.widget.ViewPager
import com.sm9i.eyes.R
import com.sm9i.eyes.UserPreferences
import com.sm9i.eyes.player.render.IRenderView.AR_ASPECT_FIT_PARENT
import com.sm9i.eyes.ui.MainActivity
import com.sm9i.eyes.ui.base.BaseAppCompatFragment
import com.sm9i.eyes.utils.readyGoThenKillSelf
import kotlinx.android.synthetic.main.fragment_video_landing.*


class VideoSplashFragment : BaseAppCompatFragment() {


    private var mVideoPosition = 0
    private var isHasPaused = false
    private lateinit var mFragment: MutableList<SloganFragment>

    companion object {
        @JvmStatic
        fun newInstance(): VideoSplashFragment = VideoSplashFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        initSloganText()
        setVideoObserver()
        playVideo()
    }

    private fun initSloganText() {
        tv_slogan_en.printText(resources.getStringArray(R.array.slogan_array_en)[0])
        tv_slogan_zh.printText(resources.getStringArray(R.array.slogan_array_zh)[0])

        pageIndicatorView.count = 4

        //创建4个slogan
        mFragment = List(4) { SloganFragment.newInstance() } as MutableList<SloganFragment>

        with(view_pager) {

            verticalListener = { goMainActivity() }
            horizontalListener = { goMainActivity() }
            mDisMissIndex = mFragment.size - 1
            adapter = SplashVideoFragmentAdapter(mFragment, childFragmentManager)

            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    pageIndicatorView.setSelected(position)
                }

                override fun onPageSelected(position: Int) {
                    if (position in 0..3) {
                        tv_slogan_zh.printText(resources.getStringArray(R.array.slogan_array_zh)[position])
                        tv_slogan_en.printText(resources.getStringArray(R.array.slogan_array_en)[position])
                    }
                }

            })


        }


    }

    /**
     * 生命周期
     */
    private fun setVideoObserver() {
        ///视频的生命周期
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onVideoResume() {
                video_view.seekTo(mVideoPosition)
                video_view.resume()
                isHasPaused = false
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onVideoPause() {
                mVideoPosition = video_view.currentPosition
                video_view.pause()
                isHasPaused = true
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onVideoStop() {
                video_view.stopPlayback()
            }
        })
    }

    private fun playVideo() {
        val path = R.raw.landing
        with(video_view) {
            //设置视频比例
            setAspectRatio(AR_ASPECT_FIT_PARENT)
            setVideoPath("android.resource://${activity?.packageName}/$path")
            //准备好
            setOnPreparedListener {
                requestFocus()
                seekTo(0)
                start()
            }
            //播放完毕后 无限循环
            setOnCompletionListener {
                it.isLooping = true
                start()
            }


        }
    }


    private fun goMainActivity() {
        UserPreferences.saveUserIsFirstLogin(false)
        readyGoThenKillSelf<MainActivity>()
    }


    override fun getContentViewLayoutId(): Int = R.layout.fragment_video_landing

}