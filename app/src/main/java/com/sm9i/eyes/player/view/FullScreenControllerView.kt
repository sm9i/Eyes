package com.sm9i.eyes.player.view

import android.content.Context
import android.view.View
import android.widget.SeekBar
import com.sm9i.eyes.R
import com.sm9i.eyes.utils.stringForTime
import kotlinx.android.synthetic.main.layout_media_controller_full_screen.view.*


/**
 * 全屏的controller
 *
 */
class FullScreenControllerView(context: Context) : ControllerView(context), View.OnClickListener {


    private var mChangeProgress = 0

    companion object {
        const val FULL_SCREEN_ID = 100
    }


    override val layoutId = R.layout.layout_media_controller_full_screen
    override fun initView() {
        initListener()
    }

    override fun initData() {
        //设置标题和时间
        tv_title.text = mCurrentVideoInfo!!.title
        val position = mPlayer.currentPosition
        val duration = mPlayer.duration
        tv_current_time.text = stringForTime(position)
        tv_end_time.text = stringForTime(duration)

        sb_progress.apply {
            max = 1000
            if (duration >= 0 && mPlayer.bufferPercentage > 0) {
                val firstProgress = 1000L * position / duration
                val secondProgress = mPlayer.bufferPercentage * 10
                progress = firstProgress.toInt()
                secondaryProgress = secondProgress
            }
            setPadding(0, 0, 0, 0)
        }
        updatePreNextButton()
        id = FULL_SCREEN_ID
    }

    /**
     * 设置上下一步按钮是否展示
     */
    private fun updatePreNextButton() {
        iv_previous.visibility = if (mController.isHavePreVideo()) View.VISIBLE else View.GONE
        iv_next.visibility = if (mController.isHaveNextVideo()) View.VISIBLE else View.GONE
    }


    /**
     * 设置按钮监听
     */
    private fun initListener() {
        iv_previous.setOnClickListener(this)
        iv_min_screen.setOnClickListener(this)
        iv_pause.setOnClickListener(this)
        iv_next.setOnClickListener(this)
        setOnClickListener(this)
        sb_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val duration = mPlayer.duration.toLong()
                    val newPosition = duration * progress / 1000L
                    mChangeProgress = progress
                    tv_current_time.text = stringForTime(newPosition.toInt())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //当一直在触摸时设置不消失
                isDragging = true
                mController.showAllTheTime()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val newPosition = mPlayer.duration * mChangeProgress / 1000L
                mPlayer.seekTo(newPosition.toInt())
                mController.show()
                isDragging = false
            }
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            FULL_SCREEN_ID -> mController.hide()
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
            R.id.iv_min_screen -> {
                mController.switchControllerView(ControllerViewFactory.TINY_MODE)
                mController.controllerListener?.onTinyScreenClick()
            }
        }
    }

    override fun updateProgress(progress: Int, secondaryProgress: Int) {
        sb_progress.progress = progress
        sb_progress.secondaryProgress = secondaryProgress
    }

    override fun updateTime(currentTime: String, endTime: String) {
        tv_current_time.text = currentTime
        tv_end_time.text = endTime
    }

    override fun updateTogglePauseUI(isPlaying: Boolean) {
        if (isPlaying) {
            iv_pause.setImageResource(R.drawable.ic_player_pause)
        } else {
            iv_pause.setImageResource(R.drawable.ic_player_play)
        }

    }

}