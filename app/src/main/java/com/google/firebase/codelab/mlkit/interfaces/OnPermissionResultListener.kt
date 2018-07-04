package com.google.firebase.codelab.mlkit.interfaces

interface OnpermissionResultListener {
    fun onResult(permission : List<String>, grandted : List<Boolean>)
}
