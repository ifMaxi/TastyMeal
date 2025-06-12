package com.maxidev.tastymeal.presentation.filters

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.domain.model.FilterByCategory
import com.maxidev.tastymeal.domain.usecase.GetFiltersByCategoryUseCase
import com.maxidev.tastymeal.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    filterUseCase: GetFiltersByCategoryUseCase
): ViewModel() {

    private val filterId = checkNotNull(savedStateHandle.get<String>("filterId"))

    private val _categoryState: MutableStateFlow<Resource<List<FilterByCategory>>> =
        MutableStateFlow(Resource.Loading())
    val categoryState: StateFlow<Resource<List<FilterByCategory>>> = _categoryState.asStateFlow()

    init {
        Log.d("FilterViewModel", "FilterId: $filterId")

        viewModelScope.launch {
            _categoryState.value = Resource.Loading()

            _categoryState.update {
                try {
                    Resource.Success(data = filterUseCase.invoke(filterId))
                } catch (e: IOException) {
                    Resource.Error(e.message.toString())
                } catch (e: HttpException) {
                    Resource.Error(e.message())
                }
            }
        }
    }
}