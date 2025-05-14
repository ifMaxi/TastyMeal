package com.maxidev.tastymeal.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class FilterDto(
    val meals: List<Meal?>? = listOf()
) {
    @Serializable
    data class Meal(
        val strMeal: String? = "",
        val strMealThumb: String? = "",
        val idMeal: String? = ""
    )
}