package com.bangkit.citisnap.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.citisnap.databinding.LayoutItemPostBinding
import com.bangkit.citisnap.model_dummy.Posts
import com.bumptech.glide.Glide

class PostAdapter(private val postsList: List<Posts>) :
    RecyclerView.Adapter<PostAdapter.HomeViewHolder>() {

    class HomeViewHolder(private val binding: LayoutItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(posts: Posts) {
            with(binding) {
                name.text = posts.name
                desc.text = posts.text

                if (posts.imageUrl == "null"){
                    imageCard.visibility = View.GONE
                }else{
                    imageCard.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(posts.imageUrl).into(image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            LayoutItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(postsList[position])
    }
}