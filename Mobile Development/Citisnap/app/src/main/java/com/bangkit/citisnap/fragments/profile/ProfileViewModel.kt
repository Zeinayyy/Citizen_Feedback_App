package com.bangkit.citisnap.fragments.profile

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bangkit.citisnap.model.Posts
import com.bangkit.citisnap.repository.Repository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel(repository: Repository, userId : String): ViewModel() {

    val posts: LiveData<PagingData<Posts>> =
        repository.getProfilePosts(userId).cachedIn(viewModelScope)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _listDataUser = MutableLiveData<List<String>>()
    val listDataUser: LiveData<List<String>> get() = _listDataUser

    fun getDataUser(username : String){
        _isLoading.value = true

        val db = Firebase.firestore
        db.collection("users").document(username)
            .addSnapshotListener{ documentSnapshot, e->

                if (e != null){
                    _message.value = e.message.toString()
                }

                if (documentSnapshot != null){
                    val name = documentSnapshot.getString("name").toString()
                    val profileImg = documentSnapshot.getString("profileImg").toString()
                    val userId = documentSnapshot.getString("username").toString()
                    val bio = documentSnapshot.get("bio").toString()

                    _listDataUser.value = listOf(name, profileImg, userId, bio)

                    Log.d("nAMEEEE", name)
                    _isLoading.value = false


                }
            }
    }
}

@Suppress("UNCHECKED_CAST")
class ProfileViewModelFactory(private val repository: Repository, private val userId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}