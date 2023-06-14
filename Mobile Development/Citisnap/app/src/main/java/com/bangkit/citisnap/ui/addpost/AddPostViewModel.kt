package com.bangkit.citisnap.ui.addpost

import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.*
import com.bangkit.citisnap.api.ApiConfig
import com.bangkit.citisnap.model.PostModel
import com.bangkit.citisnap.response.PredictResponse
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostViewModel(private val pref: com.bangkit.citisnap.preferences.Preferences): ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _updateUI = MutableLiveData<Boolean>()
    val updateUI: LiveData<Boolean> get() = _updateUI

    private fun saveToken(token: String){
        viewModelScope.launch {
            pref.saveToken(token)
        }
    }

    fun refreshToken(name: String, description: String, getUri: Uri?, dataStore: DataStore<Preferences>){
        _isLoading.value = true
        val currentUser = Firebase.auth.currentUser

        currentUser?.getIdToken(true)?.addOnCompleteListener { task->
            if (task.isSuccessful){
                val token = task.result?.token.toString()
                saveToken(token)

                scanClassification(name, description, getUri, dataStore)
            }
        }
    }

    private fun scanClassification(name: String, description: String, getUri: Uri?, dataStore: DataStore<Preferences>){
        _isLoading.value = true
        val postModel = PostModel(description)
        val client = ApiConfig.getClient(dataStore)
        client.predict(postModel).enqueue(object : Callback<PredictResponse> {
            override fun onResponse(
                call: Call<PredictResponse>,
                response: Response<PredictResponse>
            ) {
                val respond = response.body()
                if (response.isSuccessful){
                    val classification = respond?.data?.classification.toString()
                    addPost(name, description, getUri, classification)
                    _isLoading.value = false
                }else{
                    val error = response.errorBody()?.string()

                    val jsonObject = error?.let { JSONObject(it) }
                    val errorMessage = jsonObject?.getString("detail")

                    _message.value = errorMessage.toString()
                    Log.d("DALAM RESPONSE", errorMessage.toString())
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                scanClassification(name, description, getUri, dataStore)
            }

        })
    }

    private fun addPost(name: String, description: String, getUri: Uri?, classification: String){

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
                        "imageUrl" to downloadUri,
                        "timestamp" to FieldValue.serverTimestamp(),
                        "urgency" to classification
                    )

                    db.collection("posts")
                        .add(posts)
                        .addOnSuccessListener { documentReference->
                            val documentId = documentReference.id
                            posts["documentId"] = documentId
                            documentReference.set(posts)

                            db.collection("users").document(username.toString()).collection("posts").document(documentId)
                                .set(posts)
                        }
                        .addOnFailureListener { exception->
                            _message.value = exception.message.toString()
                        }

                    _updateUI.value = true
                }
            }.addOnFailureListener{ exception->
                _message.value = exception.message.toString()
            }
        }else{
            val db = Firebase.firestore
            val posts = hashMapOf(
                "userId" to name,
                "description" to description,
                "imageUrl" to "null",
                "timestamp" to FieldValue.serverTimestamp(),
                "urgency" to classification
            )
            db.collection("posts")
                .add(posts)
                .addOnSuccessListener { documentReference->
                    val documentId = documentReference.id
                    posts["documentId"] = documentId
                    documentReference.set(posts)
                    db.collection("users").document(name).collection("posts").document(documentId)
                        .set(posts)
                    _updateUI.value = true

                }
                .addOnFailureListener { exception->
                    _message.value = exception.message.toString()
                }

        }
    }
}

class ViewModelFactory(private val pref: com.bangkit.citisnap.preferences.Preferences) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddPostViewModel::class.java) -> {
                AddPostViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}