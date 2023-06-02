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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.ActivityUsernameBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UsernameRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsernameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsernameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val register = binding.check

        register.isEnabled = false

        register.setOnClickListener {
            val intent = Intent(this@UsernameRegisterActivity, RegisterActivity::class.java)
            val bundle = Bundle()
            val username = binding.username.text.toString().lowercase()
            bundle.putString("username", username)
            intent.putExtras(bundle)

            startActivity(intent)
        }

        addTextChangeListener(binding.username) { binding.usernameLayout.error = null }

    }

    private fun check() {
        val button = binding.check
        val loading = binding.progressBar
        button.isEnabled = false
        button.text = ""
        loading.visibility = View.VISIBLE

        if (binding.username.text.toString().trim() != "") {
            checkUser()
        } else {
            binding.usernameLayout.error = getString(R.string.username_error)
            button.text = getString(R.string.next)
            loading.visibility = View.GONE
            button.isEnabled = false
        }
    }

    private fun checkUser() {
        val button = binding.check
        val loading = binding.progressBar

        val user = binding.username.text.toString().trim().lowercase()
        val checkIcon = ContextCompat.getDrawable(this, R.drawable.check)

        val db = Firebase.firestore

        val docRef = db.collection("users").document(user)
        docRef.get()
            .addOnCompleteListener { task ->
                if (task.isComplete) {
                    val document = task.result
                    if (document != null && document.exists()) {
                        button.text = getString(R.string.next)
                        loading.visibility = View.GONE
                        binding.usernameLayout.error = getString(R.string.username_already_exist)
                        button.isEnabled = false
                    } else {
                        button.text = getString(R.string.next)
                        loading.visibility = View.GONE
                        binding.usernameLayout.error = null
                        binding.usernameLayout.endIconDrawable
                        binding.username.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            null,
                            null,
                            checkIcon,
                            null
                        )
                        button.isEnabled = true
                    }
                }
            }
            .addOnFailureListener {
                Log.d("Error", it.message.toString())
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
                Handler(Looper.getMainLooper()).postDelayed({
                    check()
                }, 500)

            }
        })
    }

}