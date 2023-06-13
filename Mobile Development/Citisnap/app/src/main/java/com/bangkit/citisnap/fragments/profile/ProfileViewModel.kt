package com.bangkit.citisnap.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.citisnap.adapter.PostAdapter
import com.bangkit.citisnap.model.Posts
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewModel: ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _adapter = MutableLiveData<PostAdapter>()
    val adapter: LiveData<PostAdapter> get() = _adapter

    private val _listDataUser = MutableLiveData<List<String>>()
    val listDataUser: LiveData<List<String>> get() = _listDataUser

    fun getDataUser(username : String){
        _isLoading.value = true

        val db = Firebase.firestore
        db.collection("users").document(username).get()
            .addOnSuccessListener { user->
                val name = user.getString("name").toString()
                val profileImg = user.getString("profileImg").toString()
                val userId = user.getString("username").toString()

                _listDataUser.value = listOf(name, profileImg, userId)
                _isLoading.value = false
            }
            .addOnFailureListener { e->
                _message.value = e.message.toString()
            }
    }

    fun getPostUser(username: String){
        val postList: MutableList<Posts> = mutableListOf()
        val db = Firebase.firestore
        db.collection("posts").get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    val user = document.getString("userId")

                    if (user == username){
                        db.collection("users").document(username).get()
                            .addOnSuccessListener { userData->
                                val name = userData.getString("name").toString()
                                val photoUrl = userData.getString("profileImg").toString()
                                val imageUrl = document.getString("imageUrl").toString()
                                val description = document.getString("description").toString()
                                val postsId = document.getString("documentId").toString()

                                val posts = Posts(name, description, imageUrl, postsId, photoUrl)

                                postList.add(posts)

                                val adapter = PostAdapter(postList)

                                _adapter.value = adapter

                            }
                            .addOnFailureListener {
                                _message.value = it.message.toString()
                            }
                    }
                }
            }
            .addOnFailureListener {
                _message.value = it.message.toString()
            }
    }
}