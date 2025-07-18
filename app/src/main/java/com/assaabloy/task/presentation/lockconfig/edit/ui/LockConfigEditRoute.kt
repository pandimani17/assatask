package com.assaabloy.task.presentation.lockconfig.edit.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.assaabloy.task.presentation.lockconfig.edit.viewmodel.LockConfigEditViewModel

@Composable
fun LockConfigEditRoute(
    paramKey: String,
    onBack: () -> Unit,
    viewModel: LockConfigEditViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when {
        state.isLoading -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }
        }
        state.error != null -> {
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Error: ${state.error}") }
        }
        else -> {
            val def = state.definition!!
            LockConfigEditScreen(
                definition        = def,
                primaryValue      = state.primaryValue,
                onPrimaryChange   = viewModel::onPrimaryChange,
                secondaryValue    = state.secondaryValue,
                onSecondaryChange = viewModel::onSecondaryChange,
                onBack            = onBack,
                onSave            = { viewModel.onSave(onBack) }
            )
        }
    }
}