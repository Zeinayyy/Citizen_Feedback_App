package com.bangkit.citisnap.ui.detailProfile

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.databinding.ActivityDetailProfileBinding
import com.bangkit.citisnap.fragments.profile.ProfileViewModel
import com.bumptech.glide.Glide

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val receivedIntent = intent.extras
        val username = receivedIntent?.getString("username").toString()

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = username

        profileViewModel = ViewModelProvider(this)[ ProfileViewModel::class.java ]

        val recyclerView = binding.recycle
        recyclerView.layoutManager = LinearLayoutManager(this)

        profileViewModel.getDataUser(username)
        profileViewModel.getPostUser(username)
        profileViewModel.listDataUser.observe(this){ getDataUser(it) }
        profileViewModel.adapter.observe(this){ recyclerView.adapter = it }
        profileViewModel.message.observe(this){ Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show() }

    }

    private fun getDataUser(data : List<String>){
        val name = data[0]
        val profileImg = data[1]

        binding.name.text = name
        Glide.with(this).load(profileImg).into(binding.profileImg)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}