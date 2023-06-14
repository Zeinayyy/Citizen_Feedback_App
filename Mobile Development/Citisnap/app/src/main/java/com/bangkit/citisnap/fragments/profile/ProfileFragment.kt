package com.bangkit.citisnap.fragments.profile

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.adapter.LoadingStateAdapter
import com.bangkit.citisnap.adapter.PostPagingAdapter
import com.bangkit.citisnap.databinding.FragmentProfileBinding
import com.bangkit.citisnap.preferences.Preferences
import com.bangkit.citisnap.preferences.settingsDataStore
import com.bangkit.citisnap.repository.Repository
import com.bangkit.citisnap.ui.addpost.AddPostActivity
import com.bangkit.citisnap.ui.editProfile.EditProfileActivity
import com.bangkit.citisnap.ui.settings.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val adapter = PostPagingAdapter()
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)



        binding.editProfile.setOnClickListener { startActivity(Intent(requireContext(), EditProfileActivity::class.java)) }

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = Firebase.auth.currentUser
        val username = currentUser?.displayName.toString()

        val repository = Repository()
        val viewModelFactory = ProfileViewModelFactory(repository, username)
        val viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]

        viewModel.getDataUser(username)
        viewModel.message.observe(this){ Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show() }
        viewModel.isLoading.observe(this){ showLoading(it) }
        viewModel.listDataUser.observe(this){ dataUser(it) }

        getData(viewModel)

        binding.recycle.layoutManager = LinearLayoutManager(context)

        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.settings_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId){
                R.id.settingsActivity -> {
                    startActivity(Intent(requireContext(), SettingsActivity::class.java))
                }
            }
            false
        }

        val menuItem = toolbar.menu.findItem(R.id.settingsActivity)

        val pref = Preferences.getInstance(requireActivity().settingsDataStore)
        val settingsViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingsViewModel::class.java]
        settingsViewModel.getThemeSettings().observe(this) { isDarkMode : Boolean ->
            if (isDarkMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                menuItem.iconTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                menuItem.iconTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.black))
            }
        }

        adapter.addLoadStateListener { loadState->
            if (loadState.append.endOfPaginationReached){
                if (adapter.itemCount < 1 ){
                    binding.noPosts.visibility = View.VISIBLE
                }
            }
        }

        binding.link.setOnClickListener { startActivity(Intent(requireContext(), AddPostActivity::class.java)) }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
            getData(viewModel)
        }
    }


    private fun dataUser(listData: List<String>) {
        val name = listData[0]
        val profileImg = listData[1]
        val username = listData[2]
        val bio = listData[3]

        binding.bio.text = bio
        binding.username.text = username
        Glide.with(mContext).load(profileImg).into(binding.profileImg)
        binding.name.text = name
    }

    private fun getData(viewModel: ProfileViewModel) {

        adapter.addLoadStateListener { loadState->
            binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
            val errorState = loadState.refresh as? LoadState.Error
            errorState?.let { error ->
                Toast.makeText(requireContext(), error.error.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.recycle.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter{
                adapter.retry()
            }
        )

        viewModel.posts.observe(requireActivity()){
            adapter.submitData(lifecycle, it)
        }
    }

    private fun showLoading(state: Boolean){ binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE}
}