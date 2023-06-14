package com.bangkit.citisnap.fragments.recent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.citisnap.fragments.urgent.UrgentViewModel
import com.bangkit.citisnap.model.Posts
import com.bangkit.citisnap.repository.Repository

class RecentViewModel(repository: Repository): ViewModel() {

    val posts: LiveData<PagingData<Posts>> =
        repository.getRecentPosts().cachedIn(viewModelScope)
}

@Suppress("UNCHECKED_CAST")
class RecentViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentViewModel::class.java)) {
            return RecentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}