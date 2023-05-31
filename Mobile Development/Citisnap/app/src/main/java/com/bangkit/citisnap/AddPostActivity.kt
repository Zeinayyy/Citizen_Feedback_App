package com.bangkit.citisnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}