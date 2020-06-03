package com.sm9i.eyes.ui.author

import android.animation.ArgbEvaluator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sm9i.eyes.R
import com.sm9i.eyes.entiy.Tab
import com.sm9i.eyes.entiy.TabInfo
import com.sm9i.eyes.net.Extras
import com.sm9i.eyes.ui.author.presenter.AuthorPresenter
import com.sm9i.eyes.ui.author.view.AuthorView
import com.sm9i.eyes.ui.base.BaseActivity
import com.sm9i.eyes.ui.base.BaseFragmentItemAdapter
import com.sm9i.eyes.ui.feed.TagDetailInfoFragment
import com.sm9i.eyes.utils.showKeyBoard
import com.sm9i.eyes.widget.font.FontType
import kotlinx.android.synthetic.main.activity_author_tag_detail.*
import kotlinx.android.synthetic.main.layout_left_title_share_toolbar.*


/**
 * 用户详情页面
 */
@Route(path = "/pgc/detail")
class AuthorTagDetailActivity : BaseActivity<AuthorView, AuthorPresenter>(), AuthorView {
    //??
    @Autowired
    @JvmField
    var tabIndex: String? = null

    @Autowired
    @JvmField
    var title: String? = null

    @Autowired
    @JvmField
    var id: String? = null

    override fun getBundleExtras(extras: Bundle) {
        with(extras) {
            tabIndex = getString(Extras.TAB_INDEX)
            title = getString(Extras.TITLE)
            id = getString(Extras.ID)
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        initToolBar(title, 0f)
        mPresenter.getAuthorTagDetail(id!!)
    }

    override fun loadInfoSuccess(tab: Tab) {
        id_sticky_nav_layout_viewpager.adapter = BaseFragmentItemAdapter(
            supportFragmentManager,
            initFragments(tab.tabInfo),
            initTitles(tab.tabInfo)
        )
    }

    private fun initFragments(tabInfo: TabInfo): MutableList<Fragment> {
        val fragments = mutableListOf<Fragment>()
        for (i in tabInfo.tabList.indices) {
            fragments.add(TagDetailInfoFragment.newInstance(tabInfo.tabList[i].apiUrl))
        }
        return fragments
    }

    private fun initTitles(tabInfo: TabInfo): MutableList<String> {
        val titles = mutableListOf<String>()
        for (i in tabInfo.tabList.indices) {
            titles.add(tabInfo.tabList[i].name)
        }

        return titles
    }


    /**
     * 初始化toolBar
     */
    private fun initToolBar(title: String? = null, titleAlpha: Float = 1f) {
        findViewById<ImageView>(R.id.iv_back).setOnClickListener {
            showKeyBoard(false)
            finish()
        }
        val color = ArgbEvaluator().evaluate(titleAlpha, 0x00FFFFFF, Color.WHITE) as Int
        tool_bar.setBackgroundColor(color)
        tv_title.apply {
            setFontType(FontType.BOLD)
            text = title
            alpha = titleAlpha
        }
    }

    override fun getContentViewLayoutId() = R.layout.activity_author_tag_detail


}
