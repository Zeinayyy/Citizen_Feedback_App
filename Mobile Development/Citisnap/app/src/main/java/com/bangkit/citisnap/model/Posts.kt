package com.bangkit.citisnap.model

import com.google.firebase.firestore.DocumentSnapshot

data class Posts(
    val name: String,
    val text: String,
    val imageUrl: String,
    val postId: String,
    val profileImage: String,
    val urgency: String
)