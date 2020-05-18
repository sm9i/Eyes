package com.sm9i.eyes.player.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController.MediaPlayerControl
import android.widget.FrameLayout
import com.sm9i.eyes.entiy.ContentBean
import com.sm9i.eyes.event.VideoProgressEvent
import com.sm9i.eyes.player.IjkMediaController
import com.sm9i.eyes.utils.stringForTime
import java.util.ArrayList

/**
 * 进度条 基础类
 * sealed// 密封类
 */

abstract class ControllerView(context: Context) : FrameLayout(context) {

    protected lateinit var mPlayer: MediaPlayerControl
    protected lateinit var mController: IjkMediaController
    protected var mCurrentVideoInfo: ContentBean? = null

    //error View
    private var mErrorView: ErrorView? = null
    //所有view
    private val mContentViews: MutableList<View> = ArrayList()

    var isDragging = false//进度条是否正在拖动

    /**
     * 初始化controller 和数据
     */
    fun initControllerAndData(
        player: MediaPlayerControl,
        controller: IjkMediaController,
        currentVideoInfo: ContentBean?
    ) {
        mPlayer = player
        mController = controller
        mCurrentVideoInfo = currentVideoInfo
        LayoutInflater.from(context).inflate(layoutId, this, true)
        initView()
        initData()
    }

    abstract val layoutId: Int


    abstract fun initView()
    abstract fun initData()


    /**
     * 显示
     */
    fun display() {
        updateTogglePauseUI(mPlayer.isPlaying)
    }

    /**
     * 更新暂停切换UI
     * @param isPlaying 是否播放
     */
    open fun updateTogglePauseUI(isPlaying: Boolean) {}

    /**
     * 更新当前进度
     * @param progress 第一进度
     * @param secondaryProgress 第二进度
     */
    open fun updateProgress(progress: Int, secondaryProgress: Int) {}

    /**
     * 更新当前视频播放时间
     * @param currentTime 当前时间
     * @param endTime 结束时间
     */
    open fun updateTime(currentTime: String, endTime: String) {}


    /**
     * 更新播放进度和时间
     */
    fun updateProgressAndTime(videoProgressEvent: VideoProgressEvent) {
        if (isDragging) {
            updateProgress(videoProgressEvent.progress, videoProgressEvent.secondaryProgress)
            updateTime(
                stringForTime(videoProgressEvent.currentPosition),
                stringForTime(videoProgressEvent.duration)
            )
        }
    }

    /**
     * 切换暂停状态
     */
    fun togglePause() {
        if (mPlayer.isPlaying) {
            mPlayer.pause()
        } else {
            mPlayer.start()
        }
        updateTogglePauseUI(mPlayer.isPlaying)
    }

    internal enum class State {
        ERROR
    }

    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        if (child != null && child.tag != State.ERROR) mContentViews.add(child)
    }

    /**
     * 显示错误页面
     */
    fun showErrorView() {
        for (contentView in mContentViews) {
            contentView.visibility = View.GONE
        }
        if (mErrorView == null) {
            mErrorView = ErrorView(context).apply {
                setController(mController)
                tag = State.ERROR
            }
            addView(mErrorView)
        } else {
            mErrorView!!.visibility = View.VISIBLE
        }
    }

    fun showContent() {
        for (content in mContentViews) {
            content.visibility = View.VISIBLE
        }
        if (mErrorView != null) {
            mErrorView!!.visibility = View.GONE
        }
    }
}