package com.assaabloy.task.data.lockconfig.remote.dto

import com.google.gson.annotations.SerializedName


data class RecordResponse(
    @SerializedName("record")
    val record: LockConfigResponse,

)
