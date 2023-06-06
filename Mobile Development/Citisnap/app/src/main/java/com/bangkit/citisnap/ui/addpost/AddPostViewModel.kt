package com.bangkit.citisnap.ui.addpost

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddPostViewModel: ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _updateUI = MutableLiveData<Boolean>()
    val updateUI: LiveData<Boolean> get() = _updateUI


    fun addPost(name: String, description: String, getUri: Uri?){
        _isLoading.value = true

        if(getUri != null){
            val storage = FirebaseStorage.getInstance()
            val auth = Firebase.auth
            val username = auth.currentUser?.displayName
            val storageRef = storage.reference
            val imagesRef: StorageReference = storageRef.child("$username").child("post")
            val fileName = "${System.currentTimeMillis()}.jpg"
            val imageRef = imagesRef.child(fileName)
            val uploadTask = imageRef.putFile(getUri)

            uploadTask.addOnSuccessListener { taskSnapshot->
                taskSnapshot.storage.downloadUrl.addOnCompleteListener {
                    val downloadUri = it.result.toString()
                    val db = Firebase.firestore
                    val posts = hashMapOf(
                        "userId" to name,
                        "description" to description,
                        "imageUrl" to downloadUri
                    )

                    db.collection("posts")
                        .add(posts)
                        .addOnSuccessListener { documentReference->
                            val documentId = documentReference.id
                            posts["documentId"] = documentId
                            documentReference.set(posts)

                            db.collection("comments").document(documentId).collection("comments")
                        }
                        .addOnFailureListener { exception->
                            _message.value = exception.message.toString()
                        }

                    _updateUI.value = true
                }
            }.addOnFailureListener{ exception->
                _message.value = exception.message.toString()
                _isLoading.value = false
            }
        }else{
            val db = Firebase.firestore
            val posts = hashMapOf(
                "userId" to name,
                "description" to description,
                "imageUrl" to "null"
            )
            db.collection("posts")
                .add(posts)
                .addOnSuccessListener { documentReference->
                    val documentId = documentReference.id
                    posts["documentId"] = documentId
                    documentReference.set(posts)
                    _updateUI.value = true
                }
                .addOnFailureListener { exception->
                    _message.value = exception.message.toString()
                    _isLoading.value = false
                }

        }
    }
}