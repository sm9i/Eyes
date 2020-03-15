package com.sm9i.eyes.utils

import com.sm9i.eyes.ui.base.model.BaseModel
import com.sm9i.eyes.ui.base.presenter.BasePresenter
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import kotlin.IllegalStateException


/**
 * 获取当前类上的泛型参数，并且实例化
 *
 * @param any 当前类
 * @param index 泛型参数下标
 * @param <T> 实例化对象
 * @return 泛型实力化对象
 *
 */
fun <T> getGenericInstance(any: Any, index: Int): T {

    try {
        val type = any.javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[index] as Class<T>
        val instance = clazz.newInstance()
        return if (instance is BasePresenter<*> || instance is BaseModel) {
            instance
        } else {
            throw IllegalStateException("if you use mvp user must support generic!!!")
        }

    } catch (e: Exception) {
        e.printStackTrace()
        throw  IllegalStateException("translate fail!!")
    }
}