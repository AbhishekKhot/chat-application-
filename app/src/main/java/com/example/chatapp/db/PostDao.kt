package com.example.chatapp.db

import com.example.chatapp.data.Post
import com.example.chatapp.data.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PostDao {

    val db = FirebaseFirestore.getInstance()
    val postCollections = db.collection("posts")
    val auth = Firebase.auth

    fun addPost(text:String) {
        val currUserId = auth.currentUser!!.uid
        GlobalScope.launch {
            val userDao = UserDao()
            val user = userDao.getUserById(currUserId).await().toObject(User::class.java)!!

            val currentTime = System.currentTimeMillis()
            val post = Post(text,user,currentTime)
            postCollections.document().set(post)
        }
    }

    private fun getPostById(postId:String): Task<DocumentSnapshot> {
        return postCollections.document(postId).get()
    }

    fun likeCount(postId:String){
        GlobalScope.launch {
            val post = getPostById(postId).await().toObject(Post::class.java) !!
            val currentUserId = auth.currentUser!!.uid
            val isLiked = post.likedBy.contains(currentUserId)

            if(isLiked){
                post.likedBy.remove(currentUserId)
            }else{
                post.likedBy.remove(currentUserId)
            }
            postCollections.document(postId).set(post)
        }
    }
}