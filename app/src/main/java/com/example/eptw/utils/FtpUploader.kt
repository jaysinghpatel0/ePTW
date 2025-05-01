package com.example.eptw.utils

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import java.io.File
import java.io.FileInputStream

class FtpUploader {

    fun uploadFile(
        server: String,
        username: String,
        password: String,
        remoteDirectory: String,
        file: File
    ): Boolean {
        return try {
            val ftpClient = FTPClient()
            ftpClient.connect(server)
            ftpClient.login(username, password)
            ftpClient.enterLocalPassiveMode()
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

            val inputStream = file.inputStream()
            val success = ftpClient.storeFile("$remoteDirectory${file.name}", inputStream)
            inputStream.close()
            ftpClient.logout()
            ftpClient.disconnect()
            success
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}
