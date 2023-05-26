package com.bangkit.citisnap

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.citisnap.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.register.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if(validateInput()){
                register(email, password)
            }
        }
    }

    private fun register(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    updateUI(user)
                }else{
                    binding.passwordLayout.error = task.exception.toString()
                }
            }

    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun validateInput():    Boolean {
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        var isValid = true

        if (email.isEmpty()) {
            binding.emailLayout.error = getString(R.string.email_error)
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordLayout.error = getString(R.string.empty_pass_error)
            isValid = false
        }

        return isValid
    }
}