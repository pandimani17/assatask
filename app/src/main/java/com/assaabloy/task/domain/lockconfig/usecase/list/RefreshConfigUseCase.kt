package com.assaabloy.task.domain.lockconfig.usecase.list

import com.assaabloy.task.data.lockconfig.repository.LockConfigRepositoryImpl
import javax.inject.Inject

class RefreshConfigUseCase @Inject constructor(
    private val repo: LockConfigRepositoryImpl
) {
    suspend operator fun invoke() = repo.refreshFromApi()
}