package com.maxidev.tastymeal.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.maxidev.tastymeal.data.local.entity.FiltersEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FiltersDao {

    @Query("SELECT * FROM FILTERS_TABLE")
    fun getFilters(): Flow<List<FiltersEntity>>

    @Upsert
    suspend fun upsertAll(filters: List<FiltersEntity>)
}