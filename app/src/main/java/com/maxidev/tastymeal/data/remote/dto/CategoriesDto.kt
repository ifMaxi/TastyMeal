package com.maxidev.tastymeal.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoriesDto(
    val categories: List<Category?>? = listOf()
) {
    @Serializable
    data class Category(
        val idCategory: String? = "",
        val strCategory: String? = "",
        val strCategoryThumb: String? = "",
        val strCategoryDescription: String? = ""
    )
}