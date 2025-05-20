package com.maxidev.tastymeal.domain.repository

import com.maxidev.tastymeal.domain.model.Meal
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    fun getAllBookmarks(): Flow<List<Meal>>

    fun getBookmarkById(id: String): Flow<Meal>

    fun isBookmark(id: String): Flow<Boolean>

    suspend fun upsertBookmark(meal: Meal)

    suspend fun deleteBookmark(meal: Meal)

    suspend fun clearAll()
}