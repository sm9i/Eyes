package com.sm9i.eyes.ui.home.presenter

import com.sm9i.eyes.ui.base.presenter.BasePresenter
import com.sm9i.eyes.ui.home.model.HomeModel
import com.sm9i.eyes.ui.home.view.HomeView


class HomePresenter : BasePresenter<HomeView>() {

    private var mHomeModel: HomeModel = HomeModel()

    /**
     * 加载首页信息
     */
    fun loadCategoryData() {
        mHomeModel.loadCategoryInfo()
    }

}