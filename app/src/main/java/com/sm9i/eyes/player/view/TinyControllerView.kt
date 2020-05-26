package com.sm9i.eyes.player.view

import android.content.Context
import android.view.View
import com.sm9i.eyes.R
import com.sm9i.eyes.utils.stringForTime
import kotlinx.android.synthetic.main.layout_media_controller_tiny.view.*


/**
 * 小屏幕controller
 */
class TinyControllerView(context: Context) : ControllerView(context), View.OnClickListener {

    override val layoutId = R.layout.layout_media_controller_tiny
    override fun initView() {
        initListener()
    }

    private fun initListener() {
        iv_previous.setOnClickListener(this)
        iv_pause.setOnClickListener(this)
        iv_back.setOnClickListener(this)
        iv_full_screen.setOnClickListener(this)
        iv_next.setOnClickListener(this)

    }

    override fun initData() {
        val position = mPlayer.currentPosition
        val duration = mPlayer.duration
        tv_current_time.text = stringForTime(position)
        tv_end_time.text = "/${stringForTime(duration)}"
        updatePreNextButton()
    }

    /**
     * 判断是否显示上下按钮
     */
    private fun updatePreNextButton() {
        iv_previous.visibility = if (mController.isHavePreVideo()) View.VISIBLE else View.GONE
        iv_next.visibility = if (mController.isHaveNextVideo()) View.VISIBLE else View.GONE
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_pause -> {
                togglePause()
                mController.show()
            }
            R.id.iv_previous -> {
                mController.controllerListener?.onPreCLick()
                updatePreNextButton()
            }
            R.id.iv_next -> {
                mController.controllerListener?.onNextClick()
                updatePreNextButton()
            }
            R.id.iv_back -> {
                mController.hide()
                mController.controllerListener?.onBackClick()
            }
            R.id.iv_full_screen -> {
                mController.switchControllerView(ControllerViewFactory.FULL_SCREEN_MODE)
                mController.controllerListener?.onFullScreenClick()
            }
        }
    }

    override fun updateTime(currentTime: String, endTime: String) {
        tv_current_time.text = currentTime
        tv_end_time.text = "/$endTime"
    }

    override fun updateTogglePauseUI(isPlaying: Boolean) {
        if (isPlaying) {
            iv_pause.setImageResource(R.drawable.ic_player_pause)
        } else {
            iv_pause.setImageResource(R.drawable.ic_player_play)
        }
    }
}