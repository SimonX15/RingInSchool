package com.app.simon.ringinschool.ring.db

/**
 * desc: 闹钟 Helper
 * date: 2018/1/8
 *
 * @author xw
 */
@Deprecated("暂时不用")
object AlarmDBHelper {

    /**————————————————————————————————alarm————————————————————————————————————————*/
    /** 存储 */
    /* fun addAlarmList(alarmList: ArrayList<Alarm>, listener: OnDBCompleteListener) {
         //删除
         deleteAlarmList()
         //添加
         val realm = RealmHelper.getRealm()
         realm.executeTransactionAsync({
             alarmList.forEach {
                 realm.copyToRealmOrUpdate(it)
             }
         }, {
             listener.onSuccess()
             realm.close()
         }, {
             listener.onError(it)
             realm.close()
         })
     }

     */
    /** 删除闹钟 *//*
    private fun deleteAlarmList(listener: OnDBCompleteListener? = null) {
        val realm = RealmHelper.getRealm()
        realm.executeTransactionAsync({
            val results = it.where(Music::class.java)
                    .findAll()
            results.deleteAllFromRealm()
        }, {
            listener?.onSuccess()
            realm.close()
        }, {
            listener?.onError(it)
            realm.close()
        })
    }

    */
    /** 查找全部 *//*
    fun queryAlarmList(listener: OnDBCompleteListener) {
        val realm = RealmHelper.getRealm()
        realm.executeTransactionAsync({
            val results = it.where(Music::class.java)
                    .findAll()
            listener.onResult(realm.copyFromRealm(results))
        }, {
            listener.onSuccess()
            realm.close()
        }, {
            listener.onError(it)
            realm.close()
        })
    }
    */
    /**————————————————————————————————alarm————————————————————————————————————————*//*
    */
    /** 存储 *//*
    fun setRing(ring: Ring, listener: OnDBCompleteListener) {
        //添加
        val realm = RealmHelper.getRealm()
        realm.executeTransactionAsync({
            it.copyToRealmOrUpdate(ring)
        }, {
            listener.onSuccess()
            realm.close()
        }, {
            listener.onError(it)
            realm.close()
        })
    }

    */
    /** 查找全部 *//*
    fun queryRing(listener: OnDBCompleteListener) {
        val realm = RealmHelper.getRealm()
        realm.executeTransactionAsync({
            val results = it.where(Ring::class.java)
                    .findAll()
            listener.onResult(realm.copyFromRealm(results))
        }, {
            listener.onSuccess()
            realm.close()
        }, {
            listener.onError(it)
            realm.close()
        })
    }*/
}