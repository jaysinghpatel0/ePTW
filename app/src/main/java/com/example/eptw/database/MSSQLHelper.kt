package com.example.eptw.database

import android.os.StrictMode
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object MSSQLHelper {

    private const val DB_URL = "jdbc:jtds:sqlserver://49.50.100.105:5263/GrasimChemicals"
    private const val USER = "GrasimChemicals"
    private const val PASSWORD = "qk#4R021a"

    fun getConnection(): Connection? {
        return try {
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            Class.forName("net.sourceforge.jtds.jdbc.Driver")
            DriverManager.getConnection(DB_URL, USER, PASSWORD)
        } catch (e: SQLException) {
            e.printStackTrace()
            null
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
            null
        }
    }
}
