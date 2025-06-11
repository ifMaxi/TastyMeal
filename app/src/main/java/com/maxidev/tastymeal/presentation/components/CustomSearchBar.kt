package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    // Custom options
    placeholder: @Composable (() -> Unit)? = { Text("Search") },
    leadingIcon: @Composable (() -> Unit)? = { Icon(Icons.Default.Search, contentDescription = "Search") },
    trailingIcon: @Composable (() -> Unit)? = {
        if (query.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear",
                modifier = Modifier.clickable { onQueryChange("") }
            )
        }
    }
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch(query)
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon
            )
        }

    Box(
        modifier = modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .imePadding()
            .semantics { isTraversalGroup = false }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .semantics { traversalIndex = 0f },
            inputField = inputField,
            expanded = false,
            onExpandedChange = { expanded = false },
            shape = RoundedCornerShape(10.dp),
            content = content
        )
    }
}