package com.maxidev.tastymeal.domain.repository

import com.maxidev.tastymeal.domain.model.FilterByCategory

interface FiltersRepository {

    suspend fun fetchFilterByCategory(category: String): List<FilterByCategory>
}