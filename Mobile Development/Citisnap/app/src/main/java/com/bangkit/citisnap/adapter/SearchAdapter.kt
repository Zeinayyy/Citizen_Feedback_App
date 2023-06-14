package com.bangkit.citisnap.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.citisnap.databinding.LayoutItemUserBinding
import com.bangkit.citisnap.model.User
import com.bangkit.citisnap.ui.detailProfile.DetailProfileActivity
import com.bumptech.glide.Glide

class SearchAdapter (private val listUser: List<User>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    class SearchViewHolder(private val binding: LayoutItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding){
                username.text = user.username
                Glide.with(itemView.context).load(user.photoUrl).into(photoProfile)

                binding.item.setOnClickListener {
                    val intent = Intent(itemView.context, DetailProfileActivity::class.java)
                    intent.putExtra("username", user.username)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = LayoutItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(listUser[position])
    }
}