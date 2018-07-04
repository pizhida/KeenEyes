package com.google.firebase.codelab.mlkit.event

import android.graphics.Bitmap

class SendImageEvent
{
    var bitmap: String? = null

    constructor(bitmap : String)
    {
        this.bitmap = bitmap
    }
}