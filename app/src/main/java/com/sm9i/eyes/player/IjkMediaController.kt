package com.sm9i.eyes.player

import android.app.Activity
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
    private lateinit var mAnchor: View//锚点view，??

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
     * 初始化数据
     * @param currentIndex 当前视频角标
     * @param totalCount 视频集合总数
     * @param currentVideoInfo 当前视频信息
     */
    fun initData(currentIndex: Int, totalCount: Int, currentVideoInfo: ContentBean?) {
        this.currentIndex = currentIndex
        this.totalCount = totalCount
        this.currentVideoInfo = currentVideoInfo
    }

    private fun updateFloatingWindowLayout() {
        val anchorPos = IntArray(2)
        mAnchor.getLocationOnScreen(anchorPos)
        //重新测
        decorView.measure(
            MeasureSpec.makeMeasureSpec(mAnchor.width, MeasureSpec.AT_MOST),
            MeasureSpec.makeMeasureSpec(mAnchor.height, MeasureSpec.AT_MOST)
        )
        decorLayoutParams.apply {
            width = mAnchor.width
            x = anchorPos[0]
            y = anchorPos[1]
            height = mAnchor.height
        }
    }

    fun setAnchorView(view: View) {
        mAnchor = view
        mAnchor.addOnLayoutChangeListener(mLayoutChangeListener)
        updateFloatingWindowLayout()
    }

    fun setMediaPLayer(player: MediaController.MediaPlayerControl) {
        this.player = player
        switchControllerView(mode)
    }

    /**
     * 当view布局发生变化  重新设置window布局
     */
    private val mLayoutChangeListener =
        OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            updateFloatingWindowLayout()
            if (isShowing) windowManager.updateViewLayout(decorView, decorLayoutParams)

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
            controllerListener?.onShowController(false)
            windowManager.removeView(decorView)
        } catch (e: IllegalArgumentException) {
            Log.w("MediaController", "already removed")
        }
    }

    fun updateProgressAndTime(event: VideoProgressEvent) {
        controllerView?.updateProgressAndTime(event!!)
    }

    /**
     * 一直显示window ???
     */
    fun showAllTheTime() {
        show(3600000)
        removeCallbacks(mFadeOut)
    }

    /**
     * 第一次展示，不显示控制层
     */
    fun firstShow() {
        if (isShowing && controllerView != null) {
            controllerView?.display()
        }
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
     * 展示错误布局
     */
    fun showErrorView() {
        if (!isShowing) {
            controllerView?.showErrorView()
            windowManager.addView(decorView, decorLayoutParams)
            isShowing = true
        }
    }

    fun resetType() {
        controllerView?.showContent()
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


    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val keyCode = event.keyCode
        val uniqueDown = (event.repeatCount == 0 && event.action == KeyEvent.ACTION_DOWN)
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide()
                if (context is Activity) (context as Activity).finish()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
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