package com.maxidev.tastymeal.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxidev.tastymeal.data.datastore.SettingsDataStore
import com.maxidev.tastymeal.data.datastore.TypeTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: SettingsDataStore
): ViewModel() {

    val dialogVisible = MutableStateFlow(false)

    val isDynamic: StateFlow<Boolean> =
        dataStore.isDynamicThemeFlow.map { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = true
            )

    val isTypeTheme: StateFlow<SettingsState> =
        dataStore.themeTypeFlow.map { SettingsState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = SettingsState(TypeTheme.SYSTEM)
            )

    fun setDialogVisible(value: Boolean) {
        dialogVisible.value = value
    }

    fun updateIsDynamicTheme() {
        dataStore.setDynamicTheme(value = !isDynamic.value, scope = viewModelScope)
    }

    fun updateIsDarkTheme(value: TypeTheme) {
        dataStore.setTypeTheme(value = value, scope = viewModelScope)
    }
}