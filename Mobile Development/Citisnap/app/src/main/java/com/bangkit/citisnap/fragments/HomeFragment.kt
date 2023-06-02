package com.bangkit.citisnap.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.adapter.PostAdapter
import com.bangkit.citisnap.databinding.FragmentHomeBinding
import com.bangkit.citisnap.model_dummy.Posts
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

                    val name = document.getString("userId")
                    val text = document.getString("description")
                    val imageUrl = document.getString("imageUrl")

                    Log.d("BERHASIL", "name = ${name.toString()}, text = ${text.toString()}")


                    val posts = Posts(
                        name.toString(),
                        text.toString().replace("\\n", "\n"),
                        imageUrl.toString()
                    )
                    userList.add(posts)

                    Log.d("DATA", userList.toString())
                }
                val animalAdapter = PostAdapter(userList)
                binding.recycle.adapter = animalAdapter
            }
            .addOnFailureListener { e ->
                Log.d("GAGAL", e.message.toString())
            }

        binding.recycle.layoutManager = LinearLayoutManager(context)
    }
}