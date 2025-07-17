package com.assaabloy.task.presentation.lockconfig.list.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.assaabloy.task.presentation.lockconfig.list.viewmodel.LockConfigListViewModel

@Composable
fun LockConfigListRoute(
    viewModel: LockConfigListViewModel = hiltViewModel(),
    onEdit: (paramKey: String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LockConfigListScreen(
        items = state.items,
        searchQuery = state.searchQuery,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        selectedLeaf = state.selectedLeaf,
        onLeafChange = viewModel::onLeafSelected,
        onEdit = onEdit
    )
}