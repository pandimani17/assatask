package com.assaabloy.task.data.lockconfig.repository

import com.assaabloy.task.data.lockconfig.local.dao.ParamValueDao
import com.assaabloy.task.data.lockconfig.local.entity.ParamValueEntity
import com.assaabloy.task.data.lockconfig.local.mapper.toDomain
import com.assaabloy.task.data.lockconfig.local.mapper.toEntity
import com.assaabloy.task.data.lockconfig.remote.service.LockConfigApi
import com.assaabloy.task.domain.lockconfig.model.ChoiceParams
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import com.assaabloy.task.domain.lockconfig.model.ParamValue
import com.assaabloy.task.domain.lockconfig.model.RangeParams
import com.assaabloy.task.domain.lockconfig.repository.LockConfigRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockConfigRepositoryImpl @Inject constructor(
    private val api: LockConfigApi, private val dao: ParamValueDao
) : LockConfigRepository {
    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val definitionsFlow = refreshTrigger.flatMapLatest {
            flow {
                val wrapper = api.fetchLockConfig()
                val response = wrapper.record
                require(response.success) { response.message }
                val dto = response.data.lockConfiguration

                dao.clearAll()

                val definitions = listOf<ParamDefinition>(
                    ChoiceParams(
                        key = "lockVoltage",
                        displayName = "Lock Voltage",
                        values = dto.lockVoltage.values,
                        defaultValue = dto.lockVoltage.defaultValue
                    ), ChoiceParams(
                        key = "lockType",
                        displayName = "Lock Type",
                        values = dto.lockType.values,
                        defaultValue = dto.lockType.defaultValue
                    ), ChoiceParams(
                        key = "lockKick",
                        displayName = "Lock Kick",
                        values = dto.lockKick.values,
                        defaultValue = dto.lockKick.defaultValue
                    ), ChoiceParams(
                        key = "lockRelease",
                        displayName = "Lock Release",
                        values = dto.lockRelease.values,
                        defaultValue = dto.lockRelease.defaultValue
                    ), RangeParams(
                        key = "lockReleaseTime",
                        displayName = "Lock Release Time",
                        min = dto.lockReleaseTime.range.min,
                        max = dto.lockReleaseTime.range.max,
                        unit = dto.lockReleaseTime.unit,
                        defaultValue = dto.lockReleaseTime.defaultValue
                    ), RangeParams(
                        key = "lockAngle",
                        displayName = "Lock Angle",
                        min = dto.lockAngle.range.min,
                        max = dto.lockAngle.range.max,
                        unit = dto.lockAngle.unit,
                        defaultValue = dto.lockAngle.defaultValue
                    )
                )

                definitions.forEach { def ->
                    DoorLeaf.entries.forEach { leaf ->
                        dao.upsert(def.toEntity(leaf))
                    }
                }

                emit(definitions)
            }.flowOn(Dispatchers.IO)
        }.stateIn(
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    init {
        refreshTrigger.tryEmit(Unit)
    }

    override fun getAllDefinitions(): Flow<List<ParamDefinition>> = definitionsFlow

    override fun getSavedValues(leaf: DoorLeaf): Flow<List<ParamValue>> =
        dao.valuesForLeaf(leaf.name).map { list -> list.map(ParamValueEntity::toDomain) }

    override suspend fun saveValue(param: ParamValue) {
        dao.upsert(param.toEntity())
    }

    suspend fun refreshFromApi() {
        refreshTrigger.emit(Unit)
    }
}