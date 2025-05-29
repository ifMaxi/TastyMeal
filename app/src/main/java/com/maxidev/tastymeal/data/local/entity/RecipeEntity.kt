package com.maxidev.tastymeal.data.local.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipe")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val title: String,
    @ColumnInfo val image: Uri?,
    @ColumnInfo val cameraImage: Uri?,
    @ColumnInfo val instructions: String,
    @ColumnInfo val ingredients: List<String>,
    @ColumnInfo val measures: List<String>
)