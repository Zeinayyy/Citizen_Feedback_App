package com.bangkit.citisnap.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityLoginBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.bangkit.citisnap.ui.register.UsernameRegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(name = "DataUser")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        auth = Firebase.auth

        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        addTextChangeListener(binding.email) { binding.emailLayout.error = null }
        addTextChangeListener(binding.password) { binding.passwordLayout.error = null }

        binding.login.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            if (validateInput()) {
                loginViewModel.login(email, password)
            }
        }

        loginViewModel.updateUI.observe(this){ updateUI(it) }
        loginViewModel.isLoading.observe(this){ showLoading(it) }
        loginViewModel.message.observe(this){ binding.passwordLayout.error = getString(R.string.error_login) }

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


    private fun updateUI(state: Boolean) {
        if (state) {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.login.isEnabled = !state
        binding.login.text = if (state) "" else getString(R.string.login)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
    }
}