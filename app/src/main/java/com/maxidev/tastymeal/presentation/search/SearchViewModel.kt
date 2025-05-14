package com.maxidev.tastymeal.presentation.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.maxidev.tastymeal.domain.usecase.SearchMealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCase: SearchMealUseCase
): ViewModel() {

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState = _searchState.asStateFlow()

    private val _input = mutableStateOf("")
    val input = _input

    fun onInputChange(newInput: String) { _input.value = newInput }

    fun searchMeals(query: String) {
        _searchState.update {
            it.copy(
                search = useCase.invoke(query = query)
                    .cachedIn(viewModelScope)
            )
        }
    }
}