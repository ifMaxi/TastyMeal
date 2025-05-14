package com.maxidev.tastymeal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.tastymeal.utils.FILTERS_TABLE

@Entity(tableName = FILTERS_TABLE)
data class FiltersEntity(
    @PrimaryKey(autoGenerate = false) val idMeal: String,
    @ColumnInfo val strMeal: String,
    @ColumnInfo val strMealThumb: String
)