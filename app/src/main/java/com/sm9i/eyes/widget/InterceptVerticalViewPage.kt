package com.sm9i.eyes.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.viewpager.widget.ViewPager
import kotlin.math.abs

/**
 * 拦截向上和左右滑动
 *
 */
class InterceptVerticalViewPage : ViewPager {

    private var mLastMotionX: Float = 0f
    private var mLastMotionY: Float = 0f
    //上下左右回调？
    lateinit var verticalListener: () -> Unit
    lateinit var horizontalListener: (Int) -> Unit
    private var isDoListener: Boolean = false

    //后续用到
    var mDisMissIndex = -1

    //触发移动事件的最小距离
    //有的时候需要判断用户是否真的存在movie，系统提供了这样的方法。
    // 表示滑动的时候，手的移动要大于这个返回的距离值才开始移动控件。
    private val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        // ev.action 可以处理DOWN 和UP 事件
        // ev.action &  MotionEvent.ACTION_MASK 可以处理多点触摸 ACTION_POINTER_DOWN和 ACTION_POINTER_UP
        // 当一只手按下去 另一只手的 按下和抬起

        val action = ev.action and MotionEvent.ACTION_MASK
        val y = ev.y
        val x = ev.x
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //按下的时候记录点
                mLastMotionX = ev.x
                mLastMotionY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                //获取按下和当前位置的差
                val dx = mLastMotionX - x
                val dy = mLastMotionY - y
                //获取移动的绝对值
                val absX = abs(dx)
                val absY = abs(dy)

                //向上滑动  并且 y的坐标大于x的滑动的一半？
                //dy>0 向上滑动
                // absY > absX * 0.5F 上下移动的大于左右的一半
                //absY > scaledTouchSlop  真的是在滑动  移动距离大于这个 返回值
                if (dy > 0 && absY > absX * 0.5F && absY > scaledTouchSlop) {
                    if (!isDoListener) {
                        //启动回调
                        verticalListener()
                        //设置监听已经被调用
                        isDoListener = true
                        //true 继续传递  返回false 不再接受 up 和move
                        return false
                    }
                }
                //像左滑动，且x轴坐标的距离大于y轴滑动距离的一半
                if (dx > 0 && absX > absY * 0.5F && absX > scaledTouchSlop) {
                    //没有回调 并且   -1==0？？
                    if (!isDoListener && mDisMissIndex == currentItem) {
                        horizontalListener(mDisMissIndex)
                        isDoListener = true
                        return false
                    }
                }
                mLastMotionX = x
                mLastMotionY = y

            }
        }
        return super.dispatchTouchEvent(ev)
    }

}