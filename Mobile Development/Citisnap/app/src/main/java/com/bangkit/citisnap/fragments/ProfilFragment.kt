package com.bangkit.citisnap.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bangkit.citisnap.ui.main.MainActivity
import com.bangkit.citisnap.databinding.FragmentProfilBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfilFragment : Fragment() {

    private lateinit var binding: FragmentProfilBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }

        binding.ganti.setOnClickListener {
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse("https://firebasestorage.googleapis.com/v0/b/c23-pc676-capstone-project.appspot.com/o/profile_picture.png?alt=media&token=7fdc2193-9de5-4ad1-a312-0304541d6ebd"))
                .build()

            val user = auth.currentUser

            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "BERHASIL", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "GAGAL", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        Glide.with(requireContext()).load(auth.currentUser?.photoUrl).into(binding.profilePict)
    }

}