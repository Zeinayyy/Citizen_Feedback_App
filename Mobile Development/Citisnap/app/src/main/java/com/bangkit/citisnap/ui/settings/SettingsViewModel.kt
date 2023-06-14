package com.bangkit.citisnap.ui.settings

import androidx.lifecycle.*
import com.bangkit.citisnap.preferences.Preferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: Preferences): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun logout(){
        viewModelScope.launch {
            pref.logout()
        }
    }
}

class ViewModelFactory(private val pref: Preferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            return SettingsViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}