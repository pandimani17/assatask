package com.assaabloy.task.data.lockconfig.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assaabloy.task.data.lockconfig.local.entity.ParamValueEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ParamValueDao {

    @Query("SELECT * FROM ParamValueEntity WHERE leaf = :leaf")
    fun valuesForLeaf(leaf: String): Flow<List<ParamValueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ParamValueEntity)

    @Query("DELETE FROM ParamValueEntity")
    suspend fun clearAll()
}