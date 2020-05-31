package com.sm9i.eyes.ui.feed

import android.os.Bundle
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.net.Extras
import com.sm9i.eyes.ui.base.BaseFragment
import com.sm9i.eyes.ui.feed.presenter.TagDetailInfoPresenter
import com.sm9i.eyes.ui.feed.view.TagDetailInfoView


class TagDetailInfoFragment : BaseFragment<TagDetailInfoView, TagDetailInfoPresenter>(),
    TagDetailInfoView {

    private lateinit var mApiUrl: String

    companion object {

        @JvmStatic
        fun newInstance(apiUrl: String): TagDetailInfoFragment {
            val categoryFragment = TagDetailInfoFragment()
            val bundle = Bundle()
            bundle.putString(Extras.API_URL, apiUrl)
            categoryFragment.arguments = bundle
            return categoryFragment
        }
    }


    override fun getBundleExtras(extras: Bundle) {
        mApiUrl = extras.getString(Extras.API_URL)
    }

    /**
     * 懒加载 ，tab 多的情况下 每个页面请求大量数据
     * [onLazyInitView] 可在可视情况下去加载
     */
    override fun onLazyInitView(savedInstanceState: Bundle?) {
    }

    override fun showGetTabInfoSuccess(andyInfo: AndyInfo) {
    }

    override fun getContentViewLayoutId() = R.layout.fragment_tag_detail_info


}