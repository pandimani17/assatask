package com.assaabloy.task.presentation.lockconfig.edit.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.ParamValue
import com.assaabloy.task.domain.lockconfig.usecase.edit.SaveParamValueUseCase
import com.assaabloy.task.domain.lockconfig.usecase.list.GetParamDefinitionsUseCase
import com.assaabloy.task.domain.lockconfig.usecase.list.GetParamValuesUseCase
import com.assaabloy.task.presentation.lockconfig.edit.state.LockConfigEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LockConfigEditViewModel @Inject constructor(
    private val getDefs: GetParamDefinitionsUseCase,
    private val getVals: GetParamValuesUseCase,
    private val save: SaveParamValueUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val paramKey: String = checkNotNull(savedStateHandle["paramKey"])

    private val _state = MutableStateFlow(LockConfigEditState())
    val state = _state.asStateFlow()

    init { load() }

    private fun load() {
        viewModelScope.launch {
            try {
                // 1) find the definition for this key
                val defs = getDefs().first()
                val def = defs.first { it.key == paramKey }

                // 2) load saved values or defaults for both leaves
                val primVals = getVals(DoorLeaf.PRIMARY).first()
                val secVals  = getVals(DoorLeaf.SECONDARY).first()

                val prim = primVals.firstOrNull { it.key == paramKey }?.value
                    ?: def.defaultValue
                val sec  = secVals .firstOrNull { it.key == paramKey }?.value
                    ?: def.defaultValue

                _state.value = LockConfigEditState(
                    definition      = def,
                    primaryValue    = prim,
                    secondaryValue  = sec,
                    isLoading       = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun onPrimaryChange(v: String) {
        _state.value = _state.value.copy(primaryValue = v)
    }

    fun onSecondaryChange(v: String) {
        _state.value = _state.value.copy(secondaryValue = v)
    }

    fun onSave(onDone: () -> Unit) {
        val def = _state.value.definition ?: return
        viewModelScope.launch {
            // persist both leaves
            save(ParamValue(def.key, DoorLeaf.PRIMARY,   _state.value.primaryValue))
            save(ParamValue(def.key, DoorLeaf.SECONDARY, _state.value.secondaryValue))
            onDone()
        }
    }
}