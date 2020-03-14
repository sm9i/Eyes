package com.sm9i.eyes.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * 是否展示键盘
 */
fun Activity.showKeyBoard(isSHow: Boolean) {

    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    if (isSHow) {
        if (currentFocus == null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            imm.showSoftInput(currentFocus, 0)
        }
    } else {
        if (currentFocus != null) {
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

}