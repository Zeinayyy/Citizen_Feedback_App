package com.bangkit.citisnap.fragments

import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.adapter.PostAdapter
import com.bangkit.citisnap.databinding.FragmentHomeBinding
import com.bangkit.citisnap.model.Posts
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firestore = FirebaseFirestore.getInstance()
        val collectionRef = firestore.collection("posts")

        val userList: MutableList<Posts> = mutableListOf()

        collectionRef.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {

                    val username = document.getString("userId").toString()
                    val text = document.getString("description").toString()
                    val imageUrl = document.getString("imageUrl").toString()
                    val postId = document.id

                    Log.d("TEST", username)

                    val collectionRefUser = firestore.collection("users")
                    collectionRefUser.document(username).get()
                        .addOnSuccessListener { user->
                            val name = user.getString("name").toString()
                            val profileImage = user.getString("profileImg").toString()

                            val posts = Posts(
                                name,
                                text.replace("\\n", "\n"),
                                imageUrl,
                                postId,
                                profileImage
                            )

                            userList.add(posts)

                            val adapter = PostAdapter(userList)
                            binding.recycle.adapter = adapter
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), e.message.toString(), Toast.LENGTH_SHORT).show()
            }

        binding.recycle.layoutManager = LinearLayoutManager(context)
    }
}