package com.sm9i.eyes.ui.home.model

import android.util.Log
import com.sm9i.eyes.entiy.AndyInfo
import com.sm9i.eyes.net.Api
import com.sm9i.eyes.ui.base.model.BaseModel
import io.reactivex.Observable


class HomeModel : BaseModel {


    fun loadCategoryInfo() {
        Log.i("LOG", "net")
//        Log.i("dem", "${}")
        Api.getDefault().getHomeInfo().subscribe(
            {
                Log.i("LOG", "$it")
                Log.i("LOG", "${it.toString()}")
            }, {
                Log.i("LOG", "error $it")
            }
        )
    }


}