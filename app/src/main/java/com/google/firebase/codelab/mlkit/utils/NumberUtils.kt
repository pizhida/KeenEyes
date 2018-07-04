package com.google.firebase.codelab.mlkit.utils

import android.content.Context
import android.util.TypedValue
import java.text.DecimalFormat
import java.text.NumberFormat

object NumberUtils {

    fun toPixcel(context: Context, dimension: Float): Int {
        val res = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimension, context.resources.displayMetrics)
        return Math.ceil(res.toDouble()).toInt()
    }

    fun getShortNumberFormatted(value: Float): String {
        val decimalFormat = DecimalFormat("#,###,###,###,###.#")
        return if (value < 1000) {
            decimalFormat.format(value.toDouble())
        } else if (value < 1000000) {
            decimalFormat.format((value / 1000).toDouble()) + "K"
        } else {
            decimalFormat.format((value / 1000000).toDouble()) + "M"
        }
    }

    fun getNumberFormat(value: Float): String {
        val numberFormat = NumberFormat.getNumberInstance()
        return numberFormat.format(value.toDouble())
    }

    fun getCurrencyNumberFormat(value: Float): String {
        val numberFormat = NumberFormat.getCurrencyInstance()
        return numberFormat.format(value.toDouble())
    }

}
