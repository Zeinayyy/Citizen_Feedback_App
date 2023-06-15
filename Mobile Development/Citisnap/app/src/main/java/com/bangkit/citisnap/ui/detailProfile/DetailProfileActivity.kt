package com.bangkit.citisnap.ui.detailProfile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.adapter.LoadingStateAdapter
import com.bangkit.citisnap.adapter.PostPagingAdapter
import com.bangkit.citisnap.databinding.ActivityDetailProfileBinding
import com.bangkit.citisnap.fragments.profile.ProfileViewModel
import com.bangkit.citisnap.fragments.profile.ProfileViewModelFactory
import com.bangkit.citisnap.repository.Repository
import com.bumptech.glide.Glide

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding
    private val adapter = PostPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycle.layoutManager = LinearLayoutManager(this)

        val receivedIntent = intent.extras
        val username = receivedIntent?.getString("username").toString()

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = username

        val repository = Repository()
        val viewModelFactory = ProfileViewModelFactory(repository, username)
        val viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        getData(viewModel)
        viewModel.getDataUser(username)
        viewModel.message.observe(this){ Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show() }
        viewModel.isLoading.observe(this){ showLoading(it) }
        viewModel.listDataUser.observe(this){ getDataUser(it) }

        adapter.addLoadStateListener { loadState->
            if (loadState.append.endOfPaginationReached){
                if (adapter.itemCount < 1 ){
                    binding.noPosts.visibility = View.VISIBLE
                }
            }
        }


    }

    private fun getDataUser(data : List<String>){
        val name = data[0]
        val profileImg = data[1]
        val bio = data[3]

        binding.name.text = name
        binding.bio.text = bio
        Glide.with(this).load(profileImg).into(binding.profileImg)
    }

    private fun getData(viewModel: ProfileViewModel) {

        binding.recycle.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        viewModel.posts.observe(this){
            adapter.submitData(lifecycle, it)
        }


    }

    private fun showLoading(state: Boolean){ binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE}

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}