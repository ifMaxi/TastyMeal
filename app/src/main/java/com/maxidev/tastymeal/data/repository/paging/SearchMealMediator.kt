package com.maxidev.tastymeal.data.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.maxidev.tastymeal.data.local.TastyMealDataBase
import com.maxidev.tastymeal.data.local.entity.MinimalMealEntity
import com.maxidev.tastymeal.data.remote.TastyMealApiService
import com.maxidev.tastymeal.domain.mapper.asEntity
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class SearchMealMediator(
    private val api: TastyMealApiService,
    private val dataBase: TastyMealDataBase,
    private val query: String,
    //private val queryByLetter: String
): RemoteMediator<Int, MinimalMealEntity>() {

    private val dao = dataBase.minimalMealDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MinimalMealEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND ->
                return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()

                if (lastItem == null) {
                    return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }

                lastItem.idMeal
            }
        }

        return try {

            val perPage = state.config.pageSize
            val response = api.getSearchMeals(
                page = page,
                query = query,
                //firstLetter = queryByLetter
            )
            val endOfPagination = response.meals.size < perPage

            dataBase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dao.clearData()
                }

                val mapResponse = response.asEntity()

                dao.upsertData(mapResponse)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        }
    }
}