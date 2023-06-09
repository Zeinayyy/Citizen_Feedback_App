package com.bangkit.citisnap.ui.comment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.citisnap.adapter.CommentsAdapter
import com.bangkit.citisnap.model.Comments
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentsViewModel: ViewModel() {


    private val _imagePost = MutableLiveData<String>()
    val imagePost: LiveData<String> get() = _imagePost

    private val _dataPostList = MutableLiveData<List<String>>()
    val dataPostList: LiveData<List<String>> get() = _dataPostList

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _votes = MutableLiveData<Boolean>()
    val votes: LiveData<Boolean> get() = _votes

    private val _votesUp = MutableLiveData<Int>()
    val votesUp: LiveData<Int> get() = _votesUp


    fun getDataPost(postId: String){

        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("posts")

        collectionRef
            .addSnapshotListener() { documents, e ->
                if (e != null) {
                    _message.value = e.message.toString()
                }

                if (documents != null) {
                    for (document in documents){
                        if (document.id == postId){
                            val name = document.getString("userId").toString()
                            val description = document.getString("description").toString()
                            val imageUrl = document.getString("imageUrl").toString()
                            val urgency = document.getString("urgency").toString()
                            val votes = document.getLong("votes")

                            if (votes != null) {
                                _votes.value = votes > 0
                            }

                            val db = Firebase.firestore
                            db.collection("users").document(name)
                                .get()
                                .addOnSuccessListener{ documentsUser->
                                    val profilePict = documentsUser.getString("profileImg").toString()
                                    _dataPostList.value = listOf(name, description, profilePict, urgency, votes.toString())
                                }

                            if (imageUrl != "null"){
                                _imagePost.value = imageUrl
                            }
                        }
                    }
                }
            }
    }

    private val _updateUI = MutableLiveData<Boolean>()
    val updateUI: LiveData<Boolean> get() = _updateUI
    fun addComment(commentText: String, postId: String){
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val comment = hashMapOf(
                "userId" to currentUser.displayName,
                "comment" to commentText,
                "timestamp" to FieldValue.serverTimestamp()
            )

            db.collection("comments").document(postId).collection("comment").document()
                .set(comment, SetOptions.merge())
                .addOnFailureListener {
                    _message.value = it.message.toString()
                }

            getDataComments(postId)

        }else{
            _updateUI.value = true
        }
    }

    private val _isCommentLoading = MutableLiveData<Boolean>()
    val isCommentLoading: LiveData<Boolean> get() = _isCommentLoading

    private val _commentsAdapter = MutableLiveData<CommentsAdapter>()
    val commentsAdapter: LiveData<CommentsAdapter> get() = _commentsAdapter

    fun getDataComments(postId : String){
        _isCommentLoading.value = true
        val db = Firebase.firestore
        val commentList: MutableList<Comments> = mutableListOf()

        val collectionRef = db.collection("comments").document(postId).collection("comment")
        collectionRef.orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    val description = document.getString("comment").toString()
                    val userId = document.getString("userId").toString()

                    val collectionRefUser = db.collection("users")
                    collectionRefUser.document(userId).get()
                        .addOnSuccessListener { user->
                            val name = user.getString("name").toString()
                            val profileImage = user.getString("profileImg").toString()

                            val comments = Comments(
                                description,
                                name,
                                profileImage
                            )
                            commentList.add(comments)

                            val adapter = CommentsAdapter(commentList)
                            _commentsAdapter.value = adapter
                        }
                }
                _isCommentLoading.value = false
            }
            .addOnFailureListener {
                _message.value = it.message.toString()
            }
    }

    fun votesData(vote: Boolean, postId: String) {
        val username = Firebase.auth.currentUser?.displayName.toString()
        val db = Firebase.firestore
        if (vote){
            db.collection("posts").document(postId)
                .collection("votes").document(username)
                .get()
                .addOnSuccessListener { documents ->
                    when (documents.getBoolean("vote_down")) {
                        true -> {
                            val votes = hashMapOf(
                                "vote_up" to true,
                                "vote_down" to false
                            )

                            db.collection("posts").document(postId)
                                .collection("votes").document(username)
                                .set(votes)
                        }
                        false -> {

                            db.collection("posts").document(postId)
                                .collection("votes").document(username)
                                .delete()
                        }
                        else -> {
                            val votes = hashMapOf(
                                "vote_up" to true,
                                "vote_down" to false
                            )

                            db.collection("posts").document(postId)
                                .collection("votes").document(username)
                                .set(votes)
                        }
                    }
                }
        }else{
            db.collection("posts").document(postId)
                .collection("votes").document(username)
                .get()
                .addOnSuccessListener { documents ->
                    when (documents.getBoolean("vote_up")) {
                        true -> {
                            val votes = hashMapOf(
                                "vote_up" to false,
                                "vote_down" to true
                            )

                            db.collection("posts").document(postId)
                                .collection("votes").document(username)
                                .set(votes)
                        }
                        false -> {

                            db.collection("posts").document(postId)
                                .collection("votes").document(username)
                                .delete()
                        }
                        else -> {
                            val votes = hashMapOf(
                                "vote_up" to false,
                                "vote_down" to true
                            )

                            db.collection("posts").document(postId)
                                .collection("votes").document(username)
                                .set(votes)
                        }
                    }
                }
        }
    }

    fun votesDataUser(postId: String){
        val username = Firebase.auth.currentUser?.displayName.toString()
        val db = Firebase.firestore

        db.collection("posts").document(postId).collection("votes").document(username)
            .addSnapshotListener { documentSnapshot, e ->
                if (e != null) {
                    _message.value = e.message.toString()
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    val voteUp = documentSnapshot.getBoolean("vote_up")
                    val voteDown = documentSnapshot.getBoolean("vote_down")


                    if (voteUp == true) {
                        _votesUp.value = 1

                    } else if (voteDown == true) {
                        _votesUp.value = 2

                    }
                } else {
                    _votesUp.value = 3

                }

            }
    }

}