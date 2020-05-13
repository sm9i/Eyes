package com.sm9i.eyes.player

import android.content.Context
import android.graphics.PixelFormat
import android.media.AudioManager
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import com.sm9i.eyes.R
import com.sm9i.eyes.event.VideoProgressEvent


/**
 *  用于视频播放的UI
 *  ，内部原理是通过window进行展示，其中window的位置与大小是根据[mAnchor]的位置动态调整的。
 */
class IjkMediaController(context: Context) : FrameLayout(context) {

    private lateinit var decorLayoutParams: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    private lateinit var window: Window
    private lateinit var decorView: View
    var controllerListener: ControllerListener? = null
    var controllerView: ControllerView? = null
    var isShowing = false//当前window是否展示

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

    private fun hide() {
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

    private val mTouchListener = OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            if (isShowing) {
                removeCallbacks(mFadeOut)
                postDelayed(mFadeOut, WINDOW_TIME_OUT)
            }
        }
        false
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