package com.sm9i.eyes.ui.video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.ui.base.BaseActivity
import com.sm9i.eyes.ui.video.presenter.VideoDetailPresenter
import com.sm9i.eyes.ui.video.view.VideoDetailView

class VideoDetailActivity : BaseActivity<VideoDetailView, VideoDetailPresenter>(), VideoDetailView {
    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun getContentViewLayoutId(): Int = R.layout.activity_video_detail

}
