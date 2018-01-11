package com.app.simon.ringinschool.ring.db

import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * desc: RealmHelper
 * date: 2018/1/8
 *
 * @author xw
 */
object RealmHelper {
    //    private val TAG = RealmHelper::class.java.simpleName
    private val REALM_NAME = "my_realm.realm"

    /** 获取realm对象 */
    fun getRealm(): Realm {
        val configuration = RealmConfiguration.Builder()
                .name(REALM_NAME) //文件名
                //                    .schemaVersion(0) //版本号
                /*.schemaVersion(1) //版本号升级
                .migration { realm, oldVersion, newVersion ->
                    Log.i(TAG, "oldVersion:$oldVersion;newVersion:$newVersion")
                    if (oldVersion == 0L) {
                        val schema = realm.schema
                    }
                }*/
                .schemaVersion(1) //版本号升级
                //                    .deleteRealmIfMigrationNeeded()//删除原数据
                .build()
        Realm.setDefaultConfiguration(configuration)
        return Realm.getInstance(configuration)!!
    }


}
