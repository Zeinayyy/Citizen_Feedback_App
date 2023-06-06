package com.bangkit.citisnap.ui.addpost

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityAddPostBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    private lateinit var auth: FirebaseAuth
    private var getUri: Uri? = null
    private lateinit var addPostViewModel: AddPostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        Glide.with(this).load(currentUser?.photoUrl).into(binding.photoProfile)

        addPostViewModel = ViewModelProvider(this)[AddPostViewModel::class.java]

        binding.post.isEnabled = false
        addTextChangeListener(binding.description, binding.post)

        binding.back.setOnClickListener{ onBackPressed() }
        binding.gallery.setOnClickListener{ startGallery() }
        binding.post.setOnClickListener {
            val desc = binding.description.text.toString()
            val name = currentUser?.displayName.toString()
            addPostViewModel.addPost(name, desc, getUri)
        }

        addPostViewModel.isLoading.observe(this){ isLoading(it) }
        addPostViewModel.updateUI.observe(this){ updateUI(it) }
        addPostViewModel.message.observe(this){ Toast.makeText(this@AddPostActivity, it.toString(), Toast.LENGTH_SHORT).show() }
    }

    private fun updateUI(state: Boolean){
        if (state){
            startActivity(Intent(this@AddPostActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun isLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
            binding.post.isEnabled = false
            binding.post.text = ""
        }else{
            binding.progressBar.visibility = View.GONE
            binding.post.isEnabled = true
            binding.post.text = getString(R.string.post)
        }

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

    private fun addTextChangeListener(editText: EditText, button: Button) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                button.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}