package com.maxidev.tastymeal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.maxidev.tastymeal.utils.CATEGORIES_TABLE

@Entity(tableName = CATEGORIES_TABLE)
data class CategoriesEntity(
    @PrimaryKey(autoGenerate = false) val idMeal: String,
    @ColumnInfo val strName: String,
    @ColumnInfo val strThumb: String
)