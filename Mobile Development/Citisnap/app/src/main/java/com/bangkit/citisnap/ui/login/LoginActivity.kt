package com.bangkit.citisnap.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityLoginBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.bangkit.citisnap.ui.register.RegisterActivity
import com.bangkit.citisnap.ui.register.UsernameRegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        auth = Firebase.auth

        addTextChangeListener(binding.email) { binding.emailLayout.error = null }
        addTextChangeListener(binding.password) { binding.passwordLayout.error = null }

        binding.login.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if (validateInput()) {
                login(email, password)
            }
        }

        binding.register.setOnClickListener {
            startActivity(Intent(this, UsernameRegisterActivity::class.java))
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

        return isValid
    }

    private fun login(email: String, password: String) {
        val auth = FirebaseAuth.getInstance()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    binding.passwordLayout.error = getString(R.string.error_login)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
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

    private fun playAnimation() {
        val title = ObjectAnimator.ofFloat(binding.title, View.ALPHA, 1f).setDuration(duration_anim)
        val linearLayout =
            ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(duration_anim)
        val linearLayout2 =
            ObjectAnimator.ofFloat(binding.linearLayout2, View.ALPHA, 1f).setDuration(duration_anim)

        AnimatorSet().apply {
            playSequentially(
                title,
                linearLayout,
                linearLayout2
            )
            startDelay = duration_anim
        }.start()
    }

    companion object {
        private const val duration_anim: Long = 500
    }
}