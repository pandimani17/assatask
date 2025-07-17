package com.assaabloy.task.domain.lockconfig.repository

import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import com.assaabloy.task.domain.lockconfig.model.ParamValue
import kotlinx.coroutines.flow.Flow

interface LockConfigRepository {

    fun getAllDefinitions(): Flow<List<ParamDefinition>>

    fun getSavedValues(leaf: DoorLeaf): Flow<List<ParamValue>>

    suspend fun saveValue(param: ParamValue)

}