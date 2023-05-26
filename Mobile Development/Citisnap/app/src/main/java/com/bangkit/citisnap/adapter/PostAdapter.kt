package com.bangkit.citisnap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.citisnap.databinding.LayoutItemPostBinding
import com.bangkit.citisnap.model_dummy.Animals
import com.bumptech.glide.Glide

class PostAdapter (private val animalList: List<Animals>) : RecyclerView.Adapter<PostAdapter.HomeViewHolder>() {

    class HomeViewHolder(private val binding: LayoutItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(animals: Animals){
            with(binding){
                name.text = animals.name
                Glide.with(itemView.context).load(animals.photoUrl).into(image)
                desc.text = animals.desc
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = LayoutItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return animalList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(animalList[position])
    }
}