package com.example.eptw

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings

object DeviceUtils {
    fun getDeviceID(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    fun getDeviceType(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) "Android (R+)" else "Android"
    }
}