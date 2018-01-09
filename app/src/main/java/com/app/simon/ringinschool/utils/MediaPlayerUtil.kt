package com.app.simon.ringinschool.utils

import android.content.Context
import android.media.MediaPlayer
import com.app.simon.ringinschool.App

/**
 * desc: assets文件
 * date: 2018/1/8
 *
 * @author xw
 */
object MediaPlayerUtil {

    fun play(context: Context, musicPath: String) {
        val assetManager = context.assets
        val music = assetManager.openFd(musicPath)

        if (App.mediaPlayer == null) {
            App.mediaPlayer = MediaPlayer()
        }
        App.mediaPlayer?.apply {
            reset()
            setDataSource(music.fileDescriptor, music.startOffset, music.length)
            prepare()
            start()
        }
        //超过时间就停止
        //        Timer().schedule(object : TimerTask() {
        //            override fun run() {
        //                stop()
        //            }
        //        }, 1000 * 5)
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