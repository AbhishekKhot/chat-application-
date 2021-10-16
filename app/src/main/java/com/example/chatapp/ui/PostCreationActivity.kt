package com.example.chatapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapp.db.PostDao
import com.example.chatapp.R
import kotlinx.android.synthetic.main.activity_pos_creation.*

class PostCreationActivity : AppCompatActivity() {

    lateinit var postDao: PostDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pos_creation)

        postDao = PostDao()

        postButton.setOnClickListener {
            val input = postInput.text.toString().trim()
            if(input.isNotEmpty()) {
                postDao.addPost(input)
            }
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

}