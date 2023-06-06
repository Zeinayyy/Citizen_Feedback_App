package com.bangkit.citisnap.ui.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityRegisterBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val receivedIntent = intent
        val receivedBundle = receivedIntent.extras

        val value = receivedBundle?.getString("username")

        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]

        binding.register.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val username = value.toString()
            val profilePict = getString(R.string.profile_pict)
            validateInput()
            if(validateInput()){
                registerViewModel.register(username, email, password, profilePict)
                binding.progressBar.visibility = View.VISIBLE
                binding.register.isEnabled = false
                binding.register.text = null
            }
        }

        registerViewModel.isLoading.observe(this){ showLoading(it) }
        registerViewModel.message.observe(this){ binding.passwordLayout.error = it }
        registerViewModel.updateUI.observe(this){ updateUI(it) }

        addTextChangeListener(binding.email){ binding.emailLayout.error = null }
        addTextChangeListener(binding.password){ binding.passwordLayout.error = null }

    }

    private fun updateUI(state: Boolean) {
        if (state) {
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.register.isEnabled = !state
        binding.register.text = if (state) "" else getString(R.string.register)
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