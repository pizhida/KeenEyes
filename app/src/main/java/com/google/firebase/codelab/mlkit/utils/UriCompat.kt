package com.google.firebase.codelab.mlkit.utils

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import java.io.File

object UriCompat {

    fun fromFile(context: Context, file: File) : Uri {

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val provider = AppInfoUtils.getInstance().URI_PROVIDER
            FileProvider.getUriForFile(context, provider, file)
        } else {
            Uri.fromFile(file)
        }

    }

}