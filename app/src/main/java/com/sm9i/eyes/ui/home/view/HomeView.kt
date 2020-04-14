package com.sm9i.eyes.ui.home.view

import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.ui.base.BaseView


interface HomeView : BaseView {


    fun loadDataSuccess(andyInfo: AndyInfo)

    fun refreshDataSuccess(andyInfo: AndyInfo)

    fun loadMoreSuccess(andyInfo: AndyInfo)

    fun showNoMore()


}