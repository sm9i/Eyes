package com.sm9i.eyes.player

import android.app.Dialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.drawable.ColorDrawable
import android.media.AudioManager
import android.provider.Settings
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.facebook.drawee.view.SimpleDraweeView
import com.sm9i.eyes.R
import com.sm9i.eyes.player.view.ControllerViewFactory
import com.sm9i.eyes.utils.*
import kotlinx.android.synthetic.main.layout_ijk_wrapper.view.*
import tv.danmaku.ijk.media.player.IMediaPlayer
import kotlin.math.abs


class IjkVideoViewWrapper @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {


    private val mGestureDetector: GestureDetector
    private val mAudioManager: AudioManager

    private var mLightDialog: Dialog? = null
    private var mLightProgress: ProgressBar? = null
    private var mVolumeDialog: Dialog? = null
    private var mVolumeProgress: ProgressBar? = null

    private var isShowVolume = false
    private var isShowLight = false
    private var isShowPosition = false


    private val mScreenHeight: Int
    private var mParent: ViewGroup? = null

    companion object {
        private const val MIN_SCROLL = 3
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_ijk_wrapper, this, true)
        mGestureDetector = GestureDetector(context, this)
        mAudioManager = getContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mScreenHeight = getContext().getScreenHeight()

    }

    /**
     * 设置加载图
     */
    fun setPlaceImageUrl(url: String) {
        togglePlaceImage(true)
        iv_place_image.setImageURI(url)
    }

    private fun togglePlaceImage(visibility: Boolean) {
        iv_place_image.visibility = if (visibility) View.VISIBLE else View.GONE
        progress.visibility = if (visibility) View.VISIBLE else View.GONE
    }

    fun hidePlaceImage() {
        iv_place_image.handler.postDelayed({ togglePlaceImage(false) }, 500)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val relValue = mGestureDetector.onTouchEvent(event)
        val action = event.action
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            onUpCancel()
        }
        return relValue
    }

    private fun onUpCancel() {
        dismissLightDialog()
        dismissVolumeDialog()
    }

    /**
     * 隐藏声音dialog
     */
    private fun dismissVolumeDialog() {
        if (mVolumeDialog != null) {
            mVolumeDialog!!.dismiss()
            isShowVolume = false
        }
    }

    /**
     * 隐藏亮度dialog
     */
    private fun dismissLightDialog() {
        mLightDialog?.let {
            mLightDialog!!.dismiss()
            isShowLight = false
        }
    }


    override fun onShowPress(e: MotionEvent?) {
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        if (video_view.isInPlaybackState() && video_view.getMediaController() != null && (!isShowVolume || !isShowLight || isShowPosition)) {
            video_view.toggleMediaControlsVisible()
        }
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        return false
    }

    override fun onScroll(
        e1: MotionEvent,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        //判断是否是全屏
        if (video_view.screenState == ControllerViewFactory.FULL_SCREEN_MODE) {
            val downX = e1.x
            val actuallyDy = e2.y - e1.y
            val actuallyDx = e2.x - e1.x
            val absDy = abs(actuallyDy)
            val absDx = abs(actuallyDx)
            //左右移动
            if (absDx >= MIN_SCROLL && absDx > absDy) {
                showMoveToPositionDialog()
            }
            //上下移动
            if (abs(distanceY) >= MIN_SCROLL && absDy > absDx) {
                if (downX <= mScreenHeight * 0.5f) { //左边，改变声音
                    showVolumeDialog(distanceY)
                } else { //右边改变亮度
                    showLightDialog(distanceY)
                }
            }
        }
        return true
    }

    private fun showMoveToPositionDialog() {
        isShowPosition = true
    }

    /**
     * 显示亮度
     */
    private fun showLightDialog(deltaY: Float) {
        isShowLight = true
        var screenBrightness = 0f
        //记录滑动前的亮度
        val lp = getWindow(context)!!.attributes
        if (lp.screenBrightness < 0) {
            try {
                screenBrightness = Settings.System.getInt(
                    context.contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS
                ).toFloat()
            } catch (e: Settings.SettingNotFoundException) {
                e.printStackTrace()
            }
        } else {
            screenBrightness = lp.screenBrightness
        }
        if (deltaY < 0) { //向下滑动
            screenBrightness -= 0.03f
        } else { //向下滑动
            screenBrightness += 0.03f
        }
        when {
            screenBrightness >= 1 -> {
                lp.screenBrightness = 1f
            }
            screenBrightness < 0 -> {
                lp.screenBrightness = 0.1f
            }
            else -> {
                lp.screenBrightness = screenBrightness
            }
        }
        getWindow(context)!!.attributes = lp
        //设置亮度百分比
        if (mLightProgress == null) {
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_light_controller, null)
            mLightProgress = view.findViewById(R.id.pb_light_progress)
            mLightDialog = createDialogWithView(view, Gravity.END or Gravity.CENTER_VERTICAL)
        }
        if (!mLightDialog!!.isShowing) {
            mLightDialog?.show()
        }
        val lightPercent = (screenBrightness * 100f).toInt()
        mLightProgress?.progress = lightPercent
    }

    /**
     *显示声音对话框
     */
    private fun showVolumeDialog(deltaY: Float) {
        isShowVolume = true
        var currentVideoVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        if (deltaY < 0) {
            currentVideoVolume--
        } else {
            currentVideoVolume++
        }
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVideoVolume, 0)
        if (mVolumeDialog == null) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.dialog_volume_controller, null)
            mVolumeProgress = view.findViewById(R.id.pb_volume_progress)
            mVolumeDialog = createDialogWithView(view, Gravity.START or Gravity.CENTER_VERTICAL)
        }
        if (!mVolumeDialog!!.isShowing) {
            mVolumeDialog!!.show()
        }
        //设置进度条
        val nextVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        var volumePercent = (nextVolume * 1f / maxVolume * 100f).toInt()
        if (volumePercent > 100) {
            volumePercent = 100
        } else if (volumePercent < 0) {
            volumePercent = 0
        }
        mVolumeProgress!!.progress = volumePercent
    }

    private fun createDialogWithView(localView: View, gravity: Int): Dialog {
        return Dialog(context, R.style.VideoProgress).apply {
            setContentView(localView)
            window?.apply {
                addFlags(Window.FEATURE_ACTION_BAR)
                addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)
                addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
                setLayout(-2, -2)
                setBackgroundDrawable(ColorDrawable())
                val localLayoutParams = attributes
                localLayoutParams.gravity = gravity
                attributes = localLayoutParams
            }

        }
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    fun enterFullScreen() {
        hideActionBar(context)
        getActivity(context)!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        val contentView = getActivity(context)!!.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        mParent = parent as ViewGroup
        mParent!!.removeView(this)
        video_view.screenState = ControllerViewFactory.FULL_SCREEN_MODE
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.addView(this, params)
    }

    /**
     * 退出全屏
     */
    fun exitFullScreen() {
        showActionBar(context)
        val contentView = getActivity(context)!!.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
        getActivity(context)!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        contentView.removeView(this)
        video_view.screenState = ControllerViewFactory.TINY_MODE
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mParent!!.addView(this, params)
    }

    fun setVideoPath(playUrl: String) {
        video_view.setVideoPath(playUrl)
    }

    fun start() {
        video_view.start()
    }

    fun setMediaController(controller: IjkMediaController) {
        video_view.setMediaController(controller)
    }

    fun toggleAspectRatio(currentAspectRatio: Int) {
        video_view.toggleAspectRatio(currentAspectRatio)
    }

    fun setOnPreparedListener(onPreparedListener: IMediaPlayer.OnPreparedListener) {
        video_view.setOnPreparedListener(onPreparedListener)
    }

    fun setOnErrorListener(onErrorListener: IMediaPlayer.OnErrorListener) {
        video_view.setOnErrorListener(onErrorListener)
    }


    fun setOnCompletionListener(onCompletionListener: IMediaPlayer.OnCompletionListener) {
        video_view.setOnCompletionListener(onCompletionListener)
    }

    fun stopPlayback() {
        video_view.stopPlayback()
    }

    fun isPLaying(): Boolean {
        return video_view.isPlaying
    }

    fun pause() {
        video_view.pause()
    }

    fun release(clearTargetState: Boolean) {
        video_view.release(clearTargetState)
    }

    fun showErrorView(): Boolean {
        progress.handler.postDelayed(
            {
                progress.visibility = View.GONE
                video_view.getMediaController()!!.showErrorView()
            }, 1000
        )
        return false
    }

    fun resetType() {
        video_view.getMediaController()?.resetType()
    }

    fun setDragging(dragging: Boolean) {
        video_view.getMediaController()!!.controllerView!!.isDragging = dragging
    }

    fun isDragging(): Boolean {
        return video_view.getMediaController()!!.controllerView!!.isDragging
    }

    fun showControllerAllTheTIme() {
        video_view.getMediaController()?.showAllTheTime()
    }

    fun getDuration(): Int {
        return video_view.duration
    }

    fun showController() {
        video_view.getMediaController()!!.show()
    }

    fun seekTo(seek: Int) {
        video_view.seekTo(seek)
    }


}