package com.example.eptw.database

data class UpdateRequest(
    val imagePath: String,
    val latitude: String,
    val longitude: String,
    val deviceType: String,
    val deviceID: String,
    val ipAddress: String,
//    val permitID: String,
    val permitNo: String
)