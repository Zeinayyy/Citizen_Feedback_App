package com.bangkit.citisnap.ui.comment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityCommentBinding
import com.bangkit.citisnap.ui.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CommentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommentBinding
    private lateinit var commentsViewModel: CommentsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = Firebase.auth
        val currentUser = auth.currentUser
        Glide.with(this@CommentsActivity).load(currentUser?.photoUrl).into(binding.profileImageComment)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.thread)

        val receivedIntent = intent.extras
        val postId = receivedIntent?.getString("id Post").toString()

        binding.recycle.layoutManager = LinearLayoutManager(this@CommentsActivity)

        commentsViewModel = ViewModelProvider(this)[CommentsViewModel::class.java]
        commentsViewModel.getDataComments(postId)
        commentsViewModel.getDataPost(postId)
        commentsViewModel.dataPostList.observe(this){ dataPost(it) }
        commentsViewModel.isCommentLoading.observe(this){ showLoadingComment(it)}
        commentsViewModel.updateUI.observe(this){ updateUI(it) }
        commentsViewModel.commentsAdapter.observe(this){
            binding.recycle.adapter = it
            if (it.itemCount == 0){
                binding.firstComment.visibility = View.VISIBLE
            }else{
                binding.firstComment.visibility = View.GONE
            }
        }
        commentsViewModel.imagePost.observe(this){ image->
            if (image != null){
                Glide.with(this@CommentsActivity).load(image).into(binding.image)
                binding.image.visibility = View.VISIBLE
            }else{
                binding.image.visibility = View.GONE
            }
        }

        binding.send.setOnClickListener {
            val commentText = binding.comment.text.toString()
            commentsViewModel.addComment(commentText, postId)
            binding.comment.text = null
            binding.comment.onEditorAction(EditorInfo.IME_ACTION_DONE)
            binding.nestedScroll.smoothScrollTo(0,0)
        }

        addTextChangeListener(binding.comment, binding.send)


    }

    private fun dataPost(listData: List<String>){
        val name = listData[0]
        val desc = listData[1]
        val photoProf = listData[2]

        binding.name.text = name
        binding.description.text = desc
        Glide.with(this@CommentsActivity).load(photoProf).into(binding.profileImage)
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

    private fun updateUI(state: Boolean) { if (state){ startActivity(Intent(this, LoginActivity::class.java)) } }

    private fun showLoadingComment(state: Boolean){ binding.progressBarComment.visibility = if (state) View.VISIBLE else View.GONE}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}