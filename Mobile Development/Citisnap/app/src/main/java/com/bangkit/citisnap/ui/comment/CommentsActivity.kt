package com.bangkit.citisnap.ui.comment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.adapter.CommentsAdapter
import com.bangkit.citisnap.databinding.ActivityCommentBinding
import com.bangkit.citisnap.model.Comments
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.thread)

        val receivedIntent = intent.extras

        val postId = receivedIntent?.getString("id Post").toString()
        binding.recycle.layoutManager = LinearLayoutManager(this@CommentsActivity)


        getDataPost(postId)
        getDataComment(postId)

        val auth = Firebase.auth
        val currentUser = auth.currentUser
        Glide.with(this@CommentsActivity).load(currentUser?.photoUrl).into(binding.profileImageComment)

        binding.send.setOnClickListener {
            val commentText = binding.comment.text.toString()
            addDataComment(commentText, postId)
        }

        addTextChangeListener(binding.comment, binding.send)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun getDataPost(postId : String){
        val firestore = FirebaseFirestore.getInstance()
        val auth = Firebase.auth
        val currentUser = auth.currentUser
        val collectionRef = firestore.collection("posts")

        collectionRef.get()
            .addOnSuccessListener { documents->
                for (document in documents){
                    if (document.id == postId){
                        val name = document.getString("userId")
                        val description = document.getString("description")
                        val imageUrl = document.getString("imageUrl")

                        binding.name.text = name
                        binding.description.text = description
                        Glide.with(this@CommentsActivity).load(currentUser?.photoUrl).into(binding.profileImage)

                        if( imageUrl != "null" ){
                            binding.image.visibility = View.VISIBLE
                            Glide.with(this@CommentsActivity).load(imageUrl.toString()).into(binding.image)
                        }else{
                            binding.image.visibility = View.GONE
                        }

                    }
                }
            }
    }

    private fun addDataComment(commentText: String, postId: String){
        val db = Firebase.firestore
        val auth = Firebase.auth
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val comment = hashMapOf(
                "userId" to currentUser.displayName,
                "comment" to commentText,
            )

            db.collection("Comments").document(postId).collection("Comment").document()
                .set(comment, SetOptions.merge())

        }
    }

    private fun getDataComment(postId : String){
        val db = Firebase.firestore
        val commentList: MutableList<Comments> = mutableListOf()
        val collectionRef = db.collection("Comments").document(postId).collection("Comment")

        collectionRef.get()
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

                            Log.d("TEST POST COMMENTS", commentList.toString())

                            val adapter = CommentsAdapter(commentList)
                            binding.recycle.adapter = adapter
                        }
                }
            }
    }

    private fun addTextChangeListener(editText: EditText, textView: TextView) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()){
                    textView.visibility = View.GONE
                }else{
                    textView.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}