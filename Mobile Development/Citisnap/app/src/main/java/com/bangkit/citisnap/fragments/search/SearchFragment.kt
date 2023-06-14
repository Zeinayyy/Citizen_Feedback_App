package com.bangkit.citisnap.fragments.search

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.adapter.LoadingStateAdapter
import com.bangkit.citisnap.adapter.PostPagingAdapter
import com.bangkit.citisnap.databinding.FragmentSearchBinding
import com.bangkit.citisnap.repository.Repository
import com.bangkit.citisnap.ui.search.SearchActivity

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val adapter = PostPagingAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycle.layoutManager = LinearLayoutManager(context)

        val repository = Repository()
        val vmFactory = SearchViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, vmFactory)[SearchViewModel::class.java]
        getData(viewModel)

        binding.search.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            adapter.refresh()
            getData(viewModel)
        }
    }
    private fun getData(viewModel: SearchViewModel) {

        adapter.addLoadStateListener { loadState->
            binding.swipeRefresh.isRefreshing = loadState.refresh is LoadState.Loading
            val errorState = loadState.refresh as? LoadState.Error
            errorState?.let { error ->
                Toast.makeText(requireContext(), error.error.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        binding.recycle.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        viewModel.posts.observe(requireActivity()) {
            adapter.submitData(lifecycle, it)
        }
    }


}