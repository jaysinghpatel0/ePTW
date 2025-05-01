package com.example.eptw

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.eptw.database.ApiClient
import com.example.eptw.database.UpdateRequest
import com.example.eptw.utils.FtpUploader
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var previewImage: ImageView
    private lateinit var locationText: TextView

    private var photoFile: File? = null
    private var currentLatitude = ""
    private var currentLongitude = ""
    private val REQUEST_IMAGE_CAPTURE = 1

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webView)
        previewImage = findViewById(R.id.previewImage)
        locationText = findViewById(R.id.locationText)

        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ), 0)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()
        webView.addJavascriptInterface(WebAppInterface(this), "AndroidFunction")
        webView.loadUrl("http://grasimchemicals.xxatsolution.com/")
    }

    fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile!!)
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && photoFile != null) {
            previewImage.setImageURI(Uri.fromFile(photoFile))
            previewImage.visibility = ImageView.VISIBLE

            getLocation()
            uploadImageToFTP()
            updateDatabase()
        }
    }

    private fun getLocation() {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        location?.let {
            currentLatitude = it.latitude.toString()
            currentLongitude = it.longitude.toString()
            locationText.text = "Location: $currentLatitude , $currentLongitude"
            locationText.visibility = TextView.VISIBLE
        }
    }

    private fun uploadImageToFTP() {
        if (photoFile != null) {
            val ftpUploader = FtpUploader()
            Thread {
                val success = ftpUploader.uploadFile(
                    server = "grasimchemicals.xxatsolution.com",
                    username = "grasimchemicals",
                    password = "556Phn1*l",  // 🔥 Use actual FTP password
                    remoteDirectory = "/ImageUpload/",
                    file = photoFile!!
                )

                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    private fun updateDatabase() {
        val imagePath = "/ImageUpload/${photoFile?.name}"
        val deviceType = "Android Device"
        val deviceID = android.os.Build.ID
        val ipAddress = "0.0.0.0"
        val permitID = "5"
        val permitNo = "NGD/FY25/3"

        val request = UpdateRequest(imagePath, currentLatitude, currentLongitude, deviceType, deviceID, ipAddress, permitID, permitNo)

        ApiClient.instance.updateApproval(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Database updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "DB update failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
