package com.example.prog7313_poe_part_2_group_2_nextgen_codecrafters.utils

import android.util.Log

object AppLogger {

    private const val APP_TAG = "FinTrackApp"


    private const val ENABLE_DEBUG_LOGS = true

    fun debug(tag: String, message: String) {
        if (ENABLE_DEBUG_LOGS) {
            Log.d("$APP_TAG-$tag", message)
        }
    }

    fun info(tag: String, message: String) {
        if (ENABLE_DEBUG_LOGS) {
            Log.i("$APP_TAG-$tag", message)
        }
    }

    fun warning(tag: String, message: String) {
        Log.w("$APP_TAG-$tag", message)
    }

    fun error(tag: String, message: String, throwable: Throwable? = null) {
        Log.e("$APP_TAG-$tag", message, throwable)
    }
}