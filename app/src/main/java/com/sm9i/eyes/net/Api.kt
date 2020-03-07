package com.sm9i.eyes.net


object Api {


    /**
     * 主域名
     */
    val BASE_URL: String get() = "http://baobab.kaiyanapp.com/"

    /**
     * 获取retrofit 实例
     */
    fun getDefault() = RetrofitConfig.getDefaultService()
}