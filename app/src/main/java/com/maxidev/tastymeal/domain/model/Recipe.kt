package com.maxidev.tastymeal.domain.model

import android.net.Uri

data class Recipe(
    val id: Long,
    val title: String,
    val image: Uri,
    val cameraImage: Uri,
    val instructions: String,
    val ingredientsAndMeasures: String
)