package com.maxidev.tastymeal.domain.mapper

import com.maxidev.tastymeal.data.local.entity.CategoriesEntity
import com.maxidev.tastymeal.data.remote.dto.CategoriesDto
import com.maxidev.tastymeal.domain.model.CategoriesMeal

fun CategoriesDto.asEntity() =
    this.categories?.map { data ->
        CategoriesEntity(
            idMeal = data?.idCategory.orEmpty(),
            strName = data?.strCategory.orEmpty(),
            strThumb = data?.strCategoryThumb.orEmpty()
        )
    }

fun CategoriesEntity.asDomain() =
    CategoriesMeal(
        idMeal = idMeal,
        strName = strName,
        strThumb = strThumb
    )