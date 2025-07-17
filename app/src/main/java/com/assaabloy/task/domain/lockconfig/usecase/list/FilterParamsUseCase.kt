package com.assaabloy.task.domain.lockconfig.usecase.list

import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FilterParamsUseCase @Inject constructor(
    private val getDefs: GetParamDefinitionsUseCase, private val getVals: GetParamValuesUseCase
) {
    operator fun invoke(
        leaf: DoorLeaf, query: String
    ): Flow<List<Pair<ParamDefinition, String>>> = combine(
        getDefs(), getVals(leaf)
    ) { defs, vals ->
        defs.map { def ->
            val current = vals.firstOrNull { it.key == def.key }?.value ?: def.defaultValue
            def to current
        }
    }.map { list ->
        if (query.isBlank()) list
        else list.filter { (def, value) ->
            def.displayName.contains(query, ignoreCase = true) || value.contains(
                query,
                ignoreCase = true
            )
        }
    }
}