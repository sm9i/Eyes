package com.sm9i.eyes.player.view

import android.content.Context


class ControllerViewFactory {

    companion object {
        const val TINY_MODE = 0
        const val FULL_SCREEN_MODE = 1
    }

    fun create(mode: Int, context: Context): ControllerView {
        return when (mode) {
            TINY_MODE -> TinyControllerView(context)
            FULL_SCREEN_MODE -> FullScreenControllerView(context)
            else -> throw IllegalArgumentException("not correct showMode")
        }
    }

}