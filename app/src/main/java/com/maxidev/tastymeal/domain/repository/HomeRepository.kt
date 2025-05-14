package com.maxidev.tastymeal.domain.repository

import com.maxidev.tastymeal.domain.model.CategoriesMeal
import com.maxidev.tastymeal.domain.model.MinimalMeal
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    suspend fun fetchRandomMeal(): List<MinimalMeal>

    fun fetchCategories(): Flow<List<CategoriesMeal>>

//    fun fetchMealByCountry(country: String): Flow<List<FilterByCountryMeal>>
}