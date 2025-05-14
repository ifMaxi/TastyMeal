package com.maxidev.tastymeal.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.maxidev.tastymeal.data.local.TastyMealDataBase
import com.maxidev.tastymeal.data.local.entity.MinimalMealEntity
import com.maxidev.tastymeal.data.remote.TastyMealApiService
import com.maxidev.tastymeal.data.repository.paging.SearchMealMediator
import com.maxidev.tastymeal.domain.mapper.asDomain
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.domain.repository.SearchMealRepository
import com.maxidev.tastymeal.utils.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class SearchMealRepositoryImpl @Inject constructor(
    private val api: TastyMealApiService,
    private val dataBase: TastyMealDataBase
): SearchMealRepository {

    private val dao = dataBase.minimalMealDao()

    override fun fetchSearchedMeals(
        query: String,
        //queryByLetter: String?
    ): Flow<PagingData<MinimalMeal>> {
        val pagingSourceFactory = { dao.pagingSource(query) }
        val remoteMediator = SearchMealMediator(
            api = api,
            dataBase = dataBase,
            query = query,
            //queryByLetter = queryByLetter.orEmpty()
        )
        val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = true
        )

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = pagingSourceFactory,
            remoteMediator = remoteMediator
        ).flow
            .map { pagingData -> pagingData.map(MinimalMealEntity::asDomain) }
            .flowOn(Dispatchers.IO)
    }
}