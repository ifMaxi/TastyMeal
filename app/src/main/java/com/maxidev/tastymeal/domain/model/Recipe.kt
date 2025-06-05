package com.maxidev.tastymeal.domain.model

import android.net.Uri

data class Recipe(
    val id: Long = 0L,
    val title: String = "",
    val image: Uri = Uri.EMPTY,
    val cameraImage: Uri = Uri.EMPTY,
    val instructions: String = "",
    val ingredientsAndMeasures: String = ""
)