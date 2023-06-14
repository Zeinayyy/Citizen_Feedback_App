package com.bangkit.citisnap.fragments.urgent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.citisnap.model.Posts
import com.bangkit.citisnap.repository.Repository

class UrgentViewModel(repository: Repository): ViewModel() {

    val posts: LiveData<PagingData<Posts>> =
        repository.getUrgentPosts().cachedIn(viewModelScope)

}

@Suppress("UNCHECKED_CAST")
class UrgentViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UrgentViewModel::class.java)) {
            return UrgentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}