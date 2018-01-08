package com.app.simon.ringinschool.utils

import android.content.Context
import com.app.simon.ringinschool.App

/**
 * desc: assets文件
 * date: 2018/1/8
 *
 * @author xw
 */
object MediaPlayerUtil {

    fun play(context: Context, musicPath: String): Unit {
        val assetManager = context.assets
        val music = assetManager.openFd(musicPath)
        App.mediaPlayer.setDataSource(music.fileDescriptor, music.startOffset, music.length)
        App.mediaPlayer.prepare()
        App.mediaPlayer.start()
    }

    /** 停止 */
    fun stop() {
        App.mediaPlayer.stop()
        App.mediaPlayer.release()
    }
}