package com.maxidev.tastymeal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.maxidev.tastymeal.data.local.dao.BookmarkDao
import com.maxidev.tastymeal.data.local.dao.CategoriesDao
import com.maxidev.tastymeal.data.local.dao.FiltersDao
import com.maxidev.tastymeal.data.local.dao.MinimalMealDao
import com.maxidev.tastymeal.data.local.entity.CategoriesEntity
import com.maxidev.tastymeal.data.local.entity.FiltersEntity
import com.maxidev.tastymeal.data.local.entity.MealEntity
import com.maxidev.tastymeal.data.local.entity.MinimalMealEntity
import com.maxidev.tastymeal.utils.ConvertersUtils

@Database(
    entities = [
        MinimalMealEntity::class,
        CategoriesEntity::class,
        FiltersEntity::class,
        MealEntity::class
               ],
    version = 1,
    exportSchema = false
)
@TypeConverters(ConvertersUtils::class)
abstract class TastyMealDataBase: RoomDatabase() {

    abstract fun minimalMealDao(): MinimalMealDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun filtersDao(): FiltersDao
    abstract fun bookmarkDao(): BookmarkDao
}