package com.app.simon.ringinschool.ring.models

import io.realm.RealmModel
import io.realm.annotations.RealmClass

/**
 * desc: 音乐
 * date: 2018/1/5
 *
 * @author xw
 */
@RealmClass
open class Music(
        var path: String = "", //路径
        var name: String = "", //歌曲名称
        var artist: String = "", //作者
        var duration: Int = 0 //时长
) : RealmModel {
    override fun toString(): String {
        return "Music(path='$path', name='$name', artist='$artist', duration='$duration')"
    }
}
