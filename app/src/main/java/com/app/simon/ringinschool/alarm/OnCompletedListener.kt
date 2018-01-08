package com.app.simon.ringinschool.alarm

/**
 * desc: 完成
 * date: 2018/1/8
 *
 * @author xw
 */
interface OnCompletedListener {
    fun addAtPosition(position: Int) {}
    fun updateAtPosition(from: Int, to: Int) {}
    fun deleteAtPosition(position: Int) {}
}