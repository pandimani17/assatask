package com.assaabloy.task.presentation.lockconfig.edit.state

import com.assaabloy.task.domain.lockconfig.model.ParamDefinition

data class LockConfigEditState(
    val definition: ParamDefinition? = null,
    val primaryValue: String = "",
    val secondaryValue: String = "",
    val isLoading: Boolean = true,
    val error: String? = null
)