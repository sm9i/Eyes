package com.sm9i.eyes.ui.search

import android.animation.ValueAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.Gravity
import android.view.animation.AccelerateInterpolator
import android.widget.RelativeLayout
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.BaseActivity
import com.sm9i.eyes.ui.search.adapter.SearchAdapter
import com.sm9i.eyes.ui.search.presenter.SearchPresenter
import com.sm9i.eyes.ui.search.view.SearchView
import com.sm9i.eyes.utils.dip2px
import com.sm9i.eyes.utils.showKeyBoard
import com.sm9i.eyes.widget.CenterAlignImageSpan
import kotlinx.android.synthetic.main.fragment_search_hot.*


class SearchHotActivity : BaseActivity<SearchView, SearchPresenter>(), SearchView {


    private lateinit var mHotSearchAdapter: SearchAdapter
    override fun initView(savedInstanceState: Bundle?) {
        initSearchView()
        tv_cancel.setOnClickListener { finish() }
        mPresenter.searchHot()
    }

    /**
     *初始化SearchView
     */
    private fun initSearchView() {
        search_view.isIconified = false
        search_view.setIconifiedByDefault(false)
        val searchComplete =
            search_view.findViewById<androidx.appcompat.widget.SearchView.SearchAutoComplete>(R.id.search_src_text)
        searchComplete.gravity = Gravity.CENTER
        searchComplete.setHintTextColor(resources.getColor(R.color.gray_66A2A2A2))
        searchComplete.textSize = 13f
        searchComplete.hint = getDecoratedHint(
            searchComplete.hint,
            getDrawable(R.drawable.ic_action_search_no_padding),
            50
        )
        search_view.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                showKeyBoard(false)
                mPresenter.searchVideoByWord(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    /**
     * 设置输入框提示文字
     */
    private fun getDecoratedHint(
        hintText: CharSequence,
        searchHintIcon: Drawable,
        drawableSize: Int
    ): CharSequence {
        searchHintIcon.setBounds(0, 0, drawableSize, drawableSize)
        val ssb = SpannableStringBuilder("   ")
        ssb.setSpan(CenterAlignImageSpan(searchHintIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.append(hintText)
        return ssb
    }


    override fun getHotWordSuccess(hotList: MutableList<String>) {
        setRecyclerMargin()
        startContentAnimation()
        mHotSearchAdapter = SearchAdapter(hotList)
        mHotSearchAdapter.setOnItemClickListener { _, _, position ->
            showKeyBoard(false)
            mPresenter.searchVideoByWord(mHotSearchAdapter.getItem(position)!!)
        }
        val flexBoxLayoutManager = FlexboxLayoutManager(mContext, FlexDirection.ROW)
        flexBoxLayoutManager.justifyContent = JustifyContent.CENTER
        rv_search_recycler.layoutManager = flexBoxLayoutManager
        rv_search_recycler.adapter = mHotSearchAdapter

    }

    private fun startContentAnimation() {
        val valueAnimator = ValueAnimator.ofInt(multiple_state_view.measuredHeight, 0).apply {
            duration = 500
            interpolator = AccelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Int
                multiple_state_view.scrollTo(0, value)
            }
            start()
        }

    }

    override fun showSearchSuccess(queryWord: String, andyInfo: AndyInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadMoreSuccess(data: AndyInfo) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNoMore() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * 重置RecyclerMargin
     */
    private fun resetRecyclerMargin() {
        val lp = rv_search_recycler.layoutParams as RelativeLayout.LayoutParams
        lp.marginEnd = 0
        lp.marginStart = 0
        rv_search_recycler.layoutParams = lp
    }

    /**
     * 设置RecyclerMargin
     */
    private fun setRecyclerMargin() {
        val lp = rv_search_recycler.layoutParams as RelativeLayout.LayoutParams
        lp.marginEnd = dip2px(30f)
        lp.marginStart = dip2px(30f)
        rv_search_recycler.layoutParams = lp
    }


    override fun toggleOverridePendingTransition() = true

    override fun getOverridePendingTransition() = TransitionMode.TOP

    override fun getContentViewLayoutId() = R.layout.fragment_search_hot


}