package com.app.simon.ringinschool.ui

import android.Manifest
import android.app.TimePickerDialog
import android.database.Cursor
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SwitchCompat
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.adapter.AlarmAdapter
import com.app.simon.ringinschool.music.Music
import com.app.simon.ringinschool.utils.TimeUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    var adapter: AlarmAdapter? = null
    var musicList: ArrayList<Music> = ArrayList()

    val mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        initViews()
    }

    private fun initViews() {
        adapter = AlarmAdapter(this, App.alarmList)
        recycler_view.adapter = adapter

        adapter?.setOnItemChildClickListener { adapter, view, position ->

            when (view.id) {
                R.id.switch_compat -> {
                    val switchCompat = view as SwitchCompat
                    val alarm = App.alarmList[position]
                    //修改是否响铃的状态
                    alarm.isOpening = switchCompat.isChecked
                    //添加或取消响铃
                    if (switchCompat.isChecked) {
                        AlarmManagerHelper.addAlarm(this@MainActivity, alarm)
                    } else {
                        AlarmManagerHelper.cancelAlarm(this@MainActivity, alarm)
                    }
                    //notify
                    adapter?.notifyItemChanged(position)
                    Log.i(TAG, "alarm: $alarm")
                }
                else -> {
                }
            }
        }


        btn_start_alarm.onClick {
            val calendar = Calendar.getInstance()
            //当前时间上加一分钟
            calendar.add(Calendar.MINUTE, 1)

            TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                //                toast("$hourOfDay+$minute")
                if (TimeUtil.isSet(hourOfDay, minute)) {
                    toast("已经设置过该时间的闹钟")
                    return@OnTimeSetListener
                }
                AlarmManagerHelper.addAlarm(this@MainActivity, hourOfDay, minute)
                adapter?.notifyItemInserted(App.alarmList.size - 1)
                //                adapter?.addData(App.alarmList[App.alarmList.size - 1])
                //                refreshViews()
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                    .show()
        }

        btn_permission.onClick {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }

        btn_search_music.onClick {
            var cursor: Cursor? = null
            try {
                cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                        MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
                while (cursor.moveToNext()) {
                    val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) // 路径

                    val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)) // 歌曲名
                    val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) // 专辑
                    val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) // 作者
                    val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)) // 大小
                    val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) // 时长
                    val time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)) // 歌曲的id
                    // int albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                    val music = Music(path, name, artist, duration)
                    Log.i(TAG, "music: $path")
                    musicList.add(music)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                cursor?.close()
            }
        }

        btn_play.onClick {
            mediaPlayer.setDataSource(musicList[musicList.size - 2].path)
            //            mediaPlayer.prepareAsync()
            mediaPlayer.prepare()
            mediaPlayer.start()
        }

        btn_stop.onClick {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    private fun refreshViews() {

    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
