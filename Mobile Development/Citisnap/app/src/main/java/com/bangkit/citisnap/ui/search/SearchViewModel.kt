package com.bangkit.citisnap.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.citisnap.adapter.CommentsAdapter
import com.bangkit.citisnap.adapter.SearchAdapter
import com.bangkit.citisnap.model.Comments
import com.bangkit.citisnap.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _searchAdapter = MutableLiveData<SearchAdapter>()
    val searchAdapter: LiveData<SearchAdapter> get() = _searchAdapter

    fun checkUser(user : String) {

        val db = Firebase.firestore
        val userList: MutableList<User> = mutableListOf()

        val docRef = db.collection("users").orderBy("username")
            .startAt(user)
            .endAt(user + "\uf8ff")

        docRef.get()
            .addOnCompleteListener { querySnapshot  ->
                for (document in querySnapshot.result){
                    val username = document.getString("username").toString()
                    val photoUrl = document.getString("profileImg").toString()

                    val users = User(
                        username,
                        photoUrl
                    )

                    if (user != ""){
                        userList.add(users)
                        val adapter = SearchAdapter(userList)
                        _searchAdapter.value = adapter
                    }
                }
            }
            .addOnFailureListener {
                _message.value = it.message.toString()
            }
    }
}