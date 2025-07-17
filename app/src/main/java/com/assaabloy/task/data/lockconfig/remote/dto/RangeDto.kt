package com.assaabloy.task.data.lockconfig.remote.dto

import com.google.gson.annotations.SerializedName

data class RangeDto(
    val range: RangeBoundsDto,
    val unit: String,
    @SerializedName("default") val defaultValue: String

)
