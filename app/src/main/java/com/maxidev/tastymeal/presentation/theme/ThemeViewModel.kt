package com.maxidev.tastymeal.presentation.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.data.datastore.SettingsDataStore
import com.maxidev.tastymeal.data.datastore.TypeTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    dataStore: SettingsDataStore
): ViewModel() {

    // Observe the Datastore preferences
    val isDynamic: StateFlow<Boolean> =
        dataStore.isDynamicThemeFlow.map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = true
            )

    val isDarkTheme: StateFlow<TypeTheme> =
        dataStore.themeTypeFlow.map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = TypeTheme.SYSTEM
            )
}