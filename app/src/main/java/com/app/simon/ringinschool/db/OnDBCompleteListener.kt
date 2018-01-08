package com.app.simon.ringinschool.db

/**
 * desc: OnDBCompleteListener
 * date: 2017/9/25
 *
 * @author xw
 */
interface OnDBCompleteListener {
    fun onResult(results: List<Any>?)
    fun onSuccess()
    fun onError(throwable: Throwable)
}
