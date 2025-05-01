package com.example.eptw

import android.webkit.JavascriptInterface

class WebAppInterface(private val activity: MainActivity) {
    @JavascriptInterface
    fun openCamera() {
        activity.runOnUiThread {
            activity.openCamera()
        }
    }
}
