package com.assaabloy.task.presentation.lockconfig.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.usecase.list.FilterParamsUseCase
import com.assaabloy.task.domain.lockconfig.usecase.list.RefreshConfigUseCase
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockConfigListViewModel @Inject constructor(
    private val filterParams: FilterParamsUseCase,
    private val refreshConfig: RefreshConfigUseCase

) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val _searchQuery = MutableStateFlow("")

    private val _selectedLeaf = MutableStateFlow(DoorLeaf.PRIMARY)

    val state: StateFlow<LockConfigListState> = combine(
        _selectedLeaf, _searchQuery
    ) { leaf, query ->
        leaf to query
    }.flatMapLatest { (leaf, query) ->
            filterParams(leaf, query).map { defsAndValues ->
                    defsAndValues.map { (def, current) ->
                        ParamListItem(
                            definition = def, leaf = leaf, currentValue = current
                        )
                    }
                }.map { items ->
                    LockConfigListState(
                        items = items,
                        searchQuery = _searchQuery.value,
                        selectedLeaf = _selectedLeaf.value
                    )
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LockConfigListState()
        )

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            refreshConfig()
            _isRefreshing.value = false
        }
    }

    fun onSearchQueryChange(filterQuery: String) {
        _searchQuery.value = filterQuery
    }

    fun onLeafSelected(leaf: DoorLeaf) {
        _selectedLeaf.value = leaf
    }
}