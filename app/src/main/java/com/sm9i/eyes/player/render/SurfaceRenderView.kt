package com.sm9i.eyes.player.render

import android.annotation.TargetApi
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.sm9i.eyes.player.MeasureHelper
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.ISurfaceTextureHolder
import java.lang.ref.WeakReference
import java.util.concurrent.ConcurrentHashMap


/**
 * SurfaceView 渲染的视频播放界面
 */
class SurfaceRenderView : SurfaceView, IRenderView {

    private lateinit var mMeasureHelper: MeasureHelper
    private lateinit var mSurfaceCallback: SurfaceCallback

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        mMeasureHelper = MeasureHelper(this)
        mSurfaceCallback = SurfaceCallback(this)
        holder.addCallback(mSurfaceCallback)
        holder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL)
    }

    override fun getView() = this

    override fun shouldWaitForResize() = true

    override fun setVideoSize(videoWidth: Int, videoHeight: Int) {
        if (videoWidth > 0 && videoHeight > 0) {
            mMeasureHelper.setVideoSize(videoWidth, videoHeight)
            holder.setFixedSize(videoWidth, videoHeight)
            requestLayout()
        }
    }

    override fun setVideoSampleAspectRatio(videoSarNum: Int, videoSarDen: Int) {
        if (videoSarNum > 0 && videoSarDen > 0) {
            mMeasureHelper.setVideoSampleAspectRatio(videoSarNum, videoSarDen)
            requestLayout()
        }
    }

    override fun setVideoRotation(degree: Int) {
        Log.e("", "SurfaceView doesn't support rotation ($degree)!\n")
    }

    override fun setAspectRatio(aspectRation: Int) {
        mMeasureHelper.setAspectRatio(aspectRation)
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mMeasureHelper.doMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mMeasureHelper.measuredWidth, mMeasureHelper.measuredHeight)
    }

    override fun addRenderCallback(callBack: IRenderView.IRenderCallBack) {
        mSurfaceCallback.addRenderCallback(callBack)
    }

    override fun removeRenderCallback(callBack: IRenderView.IRenderCallBack) {
        mSurfaceCallback.removeRenderCallback(callBack)
    }

    //Accessibility 辅助功能？
    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.className = SurfaceRenderView::class.java.name
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            info.className = SurfaceRenderView::class.java.name
        }
    }


    //callback
    class SurfaceCallback(surfaceView: SurfaceRenderView) : SurfaceHolder.Callback {
        private var mSurfaceHolder: SurfaceHolder? = null
        private var mIsFormatChanged = false
        private var mFormat = 0
        private var mWidth = 0
        private var mHeight = 0
        private val mWeakSurfaceView: WeakReference<SurfaceRenderView> = WeakReference(surfaceView)
        private val mRenderCallbackMap: MutableMap<IRenderView.IRenderCallBack, Any> =
            ConcurrentHashMap()

        fun addRenderCallback(callback: IRenderView.IRenderCallBack) {
            mRenderCallbackMap[callback] = callback
            var surfaceHolder: IRenderView.ISurfaceHolder? = null
            if (mSurfaceHolder != null) {
                if (surfaceHolder == null) surfaceHolder =
                    InternalSurfaceHolder(mWeakSurfaceView.get()!!, mSurfaceHolder)
                callback.onSurfaceCreated(surfaceHolder, mWidth, mHeight)
            }
            if (mIsFormatChanged) {
                if (surfaceHolder == null) surfaceHolder =
                    InternalSurfaceHolder(mWeakSurfaceView.get()!!, mSurfaceHolder)
                callback.onSurfaceChanged(surfaceHolder, mFormat, mWidth, mHeight)
            }
        }

        fun removeRenderCallback(callback: IRenderView.IRenderCallBack) {
            mRenderCallbackMap.remove(callback)
        }

        override fun surfaceCreated(holder: SurfaceHolder?) {
            mSurfaceHolder = holder
            mIsFormatChanged = false
            mFormat = 0
            mWidth = 0
            mHeight = 0
            val surfaceHolder: IRenderView.ISurfaceHolder =
                InternalSurfaceHolder(mWeakSurfaceView.get()!!, mSurfaceHolder)
            for (renderCallback in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
            mSurfaceHolder = holder
            mIsFormatChanged = false
            mFormat = 0
            mWidth = 0
            mHeight = 0
            //调用监听
            val surfaceHolder: IRenderView.ISurfaceHolder =
                InternalSurfaceHolder(mWeakSurfaceView.get()!!, mSurfaceHolder)
            for (renderCallback in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceCreated(surfaceHolder, 0, 0)
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            mSurfaceHolder = null
            mIsFormatChanged = false
            mFormat = 0
            mWidth = 0
            mHeight = 0
            val surfaceHolder: IRenderView.ISurfaceHolder =
                InternalSurfaceHolder(mWeakSurfaceView.get()!!, mSurfaceHolder)
            for (renderCallback in mRenderCallbackMap.keys) {
                renderCallback.onSurfaceDestroyed(surfaceHolder)
            }
        }


    }

    //SurfaceViewHolder
    class InternalSurfaceHolder(
        private val surfaceView: SurfaceRenderView,
        override val surfaceHolder: SurfaceHolder?
    ) :
        IRenderView.ISurfaceHolder {


        override fun bindToMediaPlayer(mp: IMediaPlayer?) {
            if (mp != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && mp is ISurfaceTextureHolder) {
                    val textureHolder = mp as ISurfaceTextureHolder
                    textureHolder.surfaceTexture = null
                }
                mp.setDisplay(surfaceHolder)
            }
        }

        override val renderView: IRenderView
            get() = surfaceView

        override fun openSurface(): Surface? {
            return surfaceHolder?.surface
        }

        override val surfaceTexture: SurfaceTexture?
            get() = null
    }

}