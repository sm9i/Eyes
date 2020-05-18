package com.sm9i.eyes.player

import android.content.Context
import android.graphics.PixelFormat
import android.media.AudioManager
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.MediaController
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.ContentBean
import com.sm9i.eyes.event.VideoProgressEvent
import com.sm9i.eyes.player.view.ControllerView
import com.sm9i.eyes.player.view.ControllerViewFactory


/**
 *  用于视频播放的UI
 *  ，内部原理是通过window进行展示，其中window的位置与大小是根据[mAnchor]的位置动态调整的。
 */
class IjkMediaController(context: Context) : FrameLayout(context) {

    private lateinit var player: MediaController.MediaPlayerControl

    private lateinit var decorLayoutParams: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var window: Window
    private lateinit var decorView: View
    var controllerListener: ControllerListener? = null
    var controllerView: ControllerView? = null
    var isShowing = false//当前window是否展示

    var currentVideoInfo: ContentBean? = null
    //用来判断是否有下一个和上一页
    var totalCount = 0
    var currentIndex = 0
    //当前展示的模式，默认是小屏
    private var mode = ControllerViewFactory.TINY_MODE

    private val mControllerViewFactory = ControllerViewFactory()

    companion object {
        //默认window消失时间
        private const val WINDOW_TIME_OUT = 3500L
    }

    init {
        initFloatingWindowLayout()
        initFloatingWindowConfig()
    }

    /**
     * 初始化window布局
     */
    private fun initFloatingWindowLayout() {
        decorLayoutParams = WindowManager.LayoutParams().apply {
            gravity = Gravity.TOP or Gravity.LEFT
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            x = 0
            format = PixelFormat.TRANSLUCENT
            type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            flags =
                flags or (WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        or WindowManager.LayoutParams.FLAG_SPLIT_TOUCH)
            token = null
            windowAnimations = 0
        }


    }

    private fun initFloatingWindowConfig() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        window = PolicyCompat().createPhoneWindow(context).apply {
            setWindowManager(windowManager, null, null)
            requestFeature(Window.FEATURE_NO_TITLE)
            setContentView(this@IjkMediaController)
            setBackgroundDrawableResource(R.color.transparent)
            volumeControlStream = AudioManager.STREAM_MUSIC
        }
        decorView = window.decorView
        decorView.setOnTouchListener(mTouchListener)
        isFocusable = true
        isFocusableInTouchMode = true
        descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
        requestFocus()
    }

    /**
     * 隐藏控制view
     */
    private val mFadeOut = Runnable {
        if (isShowing) hide()
    }

    /**
     * 隐藏
     */
    fun hide() {
        try {
            removeCallbacks(mFadeOut)
            isShowing = false


        } catch (e: IllegalArgumentException) {
            Log.w("MediaController", "already removed")
        }
    }

    fun updateProgressAndTime(event: VideoProgressEvent) {
        controllerListener

    }

    /**
     * 一直显示window ???
     */
    fun showAllTheTime() {
        show(3600000)
        removeCallbacks(mFadeOut)
    }


    /**
     * 将控制器显示在屏幕上，当到达时间会自动消失
     * @param timeOut 如果 ==0 必须调动hide
     */
    @JvmOverloads
    fun show(timeOut: Long = WINDOW_TIME_OUT) {
        if (!isShowing) {
            windowManager.addView(decorView, decorLayoutParams)
            isShowing = true
        }
        if (timeOut != 0L) {
            removeCallbacks(mFadeOut)
            postDelayed(mFadeOut, timeOut)
        }
        controllerView?.display()
        controllerListener?.onShowController(true)

    }


    private val mTouchListener = OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isShowing) {
                removeCallbacks(mFadeOut)
                postDelayed(mFadeOut, WINDOW_TIME_OUT)
            }
        }
        false
    }


    /**
     * 是否有上个视频
     */
    fun isHavePreVideo() = totalCount > 0 && currentIndex > 0

    /**
     * 是否有下一个视频
     */
    fun isHaveNextVideo() = totalCount > 0 && currentIndex < totalCount

    /**
     * 根据现实模式切换ui
     */
    fun switchControllerView(mode: Int) {
        this.mode = mode
        removeAllViews()
        controllerView = mControllerViewFactory.create(this.mode, context)
        controllerView?.initControllerAndData(player, this, currentVideoInfo)
        controllerView?.display()
        addView(controllerView)
    }

    //页面按钮控制接口
    interface ControllerListener {
        //退出
        fun onBackClick()

        // 上一页
        fun onPreCLick()

        //下一页
        fun onNextClick()

        //全屏
        fun onFullScreenClick()

        //退出全屏
        fun onTinyScreenClick()

        //错误点击
        fun onErrorViewClick()

        //是否显示控制层
        fun onShowController(isShowController: Boolean)

        //双击
        fun onDoubleTap()

    }


}