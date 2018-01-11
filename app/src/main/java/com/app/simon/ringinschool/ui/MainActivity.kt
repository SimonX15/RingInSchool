package com.app.simon.ringinschool.ui

import android.app.TimePickerDialog
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SwitchCompat
import android.util.Log
import android.view.LayoutInflater
import com.app.simon.ringinschool.App
import com.app.simon.ringinschool.R
import com.app.simon.ringinschool.alarm.AlarmManagerHelper
import com.app.simon.ringinschool.alarm.OnCompletedListener
import com.app.simon.ringinschool.alarm.adapter.AlarmAdapter
import com.app.simon.ringinschool.ring.models.Music
import com.app.simon.ringinschool.utils.*
import com.app.simon.ringinschool.widgets.CustomerItemDecoration
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    /** 最后一次点击返回的时间，用于判断退出 */
    private var lastBackPressTime: Long = 0

    var adapter: AlarmAdapter? = null

    /** 音乐列表 */
    private var musicList: ArrayList<Music> = ArrayList()
    /** 只有名称 */
    private var musicNameList: ArrayList<String> = ArrayList()
    /** 闹钟类型 */
    private var alarmTypeList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        PermissionUtil.requestPermissions(this)
        initViews()
    }

    override fun onStop() {
        super.onStop()
        val alarmJson = GsonUtil.toJson(App.alarmList)
        Log.i(TAG, "alarmJson:$alarmJson ")
        SpUtil.spAlarmList.set(alarmJson)

        val ringJson = GsonUtil.toJson(App.ring)
        Log.i(TAG, "ringJson:$ringJson ")
        SpUtil.spRing.set(ringJson)
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

    private fun initData() {
        alarmTypeList.add("上课铃声")
        alarmTypeList.add("下课铃声")
        alarmTypeList.add("赞美之歌")
    }

    private fun initViews() {
        refreshRingViews()
        initRecyclerView()

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

        fl_add_alarm.onClick {
            val calendar = Calendar.getInstance()
            //当前时间上加一分钟
            calendar.add(Calendar.MINUTE, 1)

            TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
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

        tv_reset.onClick {
            //重置为默认内容
            val alert = alert {
                title = "是否全部重置为默认设置，包括铃声和闹钟时间的设置"
                okButton {
                    DefaultUtil.setAlarmDefault(this@MainActivity)
                    DefaultUtil.setRingDefault()
                    adapter?.notifyDataSetChanged()
                }
                cancelButton {

                }
            }
            alert.isCancelable = false
            alert.show()
        }

        tv_change_start_ring.onClick {
            searchExternalMusic()
            selector("请选择上课铃声", musicNameList, { _, i ->
                App.ring.startMusic = musicList[i]
                refreshRingViews()
            })
        }
        tv_change_end_ring.onClick {
            searchExternalMusic()
            selector("请选择下课铃声", musicNameList, { _, i ->
                App.ring.endMusic = musicList[i]
                refreshRingViews()
            })
        }
        tv_change_grace_ring.onClick {
            searchExternalMusic()
            selector("请选择赞美之歌", musicNameList, { _, i ->
                App.ring.graceMusic = musicList[i]
                refreshRingViews()
            })
        }
    }

    private fun refreshRingViews() {
        tv_alarm_start.text = App.ring.startMusic?.name
        tv_alarm_end.text = App.ring.endMusic?.name
        tv_alarm_grace.text = App.ring.graceMusic?.name
    }

    private fun initRecyclerView() {
        adapter = AlarmAdapter(this, App.alarmList)
        adapter?.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
            //选择是上课铃声还是下课铃声
                R.id.tv_music_name -> {
                    selector("请选择响铃类型", alarmTypeList, { _, i ->
                        val alarm = App.alarmList[position]
                        alarm.alarmType = i
                        adapter?.notifyItemChanged(position)
                    })
                }
            //更改时间
                R.id.tv_time -> {
                    val alarm = App.alarmList[position]
                    val calendar = Calendar.getInstance()
                    calendar.set(Calendar.HOUR_OF_DAY, alarm.hourOfDay)
                    calendar.set(Calendar.MINUTE, alarm.minute)

                    TimePickerDialog(this@MainActivity, TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                        if (hourOfDay == alarm.hourOfDay && minute == alarm.minute) {
                            return@OnTimeSetListener
                        }
                        if (TimeUtil.isSet(hourOfDay, minute)) {
                            toast("已经设置过该时间的闹钟")
                            return@OnTimeSetListener
                        }
                        AlarmManagerHelper.updateAlarm(this@MainActivity, position, hourOfDay, minute, alarm.alarmType, object : OnCompletedListener {
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
        adapter?.emptyView = LayoutInflater.from(this).inflate(R.layout.partial_empty, null)

        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(CustomerItemDecoration(this, LinearLayoutManager.VERTICAL))

        App.alarmList.forEach {
            if (it.isOpening) {
                sc_alarm_status.isChecked = true
                sc_alarm_status.text = "全部开启"
                return@forEach
            }
        }
    }

    /** 查询所有的音乐 */
    private fun searchExternalMusic() {
        if (musicList.isNotEmpty()) {
            return
        }
        if (App.ring.startMusic != null) {
            musicList.add(App.ring.startMusic!!)
        }
        if (App.ring.endMusic != null) {
            musicList.add(App.ring.endMusic!!)
        }
        if (App.ring.graceMusic != null) {
            musicList.add(App.ring.graceMusic!!)
        }

        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER)
            while (cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) // 路径

                val name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)) // 歌曲名
                //                val album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)) // 专辑
                val artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)) // 作者
                //                val size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)) // 大小
                val duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) // 时长
                //                val time = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)) // 歌曲的id

                val music = Music(path, name)
                musicList.add(music)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            musicList.forEach {
                musicNameList.add(it.name)
            }
            Log.i(TAG, "musicNameList: $musicNameList")
            cursor?.close()
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
