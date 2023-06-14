package com.bangkit.citisnap.ui.editProfile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.TransitionBuilder.validate
import androidx.lifecycle.ViewModelProvider
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityEditProfileBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var editProfileViewModel: EditProfileViewModel
    private var getUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = this.getString(R.string.edit_profile)

        editProfileViewModel = ViewModelProvider(this)[EditProfileViewModel::class.java]

        editProfileViewModel.getData()
        editProfileViewModel.listDataUser.observe(this){ getData(it) }
        editProfileViewModel.message.observe(this){ Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()}
        editProfileViewModel.isLoading.observe(this){ isLoading(it) }

        binding.saveChanges.setOnClickListener {
            val name = binding.name.text.toString()
            val bio = binding.bio.text.toString()
            editProfileViewModel.setData(name, bio, getUri)
        }

        binding.photoProfile.setOnClickListener { startGallery() }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading){
            binding.progressBar.visibility = View.VISIBLE
            binding.saveChanges.isEnabled = false
            binding.saveChanges.text = ""
            onBackPressedDispatcher.onBackPressed()
        }else{
            binding.progressBar.visibility = View.GONE
            binding.saveChanges.isEnabled = true
            binding.saveChanges.text = this.getString(R.string.save_changes)
            binding.progressBar.visibility = View.GONE
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

            Glide.with(this).load(getUri).into(binding.photoProfile)
        }
    }

    private fun getData(listData: List<String>) {
        val name = listData[0]
        val bio = listData[1]
        val photoImg = listData[2]

        binding.name.setText(name)
        binding.bio.setText(bio)

        Glide.with(this).load(photoImg).into(binding.photoProfile)
    }
}