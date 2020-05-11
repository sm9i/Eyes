package com.sm9i.eyes.utils

import android.content.Context
import android.os.Handler
import android.widget.Toast

import com.sm9i.eyes.App


/**
 * Author:  andy.xwt
 * Date:    2019-11-17 22:43
 * Description:
 */


fun toast(value: () -> String) = App.INSTANCE.toast(value)

fun toast(value: String) = toast { value }

inline fun Context.toast(crossinline value: () -> String, duration: Int = Toast.LENGTH_SHORT) {
    Handler(mainLooper).post { Toast.makeText(this, value(), duration).show() }
}
