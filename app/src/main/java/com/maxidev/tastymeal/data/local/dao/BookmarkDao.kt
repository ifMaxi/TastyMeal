package com.maxidev.tastymeal.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.maxidev.tastymeal.data.local.entity.MealEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Query("SELECT * FROM meal")
    fun getBookmark(): Flow<List<MealEntity>>

    @Query("SELECT * FROM meal WHERE idMeal = :id")
    fun getBookmarkById(id: String): Flow<MealEntity>

    @Query("SELECT bookmarked FROM meal WHERE idMeal = :id")
    fun isBookmarked(id: String): Flow<Boolean>

    @Query("DELETE FROM meal")
    suspend fun clearAll()

    @Upsert
    suspend fun upsertBookmark(entity: MealEntity)

    @Delete
    suspend fun deleteBookmark(entity: MealEntity)
}