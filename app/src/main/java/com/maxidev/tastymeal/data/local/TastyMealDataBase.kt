package com.maxidev.tastymeal.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.maxidev.tastymeal.data.local.dao.CategoriesDao
import com.maxidev.tastymeal.data.local.dao.FiltersDao
import com.maxidev.tastymeal.data.local.dao.MinimalMealDao
import com.maxidev.tastymeal.data.local.entity.CategoriesEntity
import com.maxidev.tastymeal.data.local.entity.FiltersEntity
import com.maxidev.tastymeal.data.local.entity.MinimalMealEntity

@Database(
    entities = [
        MinimalMealEntity::class,
        CategoriesEntity::class,
        FiltersEntity::class
               ],
    version = 1,
    exportSchema = false
)
abstract class TastyMealDataBase: RoomDatabase() {

    abstract fun minimalMealDao(): MinimalMealDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun filtersDao(): FiltersDao
}