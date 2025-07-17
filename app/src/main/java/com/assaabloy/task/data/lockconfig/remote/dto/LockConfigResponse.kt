package com.assaabloy.task.data.lockconfig.remote.dto

data class LockConfigResponse(
    val success: Boolean,
    val message: String,
    val data: LockConfigData
)