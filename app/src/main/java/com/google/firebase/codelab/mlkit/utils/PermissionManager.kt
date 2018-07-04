package com.google.firebase.codelab.mlkit.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.google.firebase.codelab.mlkit.interfaces.OnpermissionResultListener

class PermissionManager {

    private var mListener: OnpermissionResultListener? = null
    private val PERMISSION_REQ_CODE = 1034

    companion object {

        private var instancce : PermissionManager? = null

        @Synchronized
        fun getInstance() :PermissionManager {
            if (instancce == null) {
                instancce = PermissionManager()
            }
            return instancce!!
        }
    }

    fun isHavePermission(activity : Activity, permission : Array<String>) : Boolean {

        var isGrandted = true

        permission.forEach {
            if (ActivityCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED){
                isGrandted = false
            }
        }

        return isGrandted

    }

    fun requestPermission(activity : Activity, permission : Array<String>) {

        ActivityCompat.requestPermissions(activity, permission, PERMISSION_REQ_CODE)

    }

    fun setPermissionResultListener(listener : OnpermissionResultListener) {
        this.mListener = listener
    }

    fun onActivityResult(requestCode : Int, permission : Array<out String>, grantPermission : IntArray) {
        if (requestCode == PERMISSION_REQ_CODE) {
            if (grantPermission.isNotEmpty()) {
                mListener?.let {
                    mListener!!.onResult(permission.toList(), grantPermission.map { it == PackageManager.PERMISSION_GRANTED })
                }
            } else {
                mListener?.let {
                    mListener!!.onResult(permission.toList(), grantPermission.map { it == PackageManager.PERMISSION_GRANTED })
                }
            }
        }
    }

}
