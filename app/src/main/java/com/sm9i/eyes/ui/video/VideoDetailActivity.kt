package com.sm9i.eyes.ui.video

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.entiy.ContentBean
import com.sm9i.eyes.net.Extras
import com.sm9i.eyes.player.IjkMediaController
import com.sm9i.eyes.player.render.IRenderView
import com.sm9i.eyes.ui.base.BaseActivity
import com.sm9i.eyes.ui.video.presenter.VideoDetailPresenter
import com.sm9i.eyes.ui.video.view.VideoDetailView
import kotlinx.android.synthetic.main.activity_video_detail.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.ArrayList

class VideoDetailActivity : BaseActivity<VideoDetailView, VideoDetailPresenter>(), VideoDetailView {


    private lateinit var ijkMediaController: IjkMediaController

    private lateinit var mCurrentVideoInfo: ContentBean
    private var mCurrentIndex = 0
    private lateinit var mVideoListInfo: MutableList<Content>

    companion object {

        /**
         * 跳转
         */
        @JvmStatic
        fun start(
            context: Context,
            content: ContentBean,
            videoListInfo: ArrayList<Content>,
            defaultIndex: Int = 0
        ) {
            val bundle = Bundle()
            bundle.putSerializable(Extras.VIDEO_INFO, content)
            bundle.putSerializable(Extras.VIDEO_LIST_INFO, videoListInfo)
            bundle.putInt(Extras.VIDEO_INFO_INDEX, defaultIndex)
            val starter = Intent(context, VideoDetailActivity::class.java)
            starter.putExtras(bundle)
            context.startActivity(starter)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        initPlaceHolder()
        initMediaController()

        playVideo()
    }

    override fun getBundleExtras(extras: Bundle) {
        mCurrentVideoInfo = extras.getSerializable(Extras.VIDEO_INFO) as ContentBean
        mVideoListInfo = extras.getSerializable(Extras.VIDEO_LIST_INFO) as ArrayList<Content>
        mCurrentIndex = extras.getInt(Extras.VIDEO_INFO_INDEX, 0)
    }

    private fun initMediaController() {
        ijkMediaController = IjkMediaController(this)
        ijkMediaController.initData(mCurrentIndex, mVideoListInfo.size, mCurrentVideoInfo)
        ijkMediaController.controllerListener = object : IjkMediaController.ControllerListener {
            override fun onBackClick() {
                finish()
            }

            override fun onPreCLick() {
                mCurrentIndex = --mCurrentIndex
                ijkMediaController.currentIndex = mCurrentIndex
                refreshVideo(getFutureVideo())
            }

            override fun onNextClick() {
                mCurrentIndex = ++mCurrentIndex
                ijkMediaController.currentIndex = mCurrentIndex
                refreshVideo(getFutureVideo())
            }

            override fun onFullScreenClick() {
                video_view_wrapper.enterFullScreen()
            }

            override fun onTinyScreenClick() {
                video_view_wrapper.exitFullScreen()
            }

            override fun onErrorViewClick() {
                ijkMediaController.hide()
                refreshVideo(getFutureVideo())

            }

            override fun onShowController(isShowController: Boolean) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDoubleTap() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
    }

    private fun playVideo() {
        with(video_view_wrapper) {
            //视频准备完毕
            setOnPreparedListener(IMediaPlayer.OnPreparedListener {
                resetType()
                hidePlaceImage()
            })
            //设置播放视频
            setOnErrorListener(IMediaPlayer.OnErrorListener { _, _, _ ->
                showErrorView()
                true
            })

            //播放完毕
            setOnCompletionListener(IMediaPlayer.OnCompletionListener {

            })
            setVideoPath(mCurrentVideoInfo.playUrl)
            toggleAspectRatio(IRenderView.AR_MATCH_PARENT)
            setMediaController(ijkMediaController)
            start()
        }
    }

    /**
     * 重置视频信息
     */
    private fun refreshVideo(videoInfo: Content?) {
        videoInfo?.let {
            mCurrentVideoInfo = videoInfo.data

        }
    }

    /**
     * 获取即将展示的视频
     */
    private fun getFutureVideo(): Content {
        return if (mVideoListInfo[mCurrentIndex].data.content != null) {
            mVideoListInfo[mCurrentIndex].data.content!!
        } else {
            mVideoListInfo[mCurrentIndex]
        }
    }

    /**
     * 设置替换图片
     */
    private fun initPlaceHolder() {
        video_view_wrapper.setPlaceImageUrl(mCurrentVideoInfo.cover.detail)
    }

    override fun getRelatedVideoInfoSuccess(itemList: MutableList<Content>) {
    }

    override fun getRelatedVideoFail() {
    }

    override fun getContentViewLayoutId(): Int = R.layout.activity_video_detail


}
