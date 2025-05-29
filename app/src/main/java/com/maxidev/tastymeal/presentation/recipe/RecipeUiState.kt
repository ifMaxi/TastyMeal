package com.maxidev.tastymeal.presentation.recipe

import com.maxidev.tastymeal.domain.model.Recipe

data class RecipeUiState(
    val recipes: List<Recipe> = emptyList()
)