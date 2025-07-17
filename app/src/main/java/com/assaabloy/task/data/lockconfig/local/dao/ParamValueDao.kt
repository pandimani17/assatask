package com.assaabloy.task.data.lockconfig.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.assaabloy.task.data.lockconfig.local.entity.ParamValueEntity
import kotlinx.coroutines.flow.Flow
import androidx.room.OnConflictStrategy



@Dao
interface ParamValueDao {

    @Query("SELECT * FROM ParamValueEntity WHERE leaf = :leaf")
    fun valuesForLeaf(leaf: String): Flow<List<ParamValueEntity>>

//    @Query(" SELECT * FROM ParamValueEntity WHERE leaf = :leaf AND (key LIKE '%' || :query || '%' OR value LIKE '%' || :query || '%')")
//    fun filterValues(leaf: String, query: String): Flow<List<ParamValueEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: ParamValueEntity)
}