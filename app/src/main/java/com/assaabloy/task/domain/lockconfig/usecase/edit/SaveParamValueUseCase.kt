package com.assaabloy.task.domain.lockconfig.usecase.edit

import com.assaabloy.task.domain.lockconfig.model.ParamValue
import com.assaabloy.task.domain.lockconfig.repository.LockConfigRepository
import javax.inject.Inject

class SaveParamValueUseCase @Inject constructor(
    private val repository: LockConfigRepository
) {
    suspend operator fun invoke(paramValue: ParamValue) = repository.saveValue(paramValue)

}