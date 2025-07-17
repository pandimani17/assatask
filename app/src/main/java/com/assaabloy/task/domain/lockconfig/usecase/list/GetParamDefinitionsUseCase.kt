package com.assaabloy.task.domain.lockconfig.usecase.list

import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import com.assaabloy.task.domain.lockconfig.repository.LockConfigRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetParamDefinitionsUseCase @Inject constructor(
    private val repository: LockConfigRepository
) {

    operator fun invoke(): Flow<List<ParamDefinition>> = repository.getAllDefinitions()

}