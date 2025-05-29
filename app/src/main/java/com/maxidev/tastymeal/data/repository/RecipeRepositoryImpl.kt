package com.maxidev.tastymeal.data.repository

import com.maxidev.tastymeal.data.local.dao.RecipeDao
import com.maxidev.tastymeal.data.local.entity.RecipeEntity
import com.maxidev.tastymeal.domain.mapper.asDomain
import com.maxidev.tastymeal.domain.mapper.asEntity
import com.maxidev.tastymeal.domain.model.Recipe
import com.maxidev.tastymeal.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val dao: RecipeDao
): RecipeRepository {

    override fun fetchAllRecipes(): Flow<List<Recipe>> =
        dao.getAllRecipes()
            .map { it.map(RecipeEntity::asDomain) }
            .flowOn(Dispatchers.IO)

    override fun fetchRecipeById(id: Long): Flow<Recipe> =
        dao.getRecipeById(id)
            .map(RecipeEntity::asDomain)
            .flowOn(Dispatchers.IO)

    override suspend fun upsertRecipe(recipe: Recipe) =
        withContext(Dispatchers.IO) {
            dao.upsertRecipe(recipe.asEntity())
        }

    override suspend fun deleteRecipe(recipe: Recipe) =
        withContext(Dispatchers.IO) {
            dao.deleteRecipe(recipe.asEntity())
        }
}