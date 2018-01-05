package com.app.simon.ringinschool.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.app.simon.ringinschool.R
import org.jetbrains.anko.*

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        alert {
            title = "你设置的闹钟时间到了"

            okButton {
                title = "知道了"
                finish()
            }
            cancelButton {
                title = "取消"
                finish()
            }
        }.show()
    }

    companion object {
        private val TAG = SecondActivity::class.java.simpleName

        fun launch(activity: Activity) {
            activity.run {
                startActivity(intentFor<SecondActivity>(/*"" to ""*/))
            }
        }

        fun launch(context: Context) {
            context.run {
                startActivity(intentFor<SecondActivity>(/*"" to ""*/).newTask())
            }
        }
    }
}
