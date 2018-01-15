package com.app.simon.ringinschool.utils

import java.util.*

/**
 * desc: TimerUtil
 * date: 2018/1/15
 *
 * @author xw
 */
object TimerUtil {

    /** 简写timerTask */
    fun timerTaskRun(onTimerTask: OnTimerTask, delay: Long) {
        Timer().schedule(object : TimerTask() {
            override fun run() {
                onTimerTask.taskRun()
            }
        }, delay)
    }

    interface OnTimerTask {
        fun taskRun()
    }
}