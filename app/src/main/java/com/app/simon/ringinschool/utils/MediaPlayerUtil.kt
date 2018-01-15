package com.app.simon.ringinschool.utils

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.alarm.models.AlarmType
import com.app.simon.ringinschool.ring.models.Music
import org.jetbrains.anko.toast
import java.io.File
import java.util.*

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
            val music = when (alarmType) {
                AlarmType.TYPE_START -> {
                    startMusic
                }
                AlarmType.TYPE_END -> {
                    endMusic
                }
                else -> {
                    graceMusic
                }
            }
            val musicPath = music?.path

            Log.i(TAG, "musicPath: $musicPath")
            //assets
            if (musicPath!!.startsWith("assets/")) {
                playAssetsMusic(context, musicPath.substringAfter("/"))
                //                Log.i(TAG, "musicPath: ${musicPath.substringAfter("/")}")
            }
            //本地音乐
            else {
                playExternalMusic(context, music)
            }
        }
        //超过时间就停止
        //        Timer().schedule(object : TimerTask() {
        //            override fun run() {
        //                stop()
        //            }
        //        }, 1000 * 5)
    }


    /** 播放本地音乐 */
    private fun playExternalMusic(context: Context, music: Music) {
        //判断文件是否存在
        val file = File(music.path)
        if (!file.exists()) {
            context.toast("当前音乐文件不存在")
            return
        }

        if (App.mediaPlayer == null) {
            App.mediaPlayer = MediaPlayer()
        }
        App.mediaPlayer!!.apply {
            reset()
            //先调整音量
            ServiceUtil.setAudioVolumeMax(context)
            //震动时间
            ServiceUtil.vibrate(context, music.duration.toLong())
            //设置资源
            setDataSource(music.path)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    //重置音量
                    ServiceUtil.resetAudioVolume(context)
                }
            }, music.duration.toLong())
            prepare()
            start()
        }
    }

    /** 播放内置音乐 */
    private fun playAssetsMusic(context: Context, musicPath: String) {
        val assetManager = context.assets
        val assetMusic = assetManager.openFd(musicPath)

        if (App.mediaPlayer == null) {
            App.mediaPlayer = MediaPlayer()
        }
        App.mediaPlayer!!.apply {
            reset()
            //先调整音量
            ServiceUtil.setAudioVolumeMax(context)
            //震动时间
            ServiceUtil.vibrate(context, assetMusic.length)
            //设置资源
            setDataSource(assetMusic.fileDescriptor, assetMusic.startOffset, assetMusic.length)
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    //重置音量
                    ServiceUtil.resetAudioVolume(context)
                }
            }, assetMusic.length)
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
