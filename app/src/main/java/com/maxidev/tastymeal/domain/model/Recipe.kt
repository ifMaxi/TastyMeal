package com.maxidev.tastymeal.domain.model

import android.net.Uri

data class Recipe(
    val id: Long = 0,
    val title: String = "",
    val portions: String = "",
    val preparationTime: String = "",
    val cookingTime: String = "",
    val source: String = "",
    val image: Uri = Uri.EMPTY,
    val cameraImage: Uri = Uri.EMPTY,
    val instructions: String = "",
    val ingredientsAndMeasures: String = "",
    val notes: String = ""
)