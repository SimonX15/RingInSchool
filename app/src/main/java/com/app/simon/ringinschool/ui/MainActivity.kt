package com.app.simon.ringinschool.ui

import android.Manifest
import android.app.TimePickerDialog
import android.database.Cursor
import android.media.RingtoneManager
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SwitchCompat
import android.util.Log
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.OnCompletedListener
import com.app.simon.ringinschool.alarm.adapter.AlarmAdapter
import com.app.simon.ringinschool.ring.models.Music
import com.app.simon.ringinschool.utils.PermissionUtil
import com.app.simon.ringinschool.utils.TimeUtil
import com.app.simon.ringinschool.widgets.CustomerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.okButton
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    /** 最后一次点击返回的时间，用于判断退出 */
    private var lastBackPressTime: Long = 0

    var adapter: AlarmAdapter? = null
    var musicList: ArrayList<Music> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionUtil.requestPermissions(this)
        initViews()
    }

    override fun onBackPressed() {
        val time = Calendar.getInstance().time.time
        if (time - lastBackPressTime > 2000L) {
            lastBackPressTime = time
            toast("再按一次退出程序")
        } else {
            finish()
        }
    }

    private fun initViews() {
        adapter = AlarmAdapter(this, App.alarmList)
        adapter?.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
            //更改时间
                R.id.tv_time -> {
                    val alarm = App.alarmList[position]
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay)
                    calendar.set(Calendar.MINUTE, alarm.minute)

                    TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        if (hourOfDay == alarm.hourOfDay && minute == alarm.minute) {
                            return@OnTimeSetListener
                        }
                        if (TimeUtil.isSet(hourOfDay, minute)) {
                            toast("已经设置过该时间的闹钟")
                            return@OnTimeSetListener
                        }
                        AlarmManagerHelper.updateAlarm(this@MainActivity, position, hourOfDay, minute, object : OnCompletedListener {
                            override fun updateAtPosition(from: Int, to: Int) {
                                adapter?.notifyItemChanged(from)
                                adapter?.notifyItemMoved(from, to)
                            }
                        })

                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                            .show()
                }
            //开关
                R.id.switch_compat -> {
                    val switchCompat = view as SwitchCompat
                    val alarm = App.alarmList[position]
                    //修改是否响铃的状态
                    alarm.isOpening = switchCompat.isChecked
                    //添加或取消响铃
                    if (switchCompat.isChecked) {
                        AlarmManagerHelper.startAlarm(this@MainActivity, alarm)
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

        adapter?.setOnItemLongClickListener { adapter, view, position ->
            alert {
                title = "是否删除当前闹钟"
                okButton {
                    AlarmManagerHelper.deleteAlarm(this@MainActivity, position, object : OnCompletedListener {
                        override fun deleteAtPosition(position: Int) {
                            adapter.notifyItemRemoved(position)
                        }
                    })
                }
                cancelButton {

                }
            }.show()
            true
        }
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(CustomerItemDecoration(this, LinearLayoutManager.VERTICAL))

        App.alarmList.forEach {
            if (it.isOpening) {
                sc_alarm_status.isChecked = true
                sc_alarm_status.text = "全部开启"
                return@forEach
            }
        }

        sc_alarm_status.setOnCheckedChangeListener { buttonView, isChecked ->
            buttonView.text = if (isChecked) {
                App.alarmList.forEach {
                    it.isOpening = true
                }
                AlarmManagerHelper.startAllAlarm(this)
                "全部开启"
            } else {
                App.alarmList.forEach {
                    it.isOpening = false
                }
                AlarmManagerHelper.cancelAllAlarm(this)
                "全部关闭"
            }
            adapter?.notifyDataSetChanged()
        }

        btn_start_alarm.onClick {
            val calendar = Calendar.getInstance()
            //当前时间上加一分钟
            calendar.add(Calendar.MINUTE, 1)

            TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (TimeUtil.isSet(hourOfDay, minute)) {
                    toast("已经设置过该时间的闹钟")
                    return@OnTimeSetListener
                }
                AlarmManagerHelper.addAlarm(this@MainActivity, hourOfDay, minute, object : OnCompletedListener {
                    override fun addAtPosition(position: Int) {
                        adapter?.notifyItemInserted(position)
                    }
                })
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
    }

    private fun refreshViews() {

    }

    /**
     * 播放来电铃声的默认音乐
     */
    private fun playRingtoneDefault() {
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val mRingtone = RingtoneManager.getRingtone(this, uri)
        mRingtone.play()
        //        mRingtone.stop()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
