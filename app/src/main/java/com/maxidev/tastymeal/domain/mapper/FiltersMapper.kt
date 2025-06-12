package com.maxidev.tastymeal.domain.mapper

import com.maxidev.tastymeal.data.remote.dto.FilterDto
import com.maxidev.tastymeal.domain.model.FilterByCategory

fun FilterDto.asDomain() =
    this.meals?.map { data ->
        FilterByCategory(
            idMeal = data?.idMeal.orEmpty(),
            strMeal = data?.strMeal.orEmpty(),
            strMealThumb = data?.strMealThumb.orEmpty()
        )
    }