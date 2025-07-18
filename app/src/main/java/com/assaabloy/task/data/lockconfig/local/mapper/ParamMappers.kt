package com.assaabloy.task.data.lockconfig.local.mapper

import com.assaabloy.task.data.lockconfig.local.entity.ParamValueEntity
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import com.assaabloy.task.domain.lockconfig.model.ParamValue

fun ParamValueEntity.toDomain() = ParamValue(
    key   = key,
    leaf  = DoorLeaf.valueOf(leaf),
    value = value
)

fun ParamValue.toEntity() = ParamValueEntity(
    key   = key,
    leaf  = leaf.name,
    value = value
)

fun ParamDefinition.toEntity(leaf: DoorLeaf): ParamValueEntity =
    ParamValueEntity(
        key   = this.key,
        leaf  = leaf.name,
        value = this.defaultValue
    )