package com.bangkit.citisnap.ui.addpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.citisnap.databinding.ActivityAddPostBinding
import com.bangkit.citisnap.utils.uriToFile
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.checkerframework.checker.units.qual.s
import org.w3c.dom.Text
import java.io.File

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var auth: FirebaseAuth
    private var getUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        Glide.with(this).load(currentUser?.photoUrl).into(binding.photoProfile)




        binding.back.setOnClickListener{ onBackPressed() }
        binding.gallery.setOnClickListener{ startGallery() }
        binding.post.setOnClickListener {
            val desc = binding.description.text.toString()
            val name = currentUser?.displayName.toString()

            addPost(name, desc)
        }



        Log.d("USER", auth.currentUser?.displayName.toString())
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            getUri = selectedImg

            binding.photo.visibility = View.VISIBLE
            binding.photo.setImageURI(selectedImg)
        }
    }

    private fun addPost(name: String, description: String){

        if(getUri != null){
            val storage = FirebaseStorage.getInstance()
            val auth = Firebase.auth
            val username = auth.currentUser?.displayName
            val storageRef = storage.reference
            val imagesRef: StorageReference = storageRef.child("$username").child("post")
            val fileName = "${System.currentTimeMillis()}.jpg"
            val imageRef = imagesRef.child(fileName)
            val uploadTask = imageRef.putFile(getUri!!)

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
                        }
                }

            }.addOnFailureListener{ exception->
                Toast.makeText(this@AddPostActivity, exception.message.toString(), Toast.LENGTH_SHORT).show()
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
                }
                .addOnFailureListener { exception->
                    Toast.makeText(this@AddPostActivity, exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

        }


    }
}