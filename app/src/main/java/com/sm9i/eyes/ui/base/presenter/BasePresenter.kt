package com.sm9i.eyes.ui.base.presenter

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider


/**
 * presenter 基类
 *
 * LifecycleObserver android 生命周期
 */
open class BasePresenter<V> : LifecycleObserver {


    protected var mView: V? = null
    protected lateinit var mScopeProvider: AndroidLifecycleScopeProvider


    /**
     * 和view 关联
     */
    fun attachView(view: V, lifecycleOwner: LifecycleOwner) {
        this.mView = view
        this.mScopeProvider = AndroidLifecycleScopeProvider.from(lifecycleOwner)
    }

}