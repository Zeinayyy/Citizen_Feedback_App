package com.bangkit.citisnap.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsernameRegisterViewModel: ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun checkUser(user : String) {

        val db = Firebase.firestore

        val docRef = db.collection("users").document(user)
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isComplete) {
                    if(task.isSuccessful){
                        val document = task.result
                        _isLoading.value = document != null && document.exists()
                    }else{
                        _message.value = "Check Internet Connection"
                    }

                }else{
                    _message.value = "Check Internet Connection"
                }
            }
    }
}