@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.bookmark

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DeleteSweep
import androidx.compose.material.icons.rounded.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.tastymeal.domain.model.Meal
import com.maxidev.tastymeal.presentation.components.CustomAsyncImage
import com.maxidev.tastymeal.presentation.components.CustomIconButton
import com.maxidev.tastymeal.presentation.theme.TastyMealTheme
import kotlinx.coroutines.launch

@Composable
fun BookmarkScreen(
    viewModel: BookmarkViewModel = hiltViewModel(),
    navigateToDetailOffline: (String) -> Unit
) {
    val state by viewModel.getBookmarks.collectAsStateWithLifecycle()

    BookmarkContent(
        allBookmarks = state.allBookmarks,
        onClearAll = { viewModel.clearAllBookmarks() },
        navigateToDetailOffline = navigateToDetailOffline
    )
}

@Composable
private fun BookmarkContent(
    allBookmarks: List<Meal>,
    onClearAll: () -> Unit,
    navigateToDetailOffline: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var openDialog by remember { mutableStateOf(false) }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackBarHostState) }) { innerPadding ->
        val lazyState = rememberLazyListState()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .consumeWindowInsets(innerPadding),
            state = lazyState,
            contentPadding = innerPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                HeaderTitleWithButton(onClick = { openDialog = true })
            }
            item { Spacer(modifier = Modifier.size(20.dp)) }

            items(
                items = allBookmarks,
                key = { it.idMeal }
            ) {
                BookmarkedItem(
                    strMealThumb = it.strMealThumb,
                    strMeal = it.strMeal,
                    strCategory = it.strCategory,
                    onClick = { navigateToDetailOffline(it.idMeal) }
                )
                Spacer(modifier = Modifier.size(16.dp))
            }
        }

        if (openDialog) {
            AlertDialog(
                onDismissRequest = { openDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onClearAll()
                            coroutineScope.launch {
                                snackBarHostState.showSnackbar(
                                    message = "Bookmarks cleared!",
                                    duration = SnackbarDuration.Indefinite,
                                    actionLabel = "Dismiss"
                                )
                            }
                            openDialog = false
                        },
                        content = { Text(text = "Confirm") }
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = { openDialog = false },
                        content = { Text(text = "Dismiss") }
                    )
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.WarningAmber,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = "Warning!"
                    )
                },
                text = {
                    Text(
                        text = "Are you sure you want to clear all bookmarks?",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                },
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
private fun HeaderTitleWithButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .wrapContentHeight(Alignment.Top)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Bookmarks",
            style = TextStyle(
                fontWeight = FontWeight.Light,
                fontSize = 26.sp,
                textAlign = TextAlign.Start,
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.scrim,
                    blurRadius = 1f
                )
            ),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        CustomIconButton(
            imageVector = Icons.Rounded.DeleteSweep,
            contentDescription = "Clear all bookmarks",
            onClick = onClick
        )
    }
}

/* ----- Bookmark item ----- */
@Composable
fun BookmarkedItem(
    strMealThumb: String,
    strMeal: String,
    strCategory: String,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.padding(horizontal = 16.dp),
        onClick = onClick,
        elevation = CardDefaults.outlinedCardElevation(6.dp),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomAsyncImage(
                model = strMealThumb,
                contentDescription = strMeal,
                height = 100.dp,
                modifier = Modifier.clip(RoundedCornerShape(10.dp))
            )
            Column(
                modifier = Modifier.fillMaxHeight(0.5f).weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = strMeal,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
                Text(
                    text = strCategory,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun BookmarkedItemPreview() {
    TastyMealTheme {
        BookmarkedItem(
            strMealThumb = "image", strMeal = "Salad with chicken", strCategory = "Beef",
            onClick = {}
        )
    }
}