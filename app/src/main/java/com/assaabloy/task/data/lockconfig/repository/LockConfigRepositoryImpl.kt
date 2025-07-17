package com.assaabloy.task.data.lockconfig.repository

import android.content.Context
import com.assaabloy.task.data.lockconfig.local.dao.ParamValueDao
import com.assaabloy.task.data.lockconfig.local.entity.ParamValueEntity
import com.assaabloy.task.data.lockconfig.local.mapper.toDomain
import com.assaabloy.task.data.lockconfig.local.mapper.toEntity
import com.assaabloy.task.data.lockconfig.remote.dto.LockConfigResponse
import com.assaabloy.task.domain.lockconfig.model.ChoiceParams
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import com.assaabloy.task.domain.lockconfig.model.ParamValue
import com.assaabloy.task.domain.lockconfig.model.RangeParams
import com.assaabloy.task.domain.lockconfig.repository.LockConfigRepository
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockConfigRepositoryImpl @Inject constructor(
    @ApplicationContext private val ctx: Context,
    private val dao: ParamValueDao
) : LockConfigRepository {

    private val definitionsFlow: StateFlow<List<ParamDefinition>> =
        flow {
            val json = ctx.assets
                .open("lock_config.json")
                .bufferedReader().use { it.readText() }

            val resp = Gson().fromJson(json, LockConfigResponse::class.java)
            require(resp.success) { resp.message }

            val dto = resp.data.lockConfiguration
            val defs = buildList<ParamDefinition> {
                add(ChoiceParams("lockVoltage",     "Lock Voltage",      dto.lockVoltage.values,      dto.lockVoltage.defaultValue))
                add(ChoiceParams("lockType",        "Lock Type",         dto.lockType.values,         dto.lockType.defaultValue))
                add(ChoiceParams("lockKick",        "Lock Kick",         dto.lockKick.values,         dto.lockKick.defaultValue))
                add(ChoiceParams("lockRelease",     "Lock Release",      dto.lockRelease.values,      dto.lockRelease.defaultValue))
                add(
                    RangeParams(   "lockReleaseTime", "Lock Release Time", dto.lockReleaseTime.range.min,
                    dto.lockReleaseTime.range.max,
                    dto.lockReleaseTime.unit,
                    dto.lockReleaseTime.defaultValue)
                )
                add(RangeParams(   "lockAngle",       "Lock Angle",        dto.lockAngle.range.min,
                    dto.lockAngle.range.max,
                    dto.lockAngle.unit,
                    dto.lockAngle.defaultValue))
            }
            emit(defs)
        }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                started = SharingStarted.Eagerly,
                initialValue = emptyList()
            )

    override fun getAllDefinitions(): Flow<List<ParamDefinition>> =
        definitionsFlow

    override fun getSavedValues(leaf: DoorLeaf): Flow<List<ParamValue>> =
        dao.valuesForLeaf(leaf.name)
            .map { list -> list.map(ParamValueEntity::toDomain) }

    override suspend fun saveValue(param: ParamValue) {
        dao.upsert(param.toEntity())
    }
}