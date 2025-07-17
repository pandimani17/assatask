package com.assaabloy.task.data.lockconfig.remote.dto

import com.google.gson.annotations.SerializedName

data class LockConfigData(
    @SerializedName("lockConfiguration")
    val lockConfiguration: LockConfigurationDto
)