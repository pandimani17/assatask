package com.assaabloy.task.presentation.lockconfig.list.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.assaabloy.task.domain.lockconfig.model.ChoiceParams
import com.assaabloy.task.domain.lockconfig.model.DoorLeaf
import com.assaabloy.task.domain.lockconfig.model.RangeParams
import com.assaabloy.task.presentation.lockconfig.list.state.ParamListItem


@Composable
fun LockConfigListScreen(
    items: List<ParamListItem>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedLeaf: DoorLeaf,
    onLeafChange: (DoorLeaf) -> Unit,
    onEdit: (paramKey: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()  ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        LeafToggle(
            selected = selectedLeaf,
            onToggle = onLeafChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
        HorizontalDivider()
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                ParameterRow(
                    displayName = item.definition.displayName,
                    value = item.currentValue,
                    onClick = { onEdit(item.definition.key) })
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    val focus = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search parameters") },
        singleLine = true,
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search icon")
        },
        keyboardActions = KeyboardActions(
            onDone = { focus.clearFocus() }))
}

@Composable
private fun LeafToggle(
    selected: DoorLeaf, onToggle: (DoorLeaf) -> Unit, modifier: Modifier = Modifier
) {
    TabRow(
        selectedTabIndex = selected.ordinal, modifier = modifier
    ) {
        DoorLeaf.values().forEachIndexed { index, leaf ->
            Tab(
                selected = leaf == selected,
                onClick = { onToggle(leaf) },
                text = { Text(leaf.name.lowercase().replaceFirstChar { it.uppercase() }) })
        }
    }
}

@Composable
private fun ParameterRow(
    displayName: String, value: String, onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = displayName, style = MaterialTheme.typography.titleMedium)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
        Icon(
            Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewListScreen() {
    val sampleItems = listOf(
        ParamListItem(
            definition = ChoiceParams(
                key = "lockVoltage",
                displayName = "Lock Voltage",
                values = listOf("No Lock", "12V", "24V"),
                defaultValue = "No Lock"
            ), leaf = DoorLeaf.PRIMARY, currentValue = "12V"
        ), ParamListItem(
            definition = RangeParams(
                key = "lockAngle",
                displayName = "Lock Angle",
                min = 65f,
                max = 125f,
                unit = "degrees",
                defaultValue = "90"
            ), leaf = DoorLeaf.SECONDARY, currentValue = "100"
        )
    )
    LockConfigListScreen(
        items = sampleItems,
        searchQuery = "",
        onSearchQueryChange = {},
        selectedLeaf = DoorLeaf.PRIMARY,
        onLeafChange = {},
        onEdit = {})
}