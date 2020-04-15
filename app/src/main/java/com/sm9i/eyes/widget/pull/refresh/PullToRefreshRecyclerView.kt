package com.sm9i.eyes.widget.pull.refresh

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sm9i.eyes.widget.pull.head.EliteHeaderView


/**
 * Author:  andy.xwt
 * Date:    2018/6/19 14:37
 * Description:下拉刷新RecyclerView
 */

class PullToRefreshRecyclerView : PullToRefreshBase<RecyclerView> {


    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun createRootView(context: Context): RecyclerView = RecyclerView(context)


    /**
     * 判断当前recyclerView是否滑动到顶部，如果是在顶部就可以进行下拉
     */
    override fun isReadyForPullStart(): Boolean {
        val adapter = mRootView.adapter
        return if (adapter == null || adapter.itemCount == 0) true else !mRootView.canScrollVertically(
            -1
        )

    }


    /**
     * 设置适配器与布局管理器
     */
    fun setAdapterAndLayoutManager(
        adapter: BaseQuickAdapter<*, *>,
        layoutManager: LinearLayoutManager
    ) {
        mRootView.adapter = adapter
        mRootView.layoutManager = layoutManager
        mRootView.itemAnimator = DefaultItemAnimator()
    }


    /**
     * 滚动到相应位置
     */
    fun smoothScrollToPosition(position: Int) {
        mRootView.smoothScrollToPosition(position)
    }

    /**
     * 添加滑动监听
     */
    fun addOnScrollListener(onScrollListener: RecyclerView.OnScrollListener) {
        mRootView.addOnScrollListener(onScrollListener)
    }

    override fun initRefreshView() = EliteHeaderView(context)


    override fun dispatchExtraPullEvent(dy: Float) {
        mRefreshView?.handleExtraPullEvent(dy)
    }

    override fun dispatchValidPullEvent(dy: Float) {
        mRefreshView?.handleValidPullEvent(dy)
    }


}