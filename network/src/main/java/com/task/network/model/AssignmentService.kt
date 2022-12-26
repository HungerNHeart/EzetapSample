package com.task.network.model

import retrofit2.Response
import retrofit2.http.GET

interface AssignmentService {
    @GET("android_assignment.json")
    suspend fun fetchUIData(): Response<AssignmentResponse>
}