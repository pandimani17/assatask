package com.assaabloy.task.di.lockconfig

import com.assaabloy.task.data.core.AppDatabase
import com.assaabloy.task.data.lockconfig.local.dao.ParamValueDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object LockConfigDatabaseModule {

    @Provides
    fun provideParamValueDao(
        db: AppDatabase
    ): ParamValueDao = db.paramValueDao()
}