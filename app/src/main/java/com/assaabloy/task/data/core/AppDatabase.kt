package com.assaabloy.task.data.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assaabloy.task.data.lockconfig.local.dao.ParamValueDao
import com.assaabloy.task.data.lockconfig.local.entity.ParamValueEntity


@Database(
    entities = [ParamValueEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paramValueDao(): ParamValueDao
}