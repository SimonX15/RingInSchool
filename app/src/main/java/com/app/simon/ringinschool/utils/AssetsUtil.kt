package com.app.simon.ringinschool.utils

import android.content.Context
import android.media.MediaPlayer

/**
 * desc: assets文件
 * date: 2018/1/8
 *
 * @author xw
 */
object AssetsUtil {
    fun play(context: Context, musicPath: String): Unit {
        val assetManager = context.assets
        val music = assetManager.openFd(musicPath)
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(music.fileDescriptor, music.startOffset, music.length)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }
}