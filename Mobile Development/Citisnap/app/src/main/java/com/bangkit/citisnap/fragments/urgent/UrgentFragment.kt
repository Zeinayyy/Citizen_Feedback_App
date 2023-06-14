package com.bangkit.citisnap.fragments.urgent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.adapter.LoadingStateAdapter
import com.bangkit.citisnap.adapter.PostPagingAdapter
import com.bangkit.citisnap.databinding.FragmentUrgentBinding
import com.bangkit.citisnap.repository.Repository

class UrgentFragment : Fragment() {

    private lateinit var binding : FragmentUrgentBinding
    private val adapter = PostPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUrgentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycle.layoutManager = LinearLayoutManager(context)

        val repository = Repository()
        val viewModelFactory = UrgentViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, viewModelFactory)[UrgentViewModel::class.java]

        getData(viewModel)

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
            getData(viewModel)
        }
    }

    private fun getData(viewModel: UrgentViewModel) {

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

}