package com.bangkit.citisnap.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bangkit.citisnap.model.Posts
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class UrgentPagingSource() : PagingSource<DocumentSnapshot, Posts>() {

    override suspend fun load(params: LoadParams<DocumentSnapshot>): LoadResult<DocumentSnapshot, Posts> {
        return try {
            val page = params.key
            val pageSize = params.loadSize

            val firestore = FirebaseFirestore.getInstance()
            val collectionRef = firestore.collection("posts")

            val query = collectionRef.orderBy("urgency", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())

            val currentPageQuery = if (page == null) {
                query
            } else {
                query.startAfter(page)
            }

            val querySnapshot = currentPageQuery.get().await()

            val postsList: MutableList<Posts> = mutableListOf()
            var lastVisibleProduct: DocumentSnapshot? = null

            for (document in querySnapshot.documents) {
                val urgency = document.getString("urgency").toString()

                if (urgency == "urgent" || urgency == "Harus Di Selesaikan"){
                    val username = document.getString("userId").toString()

                    val collectionRefUser = firestore.collection("users")
                    val userDocument = collectionRefUser.document(username).get().await()

                    val profileImage = userDocument.getString("profileImg").toString()
                    val text = document.getString("description").toString()
                    val imageUrl = document.getString("imageUrl").toString()

                    val postId = document.id

                    val posts = Posts(
                        username,
                        text.replace("\\n", "\n"),
                        imageUrl,
                        postId,
                        profileImage,
                        urgency
                    )
                    postsList.add(posts)
                }
            }

            if (querySnapshot.documents.isNotEmpty()) {
                lastVisibleProduct = querySnapshot.documents.last()
            }

            LoadResult.Page(
                data = postsList,
                prevKey = null,
                nextKey = lastVisibleProduct
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<DocumentSnapshot, Posts>): DocumentSnapshot? {
        return null
    }
}

