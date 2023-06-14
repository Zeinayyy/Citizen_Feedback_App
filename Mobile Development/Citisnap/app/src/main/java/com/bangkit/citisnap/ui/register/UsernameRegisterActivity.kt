package com.bangkit.citisnap.ui.register

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityUsernameBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsernameRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsernameBinding
    private lateinit var usernameRegisterViewModel: UsernameRegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usernameRegisterViewModel = ViewModelProvider(this)[UsernameRegisterViewModel::class.java]
        usernameRegisterViewModel.isLoading.observe(this){ showLoading(it) }
        usernameRegisterViewModel.message.observe(this){ Toast.makeText(this, it, Toast.LENGTH_SHORT).show() }

        val register = binding.check

        register.isEnabled = false


        register.setOnClickListener {
            val username = binding.username.text.toString().lowercase()
            val intent = Intent(this@UsernameRegisterActivity, RegisterActivity::class.java)
            val bundle = Bundle()
            bundle.putString("username", username)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        addTextChangeListener(binding.username, usernameRegisterViewModel) { binding.usernameLayout.error = null }

    }

    private fun showLoading(state: Boolean) {
        if (state){
            binding.check.text = getString(R.string.next)
            binding.progressBar.visibility = View.GONE
            binding.usernameLayout.error = getString(R.string.username_already_exist)
            binding.check.isEnabled = false
        }else{
            val checkIcon = ContextCompat.getDrawable(this, R.drawable.check)
            binding.check.text = getString(R.string.next)
            binding.progressBar.visibility = View.GONE
            binding.usernameLayout.error = null
            binding.usernameLayout.endIconDrawable
            binding.username.setCompoundDrawablesRelativeWithIntrinsicBounds(
                null,
                null,
                checkIcon,
                null
            )
            binding.check.isEnabled = true
        }
    }

    private fun check(viewModel: UsernameRegisterViewModel, user: String) {
        val button = binding.check
        val loading = binding.progressBar
        button.isEnabled = false
        button.text = ""
        loading.visibility = View.VISIBLE

        if (binding.username.text.toString().trim() != "") {
            Log.d("USER", user)
            viewModel.checkUser(user)
        } else {
            binding.usernameLayout.error = getString(R.string.username_error)
            button.text = getString(R.string.next)
            loading.visibility = View.GONE
            button.isEnabled = false
        }
    }

    private fun addTextChangeListener(editText: EditText, viewModel: UsernameRegisterViewModel, callback: () -> Unit) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                callback.invoke()
            }

            override fun afterTextChanged(s: Editable?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    check(viewModel, s.toString())
                }, 500)

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@UsernameRegisterActivity, MainActivity::class.java))
    }

}