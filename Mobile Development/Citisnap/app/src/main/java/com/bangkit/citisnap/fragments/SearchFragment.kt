package com.bangkit.citisnap.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.adapter.PostAdapter
import com.bangkit.citisnap.databinding.FragmentSearchBinding
import com.bangkit.citisnap.model_dummy.AnimalsData

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val animalAdapter = PostAdapter(AnimalsData.animal)

        binding.recycle.adapter = animalAdapter
        binding.recycle.layoutManager = LinearLayoutManager(context)

    }
}