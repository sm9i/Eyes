package com.sm9i.eyes.widget.state

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.sm9i.eyes.R


/**
 * 多状态布局
 * 加载、网络异常、内容、空
 */
class MultipleStateView : RelativeLayout {


    private var mEmptyView: View? = null
    private var mNetErrorView: View? = null
    private var mLoadingView: View? = null
    private var mContentViews: MutableList<View> = mutableListOf()

    //页面状态
    enum class State {
        EMPTY, NET_ERROR, LOADING, CONTENT
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun addView(child: View?, params: ViewGroup.LayoutParams?) {
        super.addView(child, params)
        //判断tag 如果不属于 load empty error  就是content
        child?.let {
            if (child.tag != State.LOADING && child.tag != State.EMPTY && child.tag != State.NET_ERROR) {
                mContentViews.add(child)
            }
        }
    }

    fun showLoading() {
        switchContent(State.LOADING)
    }

    fun showEmpty(onClickListener: OnClickListener) {
        switchContent(State.EMPTY, onClickListener)
    }

    fun showNetError(onClickListener: OnClickListener) {
        switchContent(State.NET_ERROR, onClickListener)
    }

    fun showContent() {
        switchContent(State.CONTENT)
    }

    /**
     * 展示空view
     */
    private fun showEmptyView(onClickListener: OnClickListener) {
        setEmptyView(onClickListener)
        hideLoadView()
        hideNetErrorView()
        setContentViewVisible(false)
    }

    /**
     * 隐藏空view
     */
    private fun hideEmptyView() {
        mNetErrorView?.let { it.visibility = View.GONE }
    }

    /**
     * 展示加载view
     */
    private fun showLoadView(onClickListener: OnClickListener) {
        setLoadView(onClickListener)
        hideEmptyView()
        hideNetErrorView()
        setContentViewVisible(false)
    }

    /**
     * 隐藏加载view
     */
    private fun hideLoadView() {
        mLoadingView?.let { it.visibility = View.GONE }
    }

    /**
     * 展示网络异常view
     */
    private fun showNetErrorView(onClickListener: OnClickListener) {
        setNetErrorView(onClickListener)
        hideEmptyView()
        hideLoadView()
        setContentViewVisible(false)
    }

    /**
     * 隐藏网络异常view
     */
    private fun hideNetErrorView() {
        mNetErrorView?.let { it.visibility = View.GONE }
    }

    /**
     * 展示内容view
     */
    private fun showContentView() {
        hideEmptyView()
        hideNetErrorView()
        hideLoadView()
        setContentViewVisible(true)
    }

    /**
     * 设置空view
     */
    private fun setEmptyView(onClickListener: OnClickListener) {
        if (mEmptyView == null) {
            mEmptyView = LayoutInflater.from(context).inflate(R.layout.layout_loading_message, null)
            mEmptyView?.tag = State.EMPTY
            val imageView = mEmptyView?.findViewById<ImageView>(R.id.iv_image)
            val errorText = mEmptyView?.findViewById<TextView>(R.id.tv_message_info)
            imageView?.setImageResource(R.drawable.ic_eye_black_error)
            errorText?.setText(R.string.empty_message)
            mEmptyView?.setOnClickListener(onClickListener)
            addStateView(mEmptyView)
        } else {
            mEmptyView?.visibility = View.VISIBLE
        }
    }

    /**
     * 设置加载view
     */
    private fun setLoadView(onClickListener: OnClickListener) {


    }

    /**
     * 设置网络异常view
     */
    private fun setNetErrorView(onClickListener: OnClickListener) {

    }

    /**
     * 设置内容界面是否显示
     */
    private fun setContentViewVisible(isVisible: Boolean) {
        for (mContentView in mContentViews) {
            mContentView.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }


    /**
     * 设置布局状态
     */
    private fun addStateView(view: View?) {
        val layoutParams =
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.addRule(CENTER_IN_PARENT)
        addView(view, layoutParams)
    }

    /**
     * 切换页面布局
     * @param onClicker 可选参数 点击后的效果
     */
    private fun switchContent(state: State, onClicker: OnClickListener = OnClickListener { }) {
        when (state) {
            State.EMPTY -> showEmptyView(onClicker)
            State.NET_ERROR -> showNetErrorView(onClicker)
            State.LOADING -> showLoadView(onClicker)
            State.CONTENT -> showContentView()
        }
    }


}