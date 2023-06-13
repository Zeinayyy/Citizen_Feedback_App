package com.bangkit.citisnap.fragments.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.citisnap.adapter.PostAdapter
import com.bangkit.citisnap.model.Posts
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel : ViewModel() {

    private val _postAdapter = MutableLiveData<PostAdapter>()
    val postAdapter: LiveData<PostAdapter> get() = _postAdapter

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading




    fun getListPost() {
        _isLoading.value = true
        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("posts")

        val postsList: MutableList<Posts> = mutableListOf()

        collectionRef.orderBy("vote_up", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents ->
                val tasks = mutableListOf<Task<DocumentSnapshot>>()

                for (document in documents) {
                    val username = document.getString("userId").toString()

                    val collectionRefUser = firestore.collection("users")
                    val task = collectionRefUser.document(username).get()
                    tasks.add(task)
                }

                Tasks.whenAllSuccess<DocumentSnapshot>(tasks)
                    .addOnSuccessListener { userSnapshots ->
                        for (i in userSnapshots.indices) {
                            val user = userSnapshots[i]
                            val name = user.getString("name").toString()
                            val profileImage = user.getString("profileImg").toString()
                            val text = documents.documents[i].getString("description").toString()
                            val imageUrl = documents.documents[i].getString("imageUrl").toString()
                            val postId = documents.documents[i].id

                            val posts = Posts(name, text.replace("\\n", "\n"), imageUrl, postId, profileImage)
                            postsList.add(posts)
                        }

                        val adapter = PostAdapter(postsList)
                        _postAdapter.value = adapter
                        _isLoading.value = false
                    }
                    .addOnFailureListener { e ->
                        _message.value = e.message.toString()
                        _isLoading.value = false
                    }
            }
            .addOnFailureListener { e ->
                _message.value = e.message.toString()
                _isLoading.value = false
            }
    }
}