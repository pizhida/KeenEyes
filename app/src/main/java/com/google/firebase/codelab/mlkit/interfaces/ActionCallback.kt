package com.google.firebase.codelab.mlkit.interfaces

interface ActionCallback<T> {
    fun onSuccess(t: T)
    fun onTokenExpired(){}
    fun onFailed(message: String)
}
