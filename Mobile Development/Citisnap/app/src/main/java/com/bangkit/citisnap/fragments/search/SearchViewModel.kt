package com.bangkit.citisnap.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.citisnap.model.Posts
import com.bangkit.citisnap.repository.Repository

class SearchViewModel(repository: Repository) : ViewModel() {

    val posts: LiveData<PagingData<Posts>> =
        repository.getFilterPosts().cachedIn(viewModelScope)

}

@Suppress("UNCHECKED_CAST")
class SearchViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}