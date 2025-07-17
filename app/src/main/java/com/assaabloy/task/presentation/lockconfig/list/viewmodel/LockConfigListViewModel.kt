package com.assaabloy.task.presentation.lockconfig.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.usecase.list.FilterParamsUseCase
import com.assaabloy.task.presentation.lockconfig.list.state.LockConfigListState
import com.assaabloy.task.presentation.lockconfig.list.state.ParamListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class LockConfigListViewModel @Inject constructor(
    private val filterParams: FilterParamsUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedLeaf = MutableStateFlow(DoorLeaf.PRIMARY)
    val selectedLeaf: StateFlow<DoorLeaf> = _selectedLeaf

    val state: StateFlow<LockConfigListState> = combine(
        _selectedLeaf,
        _searchQuery
    ) { leaf, query ->
        leaf to query
    }
        .flatMapLatest { (leaf, query) ->
            filterParams(leaf, query)
                .map { defsAndValues ->
                    defsAndValues.map { (def, current) ->
                        ParamListItem(
                            definition = def,
                            leaf = leaf,
                            currentValue = current
                        )
                    }
                }
                .map { items ->
                    LockConfigListState(
                        items = items,
                        searchQuery = _searchQuery.value,
                        selectedLeaf = _selectedLeaf.value
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LockConfigListState()
        )

    fun onSearchQueryChange(q: String) {
        _searchQuery.value = q
    }

    fun onLeafSelected(leaf: DoorLeaf) {
        _selectedLeaf.value = leaf
    }
}