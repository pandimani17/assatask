package com.assaabloy.task.domain.lockconfig.usecase.list

import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamValue
import com.assaabloy.task.domain.lockconfig.repository.LockConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParamValuesUseCase @Inject constructor(
    private val repository: LockConfigRepository
) {
    operator fun invoke(leaf: DoorLeaf): Flow<List<ParamValue>> = repository.getSavedValues(leaf)

}