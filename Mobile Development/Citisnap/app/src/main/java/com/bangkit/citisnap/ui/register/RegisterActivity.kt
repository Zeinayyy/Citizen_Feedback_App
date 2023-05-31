package com.bangkit.citisnap.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityRegisterBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val receivedIntent = intent
        val receivedBundle = receivedIntent.extras

        val value = receivedBundle?.getString("username")

        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val username = value.toString()

        binding.register.setOnClickListener {
            validateInput()
            if(validateInput()){
                register(username, email, password)
            }
        }

        addTextChangeListener(binding.email){ binding.emailLayout.error = null }
        addTextChangeListener(binding.password){ binding.passwordLayout.error = null }

    }

    private fun register(username: String, email: String, password: String) {
        val auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task->
                if (task.isSuccessful){
                    val currentUser = auth.currentUser
                    val db = Firebase.firestore

                    currentUser?.getIdToken(false)?.addOnCompleteListener {
                        if(it.isSuccessful){
                            val token = it.result.token

                            val user = hashMapOf(
                                "username" to username,
                                "token" to token,
                                "name" to username,
                                "email" to email
                            )

                            db.collection("users").document(username)
                                .set(user, SetOptions.merge())
                        }else{
                            task.exception
                        }
                    }

                    updateUI(currentUser)
                }else{
                    val error = task.exception.toString()
                    if (error.contains("email address")){
                        binding.emailLayout.error = error
                    }

                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
            finish()
        }
    }

    private fun validateInput(): Boolean {
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

        if (password.length < 8) {
            binding.passwordLayout.error = getString(R.string.password_long)
            isValid = false
        }

        return isValid
    }

    private fun addTextChangeListener(editText: EditText, callback: () -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                callback.invoke()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }
}