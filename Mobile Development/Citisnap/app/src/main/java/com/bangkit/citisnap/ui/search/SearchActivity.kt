package com.bangkit.citisnap.ui.search

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.databinding.ActivitySearchBinding
import com.bangkit.citisnap.ui.register.UsernameRegisterViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        searchViewModel = ViewModelProvider(this)[ SearchViewModel::class.java ]
        binding.recycle.layoutManager = LinearLayoutManager(this@SearchActivity)
        searchViewModel.searchAdapter.observe(this){ binding.recycle.adapter = it }
        searchViewModel.message.observe(this){ Toast.makeText(this@SearchActivity, it.toString(), Toast.LENGTH_SHORT).show() }

        val search = binding.search
        search.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

        addTextChangeListener(binding.search, searchViewModel)
    }

    private fun searchUser(viewModel: SearchViewModel, user:String){
        viewModel.checkUser(user)
    }

    private fun addTextChangeListener(editText: EditText, viewModel: SearchViewModel) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    searchUser(viewModel, s.toString())
                }, 500)

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}