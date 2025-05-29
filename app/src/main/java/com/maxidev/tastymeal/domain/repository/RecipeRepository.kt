package com.maxidev.tastymeal.domain.repository

import com.maxidev.tastymeal.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun fetchAllRecipes(): Flow<List<Recipe>>

    fun fetchRecipeById(id: Long): Flow<Recipe>

    suspend fun upsertRecipe(recipe: Recipe)

    suspend fun deleteRecipe(recipe: Recipe)
}