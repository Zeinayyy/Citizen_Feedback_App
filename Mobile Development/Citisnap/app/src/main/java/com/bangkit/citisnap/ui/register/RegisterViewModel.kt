package com.bangkit.citisnap.ui.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _updateUI = MutableLiveData<Boolean>()
    val updateUI: LiveData<Boolean> get() = _updateUI

    fun register(username: String, email: String, password: String, profilePict: String) {
        _isLoading.value = true
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val currentUser = auth.currentUser
                    val db = Firebase.firestore

                    val user = hashMapOf(
                        "username" to username,
                        "name" to username,
                        "uid" to currentUser?.uid,
                        "profileImg" to profilePict,
                        "bio" to ""
                    )

                    db.collection("users").document(username)
                        .set(user, SetOptions.merge())

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(profilePict))
                        .setDisplayName(username)
                        .build()

                    currentUser?.updateProfile(profileUpdates)

                    _isLoading.value = false
                    _updateUI.value = true
                }else{
                    val error = task.exception?.message.toString()
                    _isLoading.value = false
                    if (error.contains("email address")){
                        _message.value = error
                    }

                }
            }
    }
}