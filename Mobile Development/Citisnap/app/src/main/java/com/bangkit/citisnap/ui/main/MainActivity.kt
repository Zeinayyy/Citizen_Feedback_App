package com.bangkit.citisnap.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityMainBinding
import com.bangkit.citisnap.ui.login.LoginActivity
import com.bangkit.citisnap.ui.register.RegisterActivity
import com.bangkit.citisnap.ui.register.UsernameRegisterActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        if(firebaseUser != null){
            binding.relativeLayout.visibility = View.GONE
        }else{
            binding.relativeLayout.visibility = View.VISIBLE
        }

        binding.login.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this@MainActivity, UsernameRegisterActivity::class.java))
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

    }
}