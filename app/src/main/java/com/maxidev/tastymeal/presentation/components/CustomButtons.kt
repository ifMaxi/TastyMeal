package com.maxidev.tastymeal.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/* ----- Custom icon buttons ----- */

@Composable
fun CustomIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    // Custom options
    tint: Color = LocalContentColor.current,
    modifier: Modifier = Modifier
) {
    FilledTonalIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint
        )
    }
}

/* ----- Custom buttons ----- */

@Composable
fun CustomButton(
    imageVector: ImageVector,
    name: String,
    contentDescription: String,
    onClick: () -> Unit,
    // Custom options
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
        Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = name
        )
    }
}