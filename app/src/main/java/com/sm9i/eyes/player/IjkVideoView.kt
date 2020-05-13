package com.sm9i.eyes.player

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import android.widget.MediaController
import com.sm9i.eyes.player.render.IRenderView
import java.util.*


class IjkVideoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MediaController.MediaPlayerControl {


    private lateinit var mAppContext: Context
    //渲染工具
    private var mCurrentRender = RENDER_NONE


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

            }
            RENDER_SURFACE_VIEW -> {
            }
            //没设置render
            else -> Log.e(TAG, String.format(Locale.getDefault(), "invalid render %d\n", render))
        }
    }

    /**
     * 设置渲染界面，初始化当前参数，设置回调等
     */
    private fun setRenderView(renderView: IRenderView?) {

    }


    override fun isPlaying(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canSeekForward(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDuration(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBufferPercentage(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun seekTo(pos: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentPosition(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canSeekBackward(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAudioSessionId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun canPause(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
