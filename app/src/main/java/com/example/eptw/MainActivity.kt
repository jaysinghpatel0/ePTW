package com.example.eptw

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.eptw.database.ApiClient
import com.example.eptw.database.UpdateRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val url = "http://grasimchemicals.xxatsolution.com/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.clearCache(true)
        webView.clearHistory()

        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String?,
                failingUrl: String?
            ) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                Log.e("WebView", "Page Load Error: $description")
            }
        }

        webView.loadUrl(url)

        // ✅ Sample Data
        val imagePath = "/imageupload/IMG_001.jpg"
        val latitude = "21.1702"
        val longitude = "72.8311"
        val deviceType = "Tab"
        val deviceID = "AndroidDevice123"
        val ipAddress = "192.168.1.5"
        val permitID = "5"
        val permitNo = "NGD/FY25/3"

        val request = UpdateRequest(
            imagePath, latitude, longitude, deviceType, deviceID, ipAddress, permitID, permitNo
        )

        ApiClient.instance.updateApproval(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d("API", "Approval Updated Successfully.")
                } else {
                    Log.e("API", "Update failed: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("API", "Error: ${t.message}")
            }
        })
    }
}
