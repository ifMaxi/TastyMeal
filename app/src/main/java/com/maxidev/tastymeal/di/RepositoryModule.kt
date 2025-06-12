package com.maxidev.tastymeal.di

import com.maxidev.tastymeal.data.repository.BookmarkRepositoryImpl
import com.maxidev.tastymeal.data.repository.FiltersRepositoryImpl
import com.maxidev.tastymeal.data.repository.HomeRepositoryImpl
import com.maxidev.tastymeal.data.repository.MealDetailRepositoryImpl
import com.maxidev.tastymeal.data.repository.RecipeRepositoryImpl
import com.maxidev.tastymeal.data.repository.SearchMealRepositoryImpl
import com.maxidev.tastymeal.domain.repository.BookmarkRepository
import com.maxidev.tastymeal.domain.repository.FiltersRepository
import com.maxidev.tastymeal.domain.repository.HomeRepository
import com.maxidev.tastymeal.domain.repository.MealDetailRepository
import com.maxidev.tastymeal.domain.repository.RecipeRepository
import com.maxidev.tastymeal.domain.repository.SearchMealRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository

    @Binds
    abstract fun bindSearchMealRepository(impl: SearchMealRepositoryImpl): SearchMealRepository

    @Binds
    abstract fun bindMealDetailRepository(impl: MealDetailRepositoryImpl): MealDetailRepository

    @Binds
    abstract fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    abstract fun bindFiltersRepository(impl: FiltersRepositoryImpl): FiltersRepository
}