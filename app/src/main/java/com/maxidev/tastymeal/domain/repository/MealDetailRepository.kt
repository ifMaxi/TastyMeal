package com.maxidev.tastymeal.domain.repository

import com.maxidev.tastymeal.domain.model.Meal

interface MealDetailRepository {

    suspend fun fetchMealById(id: String): Meal
}