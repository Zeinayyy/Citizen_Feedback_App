package com.bangkit.citisnap.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.citisnap.model.Posts
import com.bangkit.citisnap.paging.ProfilePagingSource
import com.bangkit.citisnap.paging.RecentPagingSource
import com.bangkit.citisnap.paging.SearchPagingSource
import com.bangkit.citisnap.paging.UrgentPagingSource

class Repository {

    fun getProfilePosts(userId : String) : LiveData<PagingData<Posts>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                ProfilePagingSource(userId)
            }
        ).liveData
    }

    fun getFilterPosts() : LiveData<PagingData<Posts>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                SearchPagingSource()
            }
        ).liveData
    }

    fun getRecentPosts() : LiveData<PagingData<Posts>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                RecentPagingSource()
            }
        ).liveData
    }

    fun getUrgentPosts() : LiveData<PagingData<Posts>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                UrgentPagingSource()
            }
        ).liveData
    }
}