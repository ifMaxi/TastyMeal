package com.maxidev.tastymeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.maxidev.tastymeal.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getAllRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipe WHERE id = :id")
    fun getRecipeById(id: Long): Flow<RecipeEntity>

    @Upsert
    suspend fun upsertRecipe(entity: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(entity: RecipeEntity)
}