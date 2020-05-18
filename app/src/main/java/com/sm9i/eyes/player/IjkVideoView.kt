package com.sm9i.eyes.player

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.MediaController
import com.sm9i.eyes.event.VideoProgressEvent
import com.sm9i.eyes.player.render.IRenderView
import com.sm9i.eyes.player.render.SurfaceRenderView
import com.sm9i.eyes.player.render.TextureRenderView
import com.sm9i.eyes.rx.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.*
import java.util.concurrent.TimeUnit


class IjkVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MediaController.MediaPlayerControl {


    private var mUri: Uri? = null
    private var mMediaPlayer: IMediaPlayer? = null
    private var mMediaController: IjkMediaController? = null
    private lateinit var mAppContext: Context
    //参数
    private var mVideoWidth = 0
    private var mVideoHeight = 0
    private var mSurfaceWidth = 0
    private var mSurfaceHeight = 0
    private var mVideoSarNum = 0
    private var mVideoSarDen = 0

    private val mCanPause = true
    private val mCanSeekBack = true
    private val mCanSeekForward = true

    private var mCurrentBufferPercentage = 0
    private var mSeekWhenPrepared = 0

    private var mRenderUIView: View? = null

    //状态吧？
    private var mCurrentState = STATE_IDLE
    private var mTargetState = STATE_IDLE
    //surface 管理接口
    private var mSurfaceHolder: IRenderView.ISurfaceHolder? = null
    //控制接口
    private var mRenderView: IRenderView? = null
    //渲染工具
    private var mCurrentRender = RENDER_NONE
    //视频比例
    private var mCurrentAspectRatio = allAspectRatio[0]
    //视频旋转？
    private var mVideoRotationDegree = 0

    private var mDisposable: Disposable? = null

    //渲染回调
    private val mShCallback: IRenderView.IRenderCallBack = object : IRenderView.IRenderCallBack {
        override fun onSurfaceCreated(holder: IRenderView.ISurfaceHolder, width: Int, height: Int) {
            if (holder.renderView !== mRenderView) {
                Log.e(TAG, "onSurfaceCreated: unmatched render callback\n")
                return
            }
            mSurfaceHolder = holder
            if (mMediaPlayer != null) bindSurfaceHolder(mMediaPlayer, holder) else openVideo()
        }

        override fun onSurfaceChanged(
            holder: IRenderView.ISurfaceHolder,
            format: Int,
            width: Int,
            height: Int
        ) {

            if (holder.renderView !== mRenderView) {
                Log.e(TAG, "onSurfaceChanged: unmatched render callback\n")
                return
            }
            mSurfaceWidth = width
            mSurfaceHeight = height
            val isValidState = mTargetState == STATE_PLAYING
            val hasValidSize =
                !mRenderView!!.shouldWaitForResize() || mVideoWidth == width && mVideoHeight == height
            if (mMediaPlayer != null && isValidState && hasValidSize) {
                if (mSeekWhenPrepared != 0) {
                    seekTo(mSeekWhenPrepared)
                }
                start()
            }
        }

        override fun onSurfaceDestroyed(holder: IRenderView.ISurfaceHolder) {
            if (holder.renderView !== mRenderView) {
                Log.e(TAG, "onSurfaceDestroyed: unmatched render callback\n")
                return
            }
            mSurfaceHolder = null
            releaseWithoutStop()
        }

    }

    companion object {
        //播放状态
        private const val STATE_ERROR = -1
        private const val STATE_IDLE = 0
        private const val STATE_PREPARING = 1
        private const val STATE_PREPARED = 2
        private const val STATE_PLAYING = 3
        private const val STATE_PAUSED = 4
        private const val STATE_PLAYBACK_COMPLETED = 5
        //视频比例
        private val allAspectRatio = intArrayOf(
            IRenderView.AR_ASPECT_FIT_PARENT,
            IRenderView.AR_ASPECT_FILL_PARENT,
            IRenderView.AR_ASPECT_WRAP_CONTENT,
            IRenderView.AR_16_9_FIT_PARENT,
            IRenderView.AR_4_3_FIT_PARENT
        )
        //渲染工具
        const val RENDER_NONE = 0
        const val RENDER_SURFACE_VIEW = 1
        const val RENDER_TEXTURE_VIEW = 2
        private const val TAG = "IjkVideoView"

    }

    init {
        initVideoView(context)
    }

    //初始化
    private fun initVideoView(context: Context) {
        mAppContext = context.applicationContext
        initRenders()
        //初始化宽高
        mVideoWidth = 0
        mVideoHeight = 0
        //获取焦点
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        mCurrentState = STATE_IDLE
        mTargetState = STATE_IDLE
        background = ColorDrawable(Color.BLACK)
    }

    /**
     * 设置渲染Render
     * 4.0 版本以上用 TextureView
     *  否 用  SurfaceView
     */
    private fun initRenders() {
        mCurrentRender = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            RENDER_TEXTURE_VIEW
        } else {
            RENDER_SURFACE_VIEW
        }
        setRender(mCurrentRender)
    }

    /**
     *设置渲染Render
     */
    private fun setRender(render: Int) {
        when (render) {
            RENDER_NONE -> setRenderView(null)
            RENDER_TEXTURE_VIEW -> {
                val renderView = TextureRenderView(context)
                if (mMediaPlayer == null) {
                    renderView.getSurfaceHolder().bindToMediaPlayer(mMediaPlayer)
                    renderView.setVideoSize(mMediaPlayer!!.videoWidth, mMediaPlayer!!.videoHeight)
                    renderView.setVideoSampleAspectRatio(
                        mMediaPlayer!!.videoSarNum,
                        mMediaPlayer!!.videoSarDen
                    )
                    renderView.setAspectRatio(mCurrentAspectRatio)
                }
                setRenderView(renderView)
            }
            RENDER_SURFACE_VIEW -> {
                val renderView = SurfaceRenderView(context)
                setRenderView(renderView)
            }
            //没设置render
            else -> Log.e(TAG, String.format(Locale.getDefault(), "invalid render %d\n", render))
        }
    }

    /**
     * 设置渲染界面，初始化当前参数，设置回调等
     */
    private fun setRenderView(renderView: IRenderView?) {
        if (mRenderView != null) {
            if (mMediaPlayer != null)
                mMediaPlayer?.setDisplay(null)
            val renderUIView = mRenderView?.getView()
            mRenderView?.removeRenderCallback(mShCallback)
            mRenderView = null
            removeView(renderUIView)
        }
        if (renderView == null) return
        mRenderView = renderView
        renderView.setAspectRatio(mCurrentAspectRatio)
        if (mVideoWidth > 0 && mVideoHeight > 0)
            renderView.setVideoSize(mVideoWidth, mVideoHeight)
        if (mVideoSarNum > 0 && mVideoSarDen > 0)
            renderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
        mRenderUIView = mRenderView?.getView().apply {
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        }
        addView(mRenderUIView)
        mRenderView?.addRenderCallback(mShCallback)
        mRenderView?.setVideoRotation(mVideoRotationDegree)

    }

    /**
     * surface绑定
     */
    private fun bindSurfaceHolder(mp: IMediaPlayer?, holder: IRenderView.ISurfaceHolder) {
        if (mp == null) return
        if (holder == null) {
            mp.setDisplay(null)
            return
        }
        holder.bindToMediaPlayer(mp)
    }

    /**
     * 打开视频
     */
    private fun openVideo() {
        if (mUri == null || mSurfaceHolder == null) {
            return
        }
        release(false)
    }

    private fun releaseWithoutStop() {
        if (mMediaPlayer != null) mMediaPlayer!!.setDisplay(null)
    }

    /**
     * 释放播放器
     */
    private fun release(clearTargetState: Boolean) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null

        }
    }

    /**
     * 播放状态
     */
    private fun isInPlaybackState(): Boolean {
        return mMediaPlayer != null
                && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE
                && mCurrentState != STATE_PREPARING
    }

    /**
     * 取消进度与时间更新线程
     */
    private fun cancelProgressRunnable() {
        if (mDisposable != null && !mDisposable!!.isDisposed) {
            mDisposable?.dispose()
            mDisposable = null
        }
    }

    /**
     * 更新进度
     */
    private fun startProgressRunnable() {
        if (mDisposable == null || mDisposable!!.isDisposed) {
            mDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val position = currentPosition
                    val duration = duration
                    if (duration >= 0 && bufferPercentage > 0) {
                        val progress = 1000L * position / duration
                        val secondProgress = bufferPercentage * 10
                        val event =
                            VideoProgressEvent(duration, position, progress.toInt(), secondProgress)
                        if (mMediaController != null) {
                            mMediaController!!.updateProgressAndTime(event)
                        }
                        RxBus.post(event)
                    }
                }
        }
    }

    /**
     * 是否在播放
     */
    override fun isPlaying() = isInPlaybackState() && mMediaPlayer!!.isPlaying

    override fun canSeekForward() = mCanSeekForward

    override fun getDuration(): Int {
        return if (isInPlaybackState()) {
            mMediaPlayer?.duration!!.toInt()
        } else
            -1
    }

    override fun pause() {
        if (isInPlaybackState()) {
            if (mMediaPlayer!!.isPlaying) {
                mMediaPlayer?.pause()
                mCurrentState = STATE_PAUSED
                cancelProgressRunnable()
            }
        }
        mTargetState = STATE_PAUSED
    }

    override fun getBufferPercentage(): Int {
        return if (mMediaPlayer != null) {
            mCurrentBufferPercentage
        } else 0
    }

    override fun seekTo(pos: Int) {
        mSeekWhenPrepared = if (isInPlaybackState()) {
            mMediaPlayer?.seekTo(pos.toLong())
            0
        } else {
            pos
        }
    }

    override fun getCurrentPosition(): Int {
        return if (isInPlaybackState()) {
            mMediaPlayer?.currentPosition!!.toInt()
        } else 0
    }

    override fun canSeekBackward() = mCanSeekBack

    override fun start() {
        if (isInPlaybackState()) {
            mMediaPlayer?.start()
            mCurrentState = STATE_PLAYING
            startProgressRunnable()
        }
        mTargetState = STATE_PLAYING
    }

    override fun getAudioSessionId(): Int {
        return 0
    }

    override fun canPause()= mCanPause

}
