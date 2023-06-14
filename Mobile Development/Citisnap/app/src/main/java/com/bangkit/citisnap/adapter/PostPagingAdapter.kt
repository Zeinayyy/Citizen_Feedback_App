package com.bangkit.citisnap.adapter

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.LayoutItemPostBinding
import com.bangkit.citisnap.model.Posts
import com.bangkit.citisnap.ui.comment.CommentsActivity
import com.bangkit.citisnap.ui.detailProfile.DetailProfileActivity
import com.bangkit.citisnap.ui.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PostPagingAdapter :
    PagingDataAdapter<Posts, PostPagingAdapter.HomeViewHolder>(DIFF_CALLBACK) {

    class HomeViewHolder(private val binding: LayoutItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(posts: Posts) {
            with(binding) {
                val db = Firebase.firestore
                val username = Firebase.auth.currentUser?.displayName.toString()

                name.text = posts.name
                desc.text = posts.text

                Glide.with(itemView.context).load(posts.profileImage).into(profileImage)

                if (posts.imageUrl == "null") {
                    imageCard.visibility = View.GONE
                } else {
                    imageCard.visibility = View.VISIBLE
                    Glide.with(itemView.context).load(posts.imageUrl).into(image)
                }

                when (posts.urgency) {
                    "Harus Di Selesaikan" -> {
                        val colorStateList = ColorStateList.valueOf(Color.YELLOW)
                        binding.rlUrgency.backgroundTintList = colorStateList
                        urgency.text = itemView.context.getString(R.string.must_be_resolved)
                    }
                    "urgent" -> {
                        val colorStateList = ColorStateList.valueOf(Color.RED)
                        binding.rlUrgency.backgroundTintList = colorStateList
                        urgency.text = itemView.context.getString(R.string.urgent)
                    }
                    else -> {
                        binding.rlUrgency.visibility = View.GONE
                    }
                }

                binding.actionUp.setOnClickListener {
                    if (username != "null") {
                        db.collection("posts").document(posts.postId)
                            .collection("votes").document(username)
                            .get()
                            .addOnSuccessListener { documents ->
                                when (documents.getBoolean("vote_down")) {
                                    true -> {
                                        val vote = hashMapOf(
                                            "vote_up" to true,
                                            "vote_down" to false
                                        )

                                        db.collection("posts").document(posts.postId)
                                            .collection("votes").document(username)
                                            .set(vote)
                                    }
                                    false -> {

                                        db.collection("posts").document(posts.postId)
                                            .collection("votes").document(username)
                                            .delete()
                                    }
                                    else -> {
                                        val vote = hashMapOf(
                                            "vote_up" to true,
                                            "vote_down" to false
                                        )

                                        db.collection("posts").document(posts.postId)
                                            .collection("votes").document(username)
                                            .set(vote)
                                    }
                                }
                            }
                    } else {
                        itemView.context.startActivity(
                            Intent(
                                itemView.context,
                                LoginActivity::class.java
                            )
                        )
                    }

                }

                binding.actionDown.setOnClickListener {
                    if (username != "null") {
                        db.collection("posts").document(posts.postId)
                            .collection("votes").document(username)
                            .get()
                            .addOnSuccessListener { documents ->
                                when (documents.getBoolean("vote_up")) {
                                    true -> {
                                        val vote = hashMapOf(
                                            "vote_up" to false,
                                            "vote_down" to true
                                        )

                                        db.collection("posts").document(posts.postId)
                                            .collection("votes").document(username)
                                            .set(vote)
                                    }
                                    false -> {

                                        db.collection("posts").document(posts.postId)
                                            .collection("votes").document(username)
                                            .delete()
                                    }
                                    else -> {
                                        val vote = hashMapOf(
                                            "vote_up" to false,
                                            "vote_down" to true
                                        )

                                        db.collection("posts").document(posts.postId)
                                            .collection("votes").document(username)
                                            .set(vote)
                                    }
                                }
                            }
                    } else {
                        itemView.context.startActivity(
                            Intent(
                                itemView.context,
                                LoginActivity::class.java
                            )
                        )
                    }

                }
                db.collection("posts").document(posts.postId).collection("votes").document(username)
                    .addSnapshotListener { documentSnapshot, e ->
                        if (e != null) {
                            Toast.makeText(
                                itemView.context,
                                e.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return@addSnapshotListener
                        }

                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            val voteUp = documentSnapshot.getBoolean("vote_up")
                            val voteDown = documentSnapshot.getBoolean("vote_down")
                            val colorVoteUp =
                                ContextCompat.getColor(itemView.context, R.color.orange)
                            val colorVoteDown =
                                ContextCompat.getColor(itemView.context, R.color.red)

                            if (voteUp == true) {
                                binding.actionUp.backgroundTintList =
                                    ColorStateList.valueOf(colorVoteUp)
                                binding.actionDown.backgroundTintList = null
                            } else if (voteDown == true) {
                                binding.actionDown.backgroundTintList =
                                    ColorStateList.valueOf(colorVoteDown)
                                binding.actionUp.backgroundTintList = null
                            }
                        } else {
                            binding.actionUp.backgroundTintList = null
                            binding.actionDown.backgroundTintList = null
                        }

                    }

                db.collection("posts").document(posts.postId).collection("votes")
                    .addSnapshotListener { documentSnapshot, e ->
                        if (e != null) {
                            Toast.makeText(
                                itemView.context,
                                e.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        if (documentSnapshot != null) {
                            val voteUpCount = documentSnapshot.documents.count { it.getBoolean("vote_up") == true }

                            val votes = hashMapOf<String, Any>(
                                "votes" to voteUpCount
                            )

                            if (voteUpCount != 0) {

                                db.collection("posts").document(posts.postId)
                                    .update(votes)

                                binding.voteUpCount.text = buildString {
                                    append(voteUpCount)
                                    append(" ")
                                    append(itemView.context.getString(R.string.vote))
                                }
                                binding.voteUpCount.visibility = View.VISIBLE
                            } else {
                                db.collection("posts").document(posts.postId)
                                    .update(votes)
                                binding.voteUpCount.visibility = View.GONE
                            }
                        } else {
                            binding.voteUpCount.visibility = View.GONE
                        }

                    }

                if (username != "null") {
                    binding.comment.setOnClickListener {
                        val intent = Intent(itemView.context, CommentsActivity::class.java)
                        intent.putExtra("id Post", posts.postId)
                        itemView.context.startActivity(intent)
                    }
                } else {
                    binding.comment.setOnClickListener {
                        itemView.context.startActivity(
                            Intent(
                                itemView.context,
                                LoginActivity::class.java
                            )
                        )
                    }
                }

                binding.profileImage.setOnClickListener {
                    val intent = Intent(itemView.context, DetailProfileActivity::class.java)
                    intent.putExtra("username", posts.name)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding =
            LayoutItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CommentsActivity::class.java)
            intent.putExtra("id Post", data?.postId)
            holder.itemView.context.startActivity(intent)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Posts>() {
            override fun areItemsTheSame(oldItem: Posts, newItem: Posts): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Posts, newItem: Posts): Boolean {
                return oldItem.postId == newItem.postId
            }
        }
    }

}