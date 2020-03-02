package com.sm9i.eyes

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit


/**
 * 配置本地的用户信息 sp
 * object 实现单例
 */
object UserPreferences {


    //sp 名
    private const val NAME = "eyes."
    //用户是否登录
    private const val KEY_IS_USER_LOGIN = "is_user_login"
    //用户是否第一次登录
    private const val KEY_IS_FIRST_LOGIN = "is_first_login"
    //是否展示动画？
    private const val KEY_IS_SHOW_USER_ANIM = "key_is_show_user_anim"

    /**
     * 保存用户是否登录
     */
    fun saveUserIsLogin(isUserLogin: Boolean) {
        getSharedPreferences().edit {
            putBoolean(KEY_IS_USER_LOGIN, isUserLogin)
        }
    }

    /**
     * 保存是否向用户展示动画
     */
    fun saveShowUserAnim(isUserShowAnim: Boolean) {
        getSharedPreferences().edit {
            putBoolean(KEY_IS_SHOW_USER_ANIM, isUserShowAnim)
        }
    }

    /**
     * 保存用户是否第一次登录
     */
    fun saveUserIsFirstLogin(isFirstLogin: Boolean) {
        getSharedPreferences().edit {
            putBoolean(KEY_IS_FIRST_LOGIN, isFirstLogin)
        }
    }

    /**
     * 获取用户是否登录
     */
    fun getUserIsLogin() = getSharedPreferences().getBoolean(KEY_IS_USER_LOGIN, false)

    /**
     * 获取是否向用户展示过动画
     */
    fun getShowUserAnim() = getSharedPreferences().getBoolean(KEY_IS_SHOW_USER_ANIM, false)

    /**
     * 获取用户是否第一次登录
     *
     */
    fun getUserIsFirstLogin() = getSharedPreferences().getBoolean(KEY_IS_FIRST_LOGIN, true)

    /**
     * 获取sp对象
     */
    private fun getSharedPreferences(): SharedPreferences =
        App.INSTANCE.getSharedPreferences(NAME, Context.MODE_PRIVATE)


}