package com.assaabloy.task.presentation.lockconfig.list.state

import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition

data class ParamListItem(
    val definition: ParamDefinition,
    val leaf: DoorLeaf,
    val currentValue: String
)