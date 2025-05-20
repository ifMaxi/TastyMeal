package com.maxidev.tastymeal.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal")
data class MealEntity(
    @PrimaryKey(autoGenerate = false) val idMeal: String,
    @ColumnInfo val strMeal: String,
    @ColumnInfo val strMealThumb: String,
    @ColumnInfo val strCategory: String,
    @ColumnInfo val strInstructions: String,
    @ColumnInfo val strTags: String,
    @ColumnInfo val strYouTube: String,
    @ColumnInfo val strSource: String,
    @ColumnInfo val strIngredients: List<String>,
    @ColumnInfo val strMeasure: List<String>,

    @Embedded val bookmark: BookMarkEntity
)

data class BookMarkEntity(@ColumnInfo val bookmarked: Boolean)