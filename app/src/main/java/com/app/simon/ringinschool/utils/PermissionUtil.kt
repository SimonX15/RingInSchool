package com.app.simon.ringinschool.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.util.Log
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


/**
 * desc: 权限
 * date: 2018/1/9
 *
 * @author xw
 */
object PermissionUtil {
    private val TAG = PermissionUtil::class.java.simpleName!!

    //    private var needCheckPermissions = arrayOf("存储空间")
    private var needCheckPermissions = arrayOf("位置信息", "存储空间", "电话", "读取联系人")

    /**
     * 请求权限处理
     * 1、若已拥有全部权限，则无处理
     * 2、否则提示去设置修改权限

     * @param activity
     * *
     * @param callback
     * *         请求权限成功、请求权限失败 的回调
     */
    fun requestPermissions(activity: Activity, callback: Callback? = object : Callback {
        override fun onGranted() {

        }

        override fun onNotGranted() {
            checkAppPermission(activity)
        }
    }) {
        RxPermissions(activity)
                .request(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                        /*Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE*/
                )
                .subscribe { granted ->
                    Log.d(TAG, "权限打开完了没 granted：" + granted!!)
                    if (granted) {
                        Log.d(TAG, "所有权限所打开了")
                        callback?.onGranted()
                    } else {
                        Log.d(TAG, "部分权限没有打开")
                        callback?.onNotGranted()
                    }
                }
    }

    /**
     * 请求权限处理
     * 1、若已拥有全部权限，则无处理
     * 2、否则提示去设置修改权限
     */
    fun checkAppPermission(context: Context) {
        Log.d(TAG, "checkAppPermission")

        val myNeedPermissions = ArrayList<String>()

        val checkStoragePermission = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE")
        val hasStoragePermission = checkStoragePermission == PackageManager.PERMISSION_GRANTED
        Log.d(TAG, "hasStoragePermission:" + hasStoragePermission)
        if (!hasStoragePermission) {
            myNeedPermissions.add(needCheckPermissions[1])
        }

        //        val checkLocationPermission = context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")
        //        val hasLocationPermission = checkLocationPermission == PackageManager.PERMISSION_GRANTED
        //        Log.d(TAG, "hasLocationPermission:" + hasLocationPermission)
        //        if (!hasLocationPermission) {
        //            myNeedPermissions.add(needCheckPermissions[0])
        //        }
        //
        //        val checkReadConstantsPermission = context.checkCallingOrSelfPermission("android.permission.READ_CONTACTS")
        //        val readConstantsPermission = checkReadConstantsPermission == PackageManager.PERMISSION_GRANTED
        //
        //        if (!readConstantsPermission) {
        //            myNeedPermissions.add(needCheckPermissions[0])
        //            myNeedPermissions.add(needCheckPermissions[3])
        //        }

        //        int checkCallPhonePermission = mContext.checkCallingOrSelfPermission("android.permission.CALL_PHONE");
        //        boolean hasCallPhonePermission = checkCallPhonePermission == PackageManager.PERMISSION_GRANTED;
        //        Log.d(TAG, "hasCallPhonePermission:" + hasCallPhonePermission);
        //        if (!hasCallPhonePermission) {
        //            myNeedPermissions.add(needCheckPermissions[2]);
        //        }

        if (myNeedPermissions.isEmpty()) {
            Log.d(TAG, "权限都有")
            return
        }
        val sb = StringBuffer()
        sb.append("请在应用设置>应用权限中打开“")
        for (i in myNeedPermissions.indices) {
            val myNeedPermission = myNeedPermissions[i]
            if (i > 0) {
                sb.append("，")
            }
            sb.append(myNeedPermission)
        }
        for (myNeedPermission in myNeedPermissions) {
            sb.append(myNeedPermission)
        }
        sb.append("”的权限。")

        var msg = sb.toString()
        Log.d(TAG, msg)

        msg = "获取手机权限失败，请在手机应用权限中打开所有权限"

        AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("打开设置") { _, _ ->
                    val packageName = context.applicationContext.packageName
                    val packageURI = Uri.parse("package:" + packageName)
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    context.startActivity(intent)
                }
                .setNeutralButton("关闭", null)
                .setCancelable(true)
                .create()
                .show()
    }

    /**
     * 检查权限的回调
     */
    interface Callback {
        fun onGranted()

        fun onNotGranted()
    }
}
