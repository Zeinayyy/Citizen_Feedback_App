package com.bangkit.citisnap.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.citisnap.databinding.LayoutItemCommentBinding
import com.bangkit.citisnap.model.Comments
import com.bumptech.glide.Glide

class CommentsAdapter (private val listComments: List<Comments>) : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {

    class CommentsViewHolder(private val binding: LayoutItemCommentBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(comments: Comments){
            with(binding){
                name.text = comments.name
                description.text = comments.description
                Glide.with(itemView.context).load(comments.profileImage).into(binding.profileImage)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val binding = LayoutItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listComments.size
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(listComments[position])
    }
}