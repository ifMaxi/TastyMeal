@file:OptIn(ExperimentalMaterial3Api::class)

package com.maxidev.tastymeal.presentation.settings

import android.content.Intent
import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maxidev.tastymeal.data.datastore.TypeTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val dialogVisible by viewModel.dialogVisible.collectAsStateWithLifecycle()
    val isDynamic by viewModel.isDynamic.collectAsStateWithLifecycle()
    val themeType by viewModel.isTypeTheme.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    ScreenContent(
        isDynamic = isDynamic,
        scrollBehavior = scrollBehavior,
        onVisibility = { viewModel.setDialogVisible(true) },
        updateDynamicTheme = { viewModel.updateIsDynamicTheme() }
    )

    if (dialogVisible) {
        ThemesDialog(
            themeState = themeType,
            onVisibility = { viewModel.setDialogVisible(false) },
            updateThemeType = { viewModel.updateIsDarkTheme(it) }
        )
    }
}

@Composable
private fun ScreenContent(
    isDynamic: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    onVisibility: (Boolean) -> Unit,
    updateDynamicTheme: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val browserIntent = Intent(Intent.ACTION_VIEW, "https://github.com/ifMaxi".toUri())

    // TODO: Add more settings !
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Settings")
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Appearance",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(horizontal = 16.dp)
            )
            Card(
                modifier = Modifier.padding(16.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(5)
            ) {
                ListItem(
                    headlineContent = { Text(text = "Theme") },
                    trailingContent = {
                        IconButton(onClick = { onVisibility(true) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Change app theme."
                            )
                        }
                    }
                )
                HorizontalDivider()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ListItem(
                        headlineContent = { Text(text = "Dynamic color") },
                        trailingContent = {
                            Switch(
                                checked = (isDynamic),
                                onCheckedChange = updateDynamicTheme
                            )
                        },
                        modifier = Modifier.clickable { updateDynamicTheme(!isDynamic) }
                    )
                }
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text(text = "GitHub") },
                    trailingContent = {
                        IconButton(onClick = { context.startActivity(browserIntent) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                contentDescription = "Open GitHub."
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ThemesDialog(
    themeState: SettingsState,
    onVisibility: (Boolean) -> Unit,
    updateThemeType: (TypeTheme) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onVisibility(false) },
        title = {
            Text(
                text = "Choose theme"
            )
        },
        text = {
            RadioButtons(
                state = themeState,
                updateThemeType = updateThemeType
            )
        },
        confirmButton = {
            TextButton(onClick = { onVisibility(false) }) {
                Text(
                    text = "Confirm",
                )
            }
        }
    )
}

@Composable
private fun RadioButtons(
    state: SettingsState,
    updateThemeType: (TypeTheme) -> Unit
) {
    Column(horizontalAlignment = Alignment.Start) {
        state.radioItems.forEach {
            Row(
                modifier = Modifier.selectable(
                    selected = (it.value == state.selectedRadio),
                    onClick = { updateThemeType(it.value) }
                ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (it.value == state.selectedRadio),
                    onClick = { updateThemeType(it.value) }
                )
                Text(
                    text = it.title
                )
            }
        }
    }
}