package com.assaabloy.task.presentation.lockconfig.list.state

import com.assaabloy.task.domain.lockconfig.model.DoorLeaf

data class LockConfigListState(
    val items: List<ParamListItem> = emptyList(),
    val searchQuery: String = "",
    val selectedLeaf: DoorLeaf = DoorLeaf.PRIMARY
)