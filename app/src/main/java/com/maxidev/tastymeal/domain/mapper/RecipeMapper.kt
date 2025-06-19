package com.maxidev.tastymeal.domain.mapper

import android.net.Uri
import com.maxidev.tastymeal.data.local.entity.RecipeEntity
import com.maxidev.tastymeal.domain.model.Recipe

fun RecipeEntity.asDomain() =
    Recipe(
        id = id,
        title = title,
        image = image ?: Uri.EMPTY,
        cameraImage = cameraImage ?: Uri.EMPTY,
        instructions = instructions,
        ingredientsAndMeasures = ingredientsAndMeasures,
        portions = portions,
        preparationTime = preparationTime,
        cookingTime = cookingTime,
        source = source,
        notes = notes
    )

fun Recipe.asEntity() =
    RecipeEntity(
        id = id,
        title = title,
        image = image,
        cameraImage = cameraImage,
        instructions = instructions,
        ingredientsAndMeasures = ingredientsAndMeasures,
        portions = portions,
        preparationTime = preparationTime,
        cookingTime = cookingTime,
        source = source,
        notes = notes
    )