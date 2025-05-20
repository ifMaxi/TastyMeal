package com.maxidev.tastymeal.presentation.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.domain.usecase.AllBookmarksUseCase
import com.maxidev.tastymeal.domain.usecase.ClearAllBookmarksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    allBookmarksUseCase: AllBookmarksUseCase,
    private val clearAllUseCase: ClearAllBookmarksUseCase
): ViewModel() {

    val getBookmarks: StateFlow<BookmarkUiState> =
        allBookmarksUseCase.invoke()
            .map { BookmarkUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = BookmarkUiState()
            )

    fun clearAllBookmarks() = viewModelScope.launch {
        clearAllUseCase.invoke()
    }
}