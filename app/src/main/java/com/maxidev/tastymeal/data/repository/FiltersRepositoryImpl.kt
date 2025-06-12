package com.maxidev.tastymeal.data.repository

import com.maxidev.tastymeal.data.remote.TastyMealApiService
import com.maxidev.tastymeal.domain.mapper.asDomain
import com.maxidev.tastymeal.domain.model.FilterByCategory
import com.maxidev.tastymeal.domain.repository.FiltersRepository
import javax.inject.Inject

class FiltersRepositoryImpl @Inject constructor(
    private val api: TastyMealApiService
): FiltersRepository {

    override suspend fun fetchFilterByCategory(category: String): List<FilterByCategory> =
        api.getFilters(categories = category)
            .asDomain() ?: emptyList()
}