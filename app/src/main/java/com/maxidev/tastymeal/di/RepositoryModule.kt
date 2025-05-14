package com.maxidev.tastymeal.di

import com.maxidev.tastymeal.data.repository.HomeRepositoryImpl
import com.maxidev.tastymeal.data.repository.SearchMealRepositoryImpl
import com.maxidev.tastymeal.domain.repository.HomeRepository
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
}