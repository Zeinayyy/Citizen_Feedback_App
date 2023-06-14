package com.bangkit.citisnap.ui.login

import androidx.lifecycle.*
import com.bangkit.citisnap.preferences.Preferences
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _updateUI = MutableLiveData<Boolean>()
    val updateUI: LiveData<Boolean> get() = _updateUI

    fun login(email: String, password: String) {
        _isLoading.value = true
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _updateUI.value = true
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            }
            .addOnFailureListener {
                _message.value = it.message.toString()
            }
    }
}