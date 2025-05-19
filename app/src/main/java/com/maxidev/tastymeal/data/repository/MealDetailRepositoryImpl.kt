package com.maxidev.tastymeal.data.repository

import com.maxidev.tastymeal.data.remote.TastyMealApiService
import com.maxidev.tastymeal.domain.mapper.asDomain
import com.maxidev.tastymeal.domain.model.Meal
import com.maxidev.tastymeal.domain.repository.MealDetailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MealDetailRepositoryImpl @Inject constructor(
    private val api: TastyMealApiService
): MealDetailRepository {

    override suspend fun fetchMealById(id: String): Meal =
        withContext(Dispatchers.IO) {
            api.getMealById(id).asDomain()
                .first()
        }
}