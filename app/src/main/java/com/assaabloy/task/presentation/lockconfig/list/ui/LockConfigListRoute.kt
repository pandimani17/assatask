package com.assaabloy.task.presentation.lockconfig.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.assaabloy.task.presentation.lockconfig.list.viewmodel.LockConfigListViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LockConfigListRoute(
    viewModel: LockConfigListViewModel = hiltViewModel(), onEdit: (paramKey: String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing, onRefresh = viewModel::refresh
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LockConfigListScreen(
            items = state.items,
            searchQuery = state.searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange,
            selectedLeaf = state.selectedLeaf,
            onLeafChange = viewModel::onLeafSelected,
            onEdit = onEdit
        )

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}