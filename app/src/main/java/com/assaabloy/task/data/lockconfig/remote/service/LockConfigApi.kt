package com.assaabloy.task.data.lockconfig.remote.service

import com.assaabloy.task.data.lockconfig.remote.dto.LockConfigResponse
import com.assaabloy.task.data.lockconfig.remote.dto.RecordResponse
import retrofit2.http.GET

interface LockConfigApi {

    @GET("v3/b/687a20cd8de3783286a973fe")
    suspend fun fetchLockConfig(): RecordResponse


}