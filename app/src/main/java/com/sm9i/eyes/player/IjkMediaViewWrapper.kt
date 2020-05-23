package com.sm9i.eyes.player

import android.app.Dialog
import android.content.Context
import android.media.AudioManager
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.facebook.drawee.view.SimpleDraweeView
import com.sm9i.eyes.R
import com.sm9i.eyes.player.view.ControllerViewFactory
import com.sm9i.eyes.utils.getScreenHeight
import kotlinx.android.synthetic.main.layout_ijk_wrapper.view.*
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        if(mVolumeDialog==null){
            val view =LayoutInflater.from(context).inflate()
        }
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}