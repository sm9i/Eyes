package com.sm9i.eyes.ui.video

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Content
import com.sm9i.eyes.entiy.ContentBean
import com.sm9i.eyes.event.VideoProgressEvent
import com.sm9i.eyes.net.Extras
import com.sm9i.eyes.player.IjkMediaController
import com.sm9i.eyes.player.render.IRenderView
import com.sm9i.eyes.rx.RxBus
import com.sm9i.eyes.ui.base.BaseActivity
import com.sm9i.eyes.ui.video.adapter.VideoDetailAdapter
import com.sm9i.eyes.ui.video.presenter.VideoDetailPresenter
import com.sm9i.eyes.ui.video.view.VideoDetailView
import com.sm9i.eyes.widget.VideoDetailAuthorView
import com.sm9i.eyes.widget.pull.head.VideoDetailHeadView
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_video_detail.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.ArrayList

class VideoDetailActivity : BaseActivity<VideoDetailView, VideoDetailPresenter>(), VideoDetailView {


    private lateinit var ijkMediaController: IjkMediaController

    private lateinit var mCurrentVideoInfo: ContentBean
    private var mCurrentIndex = 0
    private lateinit var mVideoListInfo: MutableList<Content>

    private var mBackPressed = false
    private var mChangeProgress: Int = 0

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
        initSeekBar()
        registerProgressEvent()
        playVideo()
    }

    override fun getBundleExtras(extras: Bundle) {
        mCurrentVideoInfo = extras.getSerializable(Extras.VIDEO_INFO) as ContentBean
        mVideoListInfo = extras.getSerializable(Extras.VIDEO_LIST_INFO) as ArrayList<Content>
        mCurrentIndex = extras.getInt(Extras.VIDEO_INFO_INDEX, 0)
    }

    /**
     * 注册进度条监听
     */
    private fun registerProgressEvent() {
        RxBus.register(this, VideoProgressEvent::class.java, Consumer {
            if (!video_view_wrapper.isDragging()) {
                seek_bar.progress = it.progress
            }
            seek_bar.secondaryProgress = it.secondaryProgress
        })
    }

    private fun initSeekBar() {
        seek_bar.thumb = ColorDrawable(Color.TRANSPARENT)
        seek_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mChangeProgress = progress
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                video_view_wrapper.setDragging(true)
                video_view_wrapper.showControllerAllTheTIme()
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val newPosition = video_view_wrapper.getDuration() * mChangeProgress / 1000L
                video_view_wrapper.seekTo(newPosition.toInt())
                video_view_wrapper.showController()
                video_view_wrapper.setDragging(false)
            }
        })
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
                seek_bar.thumb = if (isShowController)
                    getDrawable(R.drawable.ic_player_progress_handle)
                else ColorDrawable(Color.TRANSPARENT)
            }

            override fun onDoubleTap() {
                //双击
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
                seek_bar.thumb = null
            })
            setVideoPath(mCurrentVideoInfo.playUrl)
            toggleAspectRatio(IRenderView.AR_MATCH_PARENT)
            setMediaController(ijkMediaController)
            start()
        }
        mPresenter.getRelatedVideo(mCurrentVideoInfo.id)
    }

    /**
     * 重置视频信息
     */
    private fun refreshVideo(videoInfo: Content?) {
        videoInfo?.let {
            mCurrentVideoInfo = videoInfo.data
            rv_video_recycler.visibility = View.INVISIBLE
            video_view_wrapper.togglePlaceImage(true)
            seek_bar.secondaryProgress = 0
            seek_bar.progress = 0
            initPlaceHolder()
            video_view_wrapper.setVideoPath(mCurrentVideoInfo.playUrl)
            video_view_wrapper.start()
            mPresenter.getRelatedVideo(mCurrentVideoInfo.id)
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
        iv_blurred.setImageURI(mCurrentVideoInfo.cover.blurred)
    }

    override fun onPause() {
        super.onPause()
        if (video_view_wrapper.isPLaying()) {
            video_view_wrapper.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mBackPressed) {
            video_view_wrapper.stopPlayback()
            video_view_wrapper.release(true)
        }
        RxBus.unRegister(this)
    }

    override fun onBackPressedSupport() {
        super.onBackPressedSupport()
        mBackPressed = true
    }

    override fun getRelatedVideoInfoSuccess(itemList: MutableList<Content>) {
        with(rv_video_recycler) {
            visibility = View.VISIBLE

            layoutManager = LinearLayoutManager(this@VideoDetailActivity)

            adapter = VideoDetailAdapter(itemList).apply {
                addHeaderView(getVideoDetailView())
                addHeaderView(getRelationVideoInfo())
                openLoadAnimation(BaseQuickAdapter.ALPHAIN)
                setFooterView(
                    LayoutInflater.from(this@VideoDetailActivity).inflate(
                        R.layout.item_the_end,
                        null
                    )
                )
                setOnItemClickListener { _, _, position ->
                    if (getItemViewType(position) != BaseQuickAdapter.HEADER_VIEW) {
                        refreshVideo(getItem(position))
                    }

                }

            }

        }
    }

    override fun getRelatedVideoFail() {
    }

    override fun onResume() {
        super.onResume()
        registerProgressEvent()
    }


    private fun getVideoDetailView(): View {
        return VideoDetailHeadView(this).apply {
            setTitle(mCurrentVideoInfo.title)
            setCategoryAndTime(mCurrentVideoInfo.category, mCurrentVideoInfo.duration)
            setFavoriteCount(mCurrentVideoInfo.consumption.collectionCount)
            setShareCount(mCurrentVideoInfo.consumption.shareCount)
            setReplayCount(mCurrentVideoInfo.consumption.replyCount)
            setDescription(mCurrentVideoInfo.description)
            startScrollAnimation()
        }
    }

    private fun getRelationVideoInfo(): View {
        return VideoDetailAuthorView(this).apply {
            setVideoAuthorInfo(mCurrentVideoInfo.author)
        }
    }

    override fun toggleOverridePendingTransition() = true
    override fun getOverridePendingTransition() = TransitionMode.BOTTOM
    override fun getContentViewLayoutId(): Int = R.layout.activity_video_detail


}
