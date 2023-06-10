package com.bangkit.citisnap.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.databinding.FragmentHomeBinding
import com.bangkit.citisnap.ui.main.MainActivity

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private var isDataLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycle.layoutManager = LinearLayoutManager(context)

        if (!isDataLoaded) {
            homeViewModel.getListPost()
            isDataLoaded = true
        }

        binding.swipeRefresh.setOnRefreshListener {
            homeViewModel.getListPost()
            isRefreshing(false)
        }

        homeViewModel.isLoading.observe(requireActivity()){ isRefreshing(it) }
        homeViewModel.postAdapter.observe(requireActivity()){ binding.recycle.adapter = it}
        homeViewModel.message.observe(requireActivity()){ Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show() }
    }

    fun isRefreshing(state: Boolean){
        binding.swipeRefresh.isRefreshing = state
    }
}