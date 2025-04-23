package com.example.eptw.database


import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("update_approval.php")  // Your server endpoint
    fun updateApproval(@Body request: UpdateRequest): retrofit2.Call<Void>
}
