package com.maxidev.tastymeal.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.tastymeal.domain.model.MinimalMeal
import com.maxidev.tastymeal.domain.usecase.CategoriesUseCase
import com.maxidev.tastymeal.domain.usecase.RandomMealUseCase
import com.maxidev.tastymeal.domain.usecase.SearchMealUseCase
import com.maxidev.tastymeal.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val randomMealUseCase: RandomMealUseCase,
    private val categoriesUseCase: CategoriesUseCase,
    private val searchMealUseCase: SearchMealUseCase
    //private val filterByCountryUseCase: FilterByCountryUseCase
): ViewModel() {

//    private val _homeState: MutableStateFlow<Resource<HomeUiState>> =
//        MutableStateFlow(Resource.Loading())
//    val homeState: StateFlow<Resource<HomeUiState>> = _homeState.asStateFlow()

    private val _randomFlow: MutableStateFlow<Resource<MinimalMeal>> =
        MutableStateFlow(Resource.Loading())
    val randomFlow: StateFlow<Resource<MinimalMeal>> = _randomFlow.asStateFlow()

    private val _searchByLetterState = MutableStateFlow(HomeUiState())
    val searchByLetterState = _searchByLetterState.asStateFlow()

    init {
        randomMeal()

        mealByLetter(letter = abc.random().toString())
    }

    val categoriesFlow: StateFlow<HomeUiState> =
        categoriesUseCase.invoke()
            .map { HomeUiState(categories = it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = HomeUiState()
            )

    //    val filterByCountryFlow: StateFlow<HomeUiState> =
//        filterByCountryUseCase.invoke(country = countries.random())
//            .map { HomeUiState(filterByCountry = it) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5000),
//                initialValue = HomeUiState()
//            )

    private fun mealByLetter(letter: String) {
        _searchByLetterState.update {
            it.copy(
                searchByLetter = searchMealUseCase.invoke(query = letter)
                    .cachedIn(viewModelScope)
            )
        }
    }

    private fun randomMeal() {
        viewModelScope.launch {
            _randomFlow.value = Resource.Loading()

            _randomFlow.update {
                try {
                    Resource.Success(
                        data = randomMealUseCase.invoke().first()
//                        data = HomeUiState().copy(
//                            randomMeal = randomMealUseCase.invoke()
//                        )
                    )
                } catch (e: HttpException) {
                    Resource.Error(e.message())
                } catch (e: IOException) {
                    Resource.Error(message = e.toString())
                }
            }
        }
    }
}

val abc = ('a'..'z').toList()

//val countries = listOf(
//    "American", "British", "Canadian", "Chinese", "Croatian", "Dutch", "Egyptian", "Filipino",
//    "French", "Greek", "Indian", "Irish", "Italian", "Jamaican", "Japanese", "Kenyan",
//    "Malaysian", "Mexican", "Moroccan", "Polish", "Portuguese", "Russian", "Spanish",
//    "Thai", "Tunisian", "Turkish", "Ukrainian", "Uruguayan", "Vietnamese"
//)