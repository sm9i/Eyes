package com.sm9i.eyes.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.StringRes
import com.sm9i.eyes.R
import com.sm9i.eyes.manager.ActivityManager
import com.sm9i.eyes.utils.showKeyBoard
import com.sm9i.eyes.widget.font.CustomFontView
import com.sm9i.eyes.widget.font.FontType
import com.sm9i.eyes.widget.state.MultipleStateView
import me.yokeyword.fragmentation.SupportActivity


/**
 * baseActivity
 *  设置了 进出场动画
 *  工具栏  布局id
 */
abstract class BaseAppCompatActivity : SupportActivity() {

    protected lateinit var mContext: Context
    protected lateinit var mMultipleStateView: MultipleStateView

    /**
     * 进退场动画
     */
    enum class TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        ActivityManager.getInstance().addActivity(this)
    }

    private fun initData() {
        overrideTransitionAnimation()
        val extras = intent.extras
        if (extras != null) {
            getBundleExtras(extras)
        }
        mContext = this
        if (getContentViewLayoutId() != 0) {
            mMultipleStateView = MultipleStateView(this)
            val view = View.inflate(this, getContentViewLayoutId(), mMultipleStateView)
            setContentView(view)
        } else {
            throw  IllegalArgumentException("You must return layout id")
        }
    }


    /**
     * 设置进场动画
     */
    private fun overrideTransitionAnimation() {
        if (toggleOverridePendingTransition()) {
            when (getOverridePendingTransition()) {
                TransitionMode.TOP -> overridePendingTransition(R.anim.top_in, R.anim.no_anim)
                TransitionMode.BOTTOM -> overridePendingTransition(R.anim.bottom_in, R.anim.no_anim)
                TransitionMode.LEFT -> overridePendingTransition(R.anim.left_in, R.anim.no_anim)
                TransitionMode.RIGHT -> overridePendingTransition(R.anim.right_in, R.anim.no_anim)
                TransitionMode.FADE -> overridePendingTransition(R.anim.fade_in, R.anim.no_anim)
                TransitionMode.SCALE -> overridePendingTransition(R.anim.scale_in, R.anim.no_anim)
            }
        }
    }

    override fun finish() {
        super.finish()
        if (toggleOverridePendingTransition()) {
            when (getOverridePendingTransition()) {
                TransitionMode.TOP -> overridePendingTransition(0, R.anim.top_out)
                TransitionMode.BOTTOM -> overridePendingTransition(0, R.anim.bottom_out)
                TransitionMode.LEFT -> overridePendingTransition(0, R.anim.left_out)
                TransitionMode.RIGHT -> overridePendingTransition(0, R.anim.right_out)
                TransitionMode.FADE -> overridePendingTransition(0, R.anim.fade_out)
                TransitionMode.SCALE -> overridePendingTransition(0, R.anim.scale_out)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        ActivityManager.getInstance().removeActivity(this)
    }

    /**
     * 初始化工具栏，默认加粗
     * @param title 文本
     */
    protected fun initToolBar(
        toolbar: ViewGroup,
        title: String? = null,
        fontType: FontType = FontType.BOLD
    ) {
        val ivBack = toolbar.findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            showKeyBoard(false)
            finish()
        }
        val tvTitle = toolbar.findViewById<CustomFontView>(R.id.tv_title)
        tvTitle.setFontType(fontType)
        tvTitle.text = title
    }

    /**
     * 初始化工具栏，默认加粗
     * @param id 资源文件
     */
    protected fun initToolBar(
        toolbar: ViewGroup,
        @StringRes id: Int? = null,
        fontType: FontType = FontType.BOLD
    ) {
        val ivBack = toolbar.findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener {
            showKeyBoard(false)
            finish()
        }
        val tvTitle = toolbar.findViewById<CustomFontView>(R.id.tv_title)
        tvTitle.setFontType(fontType)
        tvTitle.setText(id!!)
    }


    /**
     * 初始化view
     */
    abstract fun initView(savedInstanceState: Bundle?)


    /**
     * 获取bundle 中的数据
     */
    open fun getBundleExtras(extras: Bundle) {}

    /**
     * 是否有进退场动画
     */
    protected open fun toggleOverridePendingTransition() = false


    /**
     * 进场动画模式
     */
    protected open fun getOverridePendingTransition(): TransitionMode? = null


    /**
     * 布局Id
     */
    abstract fun getContentViewLayoutId(): Int


}