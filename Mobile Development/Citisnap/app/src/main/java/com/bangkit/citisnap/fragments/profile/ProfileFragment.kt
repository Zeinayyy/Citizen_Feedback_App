package com.bangkit.citisnap.fragments.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.citisnap.R
import com.bangkit.citisnap.databinding.FragmentProfileBinding
import com.bangkit.citisnap.ui.main.MainActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        profileViewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]

        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val currentUser = auth.currentUser

        binding.recycle.layoutManager = LinearLayoutManager(context)

        profileViewModel.getDataUser(currentUser?.displayName.toString())
        profileViewModel.getPostUser(currentUser?.displayName.toString())
        profileViewModel.isLoading.observe(requireActivity()){ showLoading(it) }
        profileViewModel.listDataUser.observe(requireActivity()){ dataUser(it) }
        profileViewModel.adapter.observe(requireActivity()){ binding.recycle.adapter = it }




//        binding.logout.setOnClickListener {
//            auth.signOut()
//            startActivity(Intent(requireContext(), MainActivity::class.java))
//            activity?.finish()
//        }
//
//        val photoUrl = "https://cdn.discordapp.com/attachments/1116004970714632324/1116023923084099654/Oscar-Oasis-02-448x336.png"
//
//        binding.ganti.setOnClickListener {
//            val profileUpdates = UserProfileChangeRequest.Builder()
//                .setPhotoUri(Uri.parse(photoUrl))
//                .build()
//
//            val user = auth.currentUser
//
//            val db = Firebase.firestore
//            val auth = Firebase.auth
//            val currentUser = auth.currentUser
//
//            val update = hashMapOf<String, Any>(
//                "profileImg" to photoUrl
//            )
//
//            db.collection("users").document(currentUser?.displayName.toString())
//                .update(update)
//
//            user?.updateProfile(profileUpdates)
//                ?.addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Toast.makeText(requireContext(), "BERHASIL", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "GAGAL", Toast.LENGTH_SHORT).show()
//                    }
//                }
//        }
//
//        Glide.with(requireContext()).load(auth.currentUser?.photoUrl).into(binding.profilePict)
//    }

    }

    private fun dataUser(listData: List<String>) {
        val name = listData[0]
        val profileImg = listData[1]
        val username = listData[2]
        (mContext as AppCompatActivity).supportActionBar?.title = username
        Glide.with(mContext).load(profileImg).into(binding.profileImg)
        binding.name.text = name

    }


    private fun showLoading(state: Boolean){ binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE}
}