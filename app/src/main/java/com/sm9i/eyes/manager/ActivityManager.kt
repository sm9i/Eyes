package com.sm9i.eyes.manager

import android.app.Activity
import kotlin.math.sign


/**
 * activity 管理类， 私有构造
 */
class ActivityManager private constructor() {
    private val mActivityList = ArrayList<Activity>()

    companion object {
        fun getInstance(): ActivityManager {
            return ActivityFactory.instance
        }
    }

    private object ActivityFactory {
        val instance = ActivityManager()
    }

    /**
     * 获取当前activity个数
     */
    val mActivitySize: Int get() = mActivityList.size

    /**
     * 获取栈顶activity？
     */
    val mForwardActivity: Activity? @Synchronized get() = if (mActivitySize > 0) mActivityList[mActivitySize - 1] else null

    /**
     * 移除activity
     */
    @Synchronized
    fun removeActivity(activity: Activity) {
        if (activity in mActivityList) mActivityList.remove(activity)
    }

    /**
     * 添加activity
     */
    @Synchronized
    fun addActivity(activity: Activity) {
        mActivityList.add(activity)
    }

    /**
     * 清除栈内activity
     */
    @Synchronized
    fun clearList() {
        var i = mActivityList.size - 1
        while (i > -1) {
            val activity = mActivityList[i]
            removeActivity(activity)
            activity.finish()
            i = mActivitySize
            i--
        }
    }

    /**
     * 清除栈顶activity
     */
    @Synchronized
    fun clearTop() {
        var i = mActivityList.size - 2
        while (i > -1) {
            val activity = mActivityList[i]
            removeActivity(activity)
            activity.finish()
            i = mActivitySize - 1
            i--
        }
    }

    /**
     * 获取最上层的activity
     */
    @Synchronized
    fun getTopActivity(): Activity? {
        return if (mActivityList.isNotEmpty()) {
            mActivityList[mActivitySize - 1]
        } else {
            null
        }
    }


}