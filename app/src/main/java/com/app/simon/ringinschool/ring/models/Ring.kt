package com.app.simon.ringinschool.ring.models

import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * desc: 上下课铃声
 * date: 2018/1/9
 *
 * @author xw
 */
@RealmClass
open class Ring(
        var startMusic: Music? = null, //上课铃声
        var endMusic: Music? = null //下课铃声

) : RealmModel {

    override fun toString(): String {
        return "Ring(startMusic=$startMusic, endMusic=$endMusic)"
    }
}