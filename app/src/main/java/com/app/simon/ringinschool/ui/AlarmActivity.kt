package com.app.simon.ringinschool.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.simon.ringinschool.R
import org.jetbrains.anko.*

class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val alert = alert {
            title = "你设置的闹钟时间到了"
            okButton {
                title = "知道了"
                finish()
            }
            cancelButton {
                title = "取消"
                finish()
            }
        }
        alert.isCancelable = false
        alert.show()
    }

    companion object {
        private val TAG = AlarmActivity::class.java.simpleName

        fun launch(activity: Activity) {
            activity.run {
                startActivity(intentFor<AlarmActivity>(/*"" to ""*/))
            }
        }

        fun launch(context: Context) {
            context.run {
                startActivity(intentFor<AlarmActivity>(/*"" to ""*/).newTask())
            }
        }
    }
}
