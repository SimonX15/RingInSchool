package com.app.simon.ringinschool.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.models.AlarmType
import java.io.File

/**
 * desc: 音乐播放
 * date: 2018/1/8
 *
 * @author xw
 */
object MediaPlayerUtil {
    private val TAG = MediaPlayerUtil::class.java.simpleName

    /** 播放音乐 */
    fun play(context: Context, alarmType: Int) {
        App.ring.apply {
            val musicPath = when (alarmType) {
                AlarmType.TYPE_START -> {
                    startMusic?.path
                }
                AlarmType.TYPE_END -> {
                    endMusic?.path
                }
                else -> {
                    graceMusic?.path
                }
            }
            Log.i(TAG, "musicPath: $musicPath")
            //assets
            if (musicPath!!.startsWith("assets/")) {
                playAssetsMusic(context, musicPath.substringAfter("/"))
                //                Log.i(TAG, "musicPath: ${musicPath.substringAfter("/")}")
            }
            //本地音乐
            else {
                playExternalMusic(musicPath)
            }

            //超过时间就停止
            //        Timer().schedule(object : TimerTask() {
            //            override fun run() {
            //                stop()
            //            }
            //        }, 1000 * 5)
        }
    }

    /** 播放本地音乐 */
    private fun playExternalMusic(musicPath: String) {
        //判断文件是否存在
        val file = File(musicPath)
        if (!file.exists()) {
            return
        }

        if (App.mediaPlayer == null) {
            App.mediaPlayer = MediaPlayer()
        }
        App.mediaPlayer!!.apply {
            reset()
            setDataSource(musicPath)
            prepare()
            start()
        }
    }

    /** 播放内置音乐 */
    private fun playAssetsMusic(context: Context, musicPath: String) {
        if (App.mediaPlayer == null) {
            App.mediaPlayer = MediaPlayer()
        }

        val assetManager = context.assets
        val music = assetManager.openFd(musicPath)

        App.mediaPlayer!!.apply {
            reset()
            setDataSource(music.fileDescriptor, music.startOffset, music.length)
            prepare()
            start()
        }
    }

    /** 停止 */
    fun stop() {
        App.mediaPlayer?.apply {
            stop()
            release()
        }
        App.mediaPlayer = null
    }
}