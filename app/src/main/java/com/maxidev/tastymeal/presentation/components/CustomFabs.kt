package com.maxidev.tastymeal.presentation.components

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape

@Composable
fun CustomExtendedFab(
    onClick: () -> Unit,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    // Custom options
    shape: Shape = FloatingActionButtonDefaults.extendedFabShape,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation()
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        shape = shape,
        elevation = elevation,
        icon = icon,
        text = text
    )
}

@Composable
fun CustomFab(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    // Custom options
    shape: Shape = FloatingActionButtonDefaults.shape,
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation()
) {
    FloatingActionButton(
        onClick = onClick,
        shape = shape,
        elevation = elevation,
        content = icon
    )
}