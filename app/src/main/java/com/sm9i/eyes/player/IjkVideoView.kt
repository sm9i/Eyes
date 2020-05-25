package com.sm9i.eyes.player

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.MediaController
import com.sm9i.eyes.event.VideoProgressEvent
import com.sm9i.eyes.player.render.IRenderView
import com.sm9i.eyes.player.render.SurfaceRenderView
import com.sm9i.eyes.player.render.TextureRenderView
import com.sm9i.eyes.player.view.ControllerViewFactory
import com.sm9i.eyes.rx.RxBus
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit


class IjkVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MediaController.MediaPlayerControl {


    private var mUri: Uri? = null
    //视频header
    private var mHeaders: Map<String, String>? = null
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
    //一系列的监听
    private var mOnCompletionListener: IMediaPlayer.OnCompletionListener? = null
    private var mOnPreparedListener: IMediaPlayer.OnPreparedListener? = null
    private var mOnErrorListener: IMediaPlayer.OnErrorListener? = null
    private var mOnInfoListener: IMediaPlayer.OnInfoListener? = null
    var screenState = ControllerViewFactory.TINY_MODE

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
    private fun bindSurfaceHolder(mp: IMediaPlayer?, holder: IRenderView.ISurfaceHolder?) {
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
        val am = mAppContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        try {
            mMediaPlayer = createPlayer()
            mMediaPlayer?.setOnPreparedListener(mPreparedListener)
            mMediaPlayer?.setOnVideoSizeChangedListener(mSizeChangedListener)
            mMediaPlayer?.setOnCompletionListener(mCompletionListener)
            mMediaPlayer?.setOnErrorListener(mErrorListener)
            mMediaPlayer?.setOnInfoListener(mInfoListener)
            mMediaPlayer?.setOnBufferingUpdateListener(mBufferingUpdateListener)
            mMediaPlayer?.setOnSeekCompleteListener(mSeekCompleteListener)
            mCurrentBufferPercentage = 0
            val scheme = mUri?.scheme
            //设置数据源
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (TextUtils.isEmpty(scheme) || scheme.equals(
                    "file",
                    ignoreCase = true
                ))
            ) {
                val dataSource: IMediaDataSource = FileMediaDataSource(File(mUri.toString()))
                mMediaPlayer?.setDataSource(dataSource)
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mMediaPlayer?.setDataSource(mAppContext, mUri, mHeaders)
            } else {
                mMediaPlayer?.dataSource = mUri.toString()
            }
            bindSurfaceHolder(mMediaPlayer, mSurfaceHolder)
            mMediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mMediaPlayer?.setScreenOnWhilePlaying(true)
            mMediaPlayer?.prepareAsync()
            mCurrentState = STATE_PREPARING
            attachMediaController()
        } catch (ex: IOException) {
            Log.w(TAG, "Unable to open content: $mUri", ex)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
        } catch (ex: IllegalArgumentException) {
            Log.w(TAG, "Unable to open content: $mUri", ex)
            mCurrentState = STATE_ERROR
            mTargetState = STATE_ERROR
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0)
        }
    }

    /**
     * 视频准备监听
     */
    private var mPreparedListener = IMediaPlayer.OnPreparedListener {
        mCurrentState = STATE_PREPARED
        if (mOnPreparedListener != null) {
            mOnPreparedListener!!.onPrepared(mMediaPlayer)
        }
        if (mMediaController != null) {
            mMediaController!!.isEnabled = true
        }
        mVideoWidth = it.videoWidth
        mVideoHeight = it.videoHeight
        val seekToPosition = mSeekWhenPrepared
        if (seekToPosition != 0) {
            seekTo(seekToPosition)
        }
        if (mVideoWidth != 0 && mVideoHeight != 0) {
            mRenderView?.let {
                mRenderView?.setVideoSize(mVideoWidth, mVideoHeight)
                mRenderView?.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
                if (!mRenderView!!.shouldWaitForResize() || mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
                    if (mTargetState == STATE_PLAYING) {
                        start()
                        mMediaController?.firstShow()
                    } else if (!isPlaying && (seekToPosition != 0 || currentPosition > 0)) {
                        mMediaController?.show(0)
                    }
                }
            }
        } else {
            if (mTargetState == STATE_PLAYING) {
                start()
            }
        }
    }

    /**
     * 大小发生变化的时候
     */
    private var mSizeChangedListener =
        IMediaPlayer.OnVideoSizeChangedListener { mp, _, _, _, _ ->
            mVideoWidth = mp.videoWidth
            mVideoHeight = mp.videoHeight
            mVideoSarNum = mp.videoSarNum
            mVideoSarDen = mp.videoSarDen
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                mRenderView?.let {
                    mRenderView?.setVideoSize(mVideoWidth, mVideoHeight)
                    mRenderView?.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen)
                }
                requestLayout()
            }
        }

    /**
     * 完毕后
     */
    private var mCompletionListener = IMediaPlayer.OnCompletionListener {
        mCurrentState = STATE_PLAYBACK_COMPLETED
        mTargetState = STATE_PLAYBACK_COMPLETED
        mMediaController?.hide()
        mOnCompletionListener?.onCompletion(mMediaPlayer)
    }
    private val mSeekCompleteListener = IMediaPlayer.OnSeekCompleteListener { }
    private val mBufferingUpdateListener = IMediaPlayer.OnBufferingUpdateListener { _, percent ->
        mCurrentBufferPercentage = percent
    }

    private var mInfoListener = IMediaPlayer.OnInfoListener { mp, arg1, arg2 ->
        mOnInfoListener?.onInfo(mp, arg1, arg2)
        when (arg1) {
            IMediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING -> Log.d(
                TAG,
                "MEDIA_INFO_VIDEO_TRACK_LAGGING:"
            )
            IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> Log.d(
                TAG,
                "MEDIA_INFO_VIDEO_RENDERING_START:"
            )
            IMediaPlayer.MEDIA_INFO_BUFFERING_START -> Log.d(TAG, "MEDIA_INFO_BUFFERING_START:")
            IMediaPlayer.MEDIA_INFO_BUFFERING_END -> Log.d(TAG, "MEDIA_INFO_BUFFERING_END:")
            IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH -> Log.d(
                TAG,
                "MEDIA_INFO_NETWORK_BANDWIDTH: $arg2"
            )
            IMediaPlayer.MEDIA_INFO_BAD_INTERLEAVING -> Log.d(TAG, "MEDIA_INFO_BAD_INTERLEAVING:")
            IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE -> Log.d(TAG, "MEDIA_INFO_NOT_SEEKABLE:")
            IMediaPlayer.MEDIA_INFO_METADATA_UPDATE -> Log.d(TAG, "MEDIA_INFO_METADATA_UPDATE:")
            IMediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE -> Log.d(
                TAG,
                "MEDIA_INFO_UNSUPPORTED_SUBTITLE:"
            )
            IMediaPlayer.MEDIA_INFO_SUBTITLE_TIMED_OUT -> Log.d(
                TAG,
                "MEDIA_INFO_SUBTITLE_TIMED_OUT:"
            )
            IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED -> {
                mVideoRotationDegree = arg2
                Log.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: $arg2")
                if (mRenderView != null) mRenderView!!.setVideoRotation(arg2)
            }
            IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START -> Log.d(
                TAG,
                "MEDIA_INFO_AUDIO_RENDERING_START:"
            )
        }
        true
    }
    /**
     * error
     */
    private var mErrorListener = IMediaPlayer.OnErrorListener { _, framework_err, impl_err ->
        Log.d(TAG, "Error: $framework_err,$impl_err")

        mCurrentState = STATE_ERROR
        mTargetState = STATE_ERROR
        if (mOnErrorListener!!.onError(mMediaPlayer, framework_err, impl_err)) {
            return@OnErrorListener true
        }
        true


    }

    private fun releaseWithoutStop() {
        if (mMediaPlayer != null) mMediaPlayer!!.setDisplay(null)
    }

    private fun attachMediaController() {
        if (mMediaPlayer != null && mMediaController != null) {
            mMediaController?.setMediaPLayer(this)
            val anchorView = if (this.parent is View) this.parent as View else this
            mMediaController?.setAnchorView(anchorView)
            mMediaController?.isEnabled = isInPlaybackState()
        }
    }

    /**
     * 释放播放器
     */
    fun release(clearTargetState: Boolean) {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.reset()
            mMediaPlayer!!.release()
            mMediaPlayer = null
            mMediaController!!.hide()
            if (clearTargetState) {
                mTargetState = STATE_IDLE
            }
            setRenderView(null)
            initRenders()
            val am = mAppContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.abandonAudioFocus(null)
            cancelProgressRunnable()
        }
    }

    /**
     * 播放状态
     */
    fun isInPlaybackState(): Boolean {
        return mMediaPlayer != null
                && mCurrentState != STATE_ERROR
                && mCurrentState != STATE_IDLE
                && mCurrentState != STATE_PREPARING
    }

    /**
     * 获取controller
     */
    fun getMediaController(): IjkMediaController? {
        return mMediaController
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
     * 设置视频路径
     * @param path 视频路径
     */
    fun setVideoPath(path: String?) {
        setVideoURI(Uri.parse(path))
    }

    /**
     * 设置视频URI
     * @param uri 视频URI
     */
    private fun setVideoURI(uri: Uri) {
        setVideoURI(uri, null)
    }

    /**
     * 设置带header的视频URI
     * @param uri 视频URI
     * @param headers URI请求的header
     */
    private fun setVideoURI(uri: Uri, headers: Map<String, String>?) {
        mUri = uri
        mHeaders = headers
        mSeekWhenPrepared = 0
        openVideo()
        requestLayout()
        invalidate()
    }

    /**
     * 停止播放
     */
    fun stopPlayback() {
        mMediaPlayer?.let {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
            mMediaPlayer = null
            mCurrentState = STATE_IDLE
            mTargetState = STATE_IDLE
            mMediaController?.let {
                mMediaController?.hide()
                mMediaController = null
            }
            val am = mAppContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            am.abandonAudioFocus(null)
            cancelProgressRunnable()
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


    fun toggleMediaControlsVisible() {
        if (mMediaController!!.isShowing) {
            mMediaController?.hide()
        } else {
            mMediaController?.show()
        }
    }

    fun suspend() {
        release(false)
        cancelProgressRunnable()
    }

    fun resume() {
        openVideo()
    }

    fun setOnCompletionListener(l: IMediaPlayer.OnCompletionListener?) {
        mOnCompletionListener = l
    }

    fun setOnPreparedListener(l: IMediaPlayer.OnPreparedListener?) {
        mOnPreparedListener = l
    }

    fun setOnErrorListener(l: IMediaPlayer.OnErrorListener?) {
        mOnErrorListener = l
    }

    fun toggleAspectRatio(currentAspectRatio: Int): Int {
        mRenderView?.setAspectRatio(currentAspectRatio)
        mCurrentAspectRatio = currentAspectRatio
        return mCurrentAspectRatio
    }

    fun setMediaController(controller: IjkMediaController) {
        mMediaController = controller
        attachMediaController()
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

    override fun canPause() = mCanPause

    override fun onTrackballEvent(event: MotionEvent?): Boolean {
        if (isInPlaybackState() && mMediaController != null) {
            toggleMediaControlsVisible()
        }
        return false
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val isKeyCodeSupported =
            keyCode != KeyEvent.KEYCODE_BACK && keyCode != KeyEvent.KEYCODE_VOLUME_UP && keyCode != KeyEvent.KEYCODE_VOLUME_DOWN && keyCode != KeyEvent.KEYCODE_VOLUME_MUTE && keyCode != KeyEvent.KEYCODE_MENU && keyCode != KeyEvent.KEYCODE_CALL && keyCode != KeyEvent.KEYCODE_ENDCALL
        if (isInPlaybackState() && isKeyCodeSupported && mMediaController != null) {
            if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK ||
                keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
            ) {
                if (mMediaPlayer!!.isPlaying) {
                    pause()
                    mMediaController!!.show()
                } else {
                    start()
                    mMediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
                if (!mMediaPlayer!!.isPlaying) {
                    start()
                    mMediaController!!.hide()
                }
                return true
            } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE
            ) {
                if (mMediaPlayer!!.isPlaying) {
                    pause()
                    mMediaController!!.show()
                }
                return true
            } else {
                toggleMediaControlsVisible()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun createPlayer(): IMediaPlayer? {
        if (mUri != null) {
            return IjkMediaPlayer().apply {
                IjkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0)
                setOption(
                    IjkMediaPlayer.OPT_CATEGORY_PLAYER,
                    "overlay-format",
                    IjkMediaPlayer.SDL_FCC_RV32.toLong()
                )
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1)
                setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0)
                setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48)
            }
        }
        return null
    }
}
