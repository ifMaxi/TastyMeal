package com.maxidev.tastymeal.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MinimalMealDto(
    val idMeal: String? = "",
    val strMeal: String? = "",
    val strMealThumb: String? = "",
    val strCategory: String? = ""
)