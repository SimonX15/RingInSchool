package com.app.simon.ringinschool.utils

import android.app.Service
import android.content.Context
import android.media.AudioManager
import android.os.Vibrator


/**
 * desc: 服务
 * date: 2018/1/15
 *
 * @author xw
 */
object ServiceUtil {

    /** 音量管理 */
    var audioManager: AudioManager? = null
    /** 当前音量 */
    var currentVolume = 0

    /** 设置音量为最大 */
    fun setAudioVolumeMax(context: Context) {
        if (audioManager == null) {
            audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        audioManager?.apply {
            currentVolume = getStreamVolume(AudioManager.STREAM_MUSIC)
            //            val maxVolume = getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            setStreamVolume(AudioManager.STREAM_MUSIC, getStreamMaxVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND)
        }
    }

    /** 重置为之前的音量大小 */
    fun resetAudioVolume(context: Context) {
        if (audioManager == null) {
            audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        audioManager?.apply {
            setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, AudioManager.FLAG_PLAY_SOUND)
        }
    }

    /** 震动milliseconds毫秒 */
    fun vibrate(context: Context, milliseconds: Long) {
        val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vib.vibrate(milliseconds)
    }


    /** 取消震动 */
    fun virateCancle(context: Context) {
        val vib = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vib.cancel()
    }
}