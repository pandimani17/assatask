package com.assaabloy.task.data.lockconfig.remote.dto

import com.google.gson.annotations.SerializedName

data class ChoiceDto (

    val values: List<String>,
    @SerializedName("default") val defaultValue: String
)