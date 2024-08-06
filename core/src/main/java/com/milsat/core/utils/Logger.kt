package com.milsat.core.utils

import android.util.Log
import com.milsat.core.BuildConfig

object Logger {

    fun debug(message: String) {
        if(BuildConfig.DEBUG.not()) return
        Log.d("DEBUG", message)
    }

    fun debug(tag:String, message: String) {
        if(BuildConfig.DEBUG.not()) return
        Log.d(tag, message)
    }

    fun error(message: String) {
        if(BuildConfig.DEBUG.not()) return
        Log.e("DEBUG", message)
    }

    fun error(tag:String, message: String) {
        if(BuildConfig.DEBUG.not()) return
        Log.e(tag, message)
    }

    fun verbose(message: String) {
        Log.v("DEBUG", message)
    }
}