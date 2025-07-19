package com.assaabloy.task.presentation.lockconfig.edit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assaabloy.task.domain.lockconfig.model.ChoiceParams
import com.assaabloy.task.domain.lockconfig.model.ParamDefinition
import com.assaabloy.task.domain.lockconfig.model.RangeParams
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockConfigEditScreen(
    definition: ParamDefinition,
    primaryValue: String,
    onPrimaryChange: (String) -> Unit,
    secondaryValue: String,
    onSecondaryChange: (String) -> Unit,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val focus = LocalFocusManager.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(definition.displayName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        focus.clearFocus()
                        onSave()
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding).verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            ParameterEditorCard(
                title = "Primary Door",
                definition = definition,
                value = primaryValue,
                onValueChange = onPrimaryChange
            )
            Spacer(modifier = Modifier.height(24.dp))
            ParameterEditorCard(
                title = "Secondary Door",
                definition = definition,
                value = secondaryValue,
                onValueChange = onSecondaryChange
            )
        }
    }
}

@Composable
private fun ParameterEditorCard(
    title: String,
    definition: ParamDefinition,
    value: String,
    onValueChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            when (definition) {
                is ChoiceParams -> ChoiceEditor(
                    options = definition.values,
                    selected = value,
                    onSelect = onValueChange
                )
                is RangeParams -> RangeEditor(
                    min = definition.min,
                    max = definition.max,
                    unit = definition.unit,
                    value = value.toFloatOrNull() ?: definition.defaultValue.toFloat(),
                    onValueChange = { onValueChange(it.toString()) }
                )
            }
        }
    }
}

@Composable
private fun ChoiceEditor(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selected),
                        onClick = { onSelect(option) }
                    )
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selected),
                    onClick = { onSelect(option) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun RangeEditor(
    min: Float,
    max: Float,
    unit: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Slider(
            value = value,
            onValueChange = { decimal ->
                val rounded = (decimal * 10f).roundToInt() / 10f
                val clamped = rounded.coerceIn(min, max)
                onValueChange(clamped)
            },
            valueRange = min..max,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = String.format("%.1f %s", value, unit),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEditChoice() {
    LockConfigEditScreen(
        definition = ChoiceParams(
            key = "lockType",
            displayName = "Lock Type",
            values = listOf("Lock with power", "Lock without power"),
            defaultValue = "Lock with power"
        ),
        primaryValue = "Lock with power",
        onPrimaryChange = {},
        secondaryValue = "Lock without power",
        onSecondaryChange = {},
        onBack = {},
        onSave = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewEditRange() {
    LockConfigEditScreen(
        definition = RangeParams(
            key = "lockAngle",
            displayName = "Lock Angle",
            min = 65f,
            max = 125f,
            unit = "°",
            defaultValue = "90"
        ),
        primaryValue = "90",
        onPrimaryChange = {},
        secondaryValue = "100",
        onSecondaryChange = {},
        onBack = {},
        onSave = {}
    )
}