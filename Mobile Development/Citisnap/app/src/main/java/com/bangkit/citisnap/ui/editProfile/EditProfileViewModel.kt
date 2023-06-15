package com.bangkit.citisnap.ui.editProfile

import android.net.Uri
import android.provider.SyncStateContract.Helpers.update
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfileViewModel : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _listDataUser = MutableLiveData<List<String>>()
    val listDataUser: LiveData<List<String>> get() = _listDataUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading:LiveData<Boolean> get() = _isLoading


    fun getData(){
        val db = Firebase.firestore
        val currentUser = Firebase.auth.currentUser?.displayName.toString()

        db.collection("users").document(currentUser)
            .get()
            .addOnSuccessListener { userData ->
                val photoProfile = userData.getString("profileImg").toString()
                val name = userData.getString("name").toString()
                val bio = userData.getString("bio").toString()

                val listData = listOf(name, bio, photoProfile)
                _listDataUser.value = listData
            }.addOnFailureListener {
                _message.value = it.message.toString()
            }
    }

    fun setData(name : String, bio: String, getUri: Uri?){
        _isLoading.value = true
        val db = Firebase.firestore
        val auth = Firebase.auth
        val username = auth.currentUser?.displayName.toString()

        if (getUri != null){
            val storage = FirebaseStorage.getInstance()
            val currentUser = auth.currentUser
            val storageRef = storage.reference
            val imagesRef: StorageReference = storageRef.child(username).child("photoProfile")
            val fileName = "${System.currentTimeMillis()}.jpg"
            val imageRef = imagesRef.child(fileName)
            val uploadTask = imageRef.putFile(getUri)

            uploadTask.addOnSuccessListener { taskSnapshot->
                taskSnapshot.storage.downloadUrl.addOnCompleteListener {
                    val downloadUri = it.result.toString()

                    val data = hashMapOf<String, Any>(
                        "name" to name,
                        "bio" to bio,
                        "profileImg" to downloadUri
                    )

                    db.collection("users").document(username)
                        .update(data)

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(downloadUri))
                        .build()

                    currentUser?.updateProfile(profileUpdates)
                }
                _message.value = "Edit successful."
                _isLoading.value = false
            }.addOnFailureListener{
                _message.value = it.message.toString()
                _isLoading.value = false
            }
        }else{
            _isLoading.value = true
            val data = hashMapOf<String, Any>(
                "name" to name,
                "bio" to bio,
            )

            db.collection("users").document(username)
                .update(data)
            _message.value = "Edit successful."
            _isLoading.value = false
        }

    }


}