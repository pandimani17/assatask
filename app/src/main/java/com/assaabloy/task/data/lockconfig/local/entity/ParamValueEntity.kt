package com.assaabloy.task.data.lockconfig.local.entity

import androidx.room.Entity


@Entity(
    tableName = "ParamValueEntity",
    primaryKeys = ["key", "leaf"]
)
data class ParamValueEntity(
    val key: String, val leaf: String, val value: String

)