package com.example.eptw

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.example.eptw.database.ApiClient
import com.example.eptw.database.MSSQLHelper
import com.example.eptw.database.UpdateRequest
import com.example.eptw.utils.FtpUploader
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.Inet4Address
import java.net.NetworkInterface
import java.sql.SQLException
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

        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), 0
        )

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
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmm", Locale.getDefault()).format(Date())
        val storageDir: File = cacheDir
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && photoFile != null) {
            previewImage.setImageURI(Uri.fromFile(photoFile))
            previewImage.visibility = ImageView.VISIBLE

            getLocation {
                uploadImageToFTP()
                updateDatabase()
            }

        }
    }

    private fun getLocation(onLocationReady: () -> Unit) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Location permission not granted", Toast.LENGTH_SHORT).show()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                currentLatitude = location.latitude.toString()
                currentLongitude = location.longitude.toString()
                locationText.text = "Location: $currentLatitude , $currentLongitude"
                locationText.visibility = TextView.VISIBLE
                onLocationReady()
            } else {
                Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadImageToFTP() {
        if (photoFile != null) {
            val ftpUploader = FtpUploader()
            Thread {
                val success = ftpUploader.uploadFile(
                    server = "grasimchemicals.xxatsolution.com",
                    username = "grasimchemicals",
                    password = "556Phn1*l",
                    remoteDirectory = "/ImageUpload/",
                    file = photoFile!!
                )

                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
    }

    //For IP Address Locate
    fun getDeviceIPAddress(): String {
        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()
            for (intf in interfaces) {
                val addresses = intf.inetAddresses
                for (addr in addresses) {
                    if (!addr.isLoopbackAddress && addr is Inet4Address) {
                        return addr.hostAddress ?: "Unavailable"
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return "Unavailable"
    }

    private fun updateDatabase() {
        val conn = MSSQLHelper.getConnection()
        val ipAddress = getDeviceIPAddress()
        if (conn != null) {
            try {
                val stmt = conn.createStatement()
                val query = """
                UPDATE GWP_Approval
                SET ImagePath1 = '.../ImageUpload/${photoFile?.name}',
                    latitude = '$currentLatitude',
                    Logitude = '$currentLongitude',
                    DeviceType = 'Tab',
                    DeviceID = '${android.os.Build.ID}',
                    IPAddress = '$ipAddress'
                WHERE ApprovalRole = 'Isolator'
                AND PermitNo = 'NGD/FY25/3'
            """.trimIndent()

                val rowsAffected = stmt.executeUpdate(query)
                if (rowsAffected > 0) {
                    runOnUiThread {
                        Toast.makeText(this, "Database updated successfully.", Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "No rows updated.", Toast.LENGTH_LONG).show()
                    }
                }

                conn.close()
            } catch (e: SQLException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this, "Database update failed: ${e.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        } else {
            runOnUiThread {
                Toast.makeText(this, "Connection failed.", Toast.LENGTH_LONG).show()
            }
        }
    }

}
