package com.assaabloy.task.di.lockconfig

import com.assaabloy.task.data.lockconfig.remote.service.LockConfigApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LockConfigApiModule {


    @Provides
    @Singleton
    fun provideLockConfigApi(retrofit: Retrofit): LockConfigApi =
        retrofit.create(LockConfigApi::class.java)
}