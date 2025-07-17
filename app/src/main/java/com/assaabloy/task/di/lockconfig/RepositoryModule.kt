package com.assaabloy.task.di.lockconfig

import com.assaabloy.task.data.lockconfig.repository.LockConfigRepositoryImpl
import com.assaabloy.task.domain.lockconfig.repository.LockConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent



@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLockConfigRepository(
        impl: LockConfigRepositoryImpl
    ): LockConfigRepository
}