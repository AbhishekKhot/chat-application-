package com.example.chatapp.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_photo_upload.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*


private const val REQUEST_CODE_IMAGE_PICK = 0

class PhotoUploadActivity : AppCompatActivity() {

      var currFile: Uri? = null
     val imageRef = Firebase.storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_upload)

        ChooseButton.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
            }
        }

        UploadButton.setOnClickListener {
            uploadImageToStorage(UUID.randomUUID().toString())
        }
    }



    private fun uploadImageToStorage(filename:String) = CoroutineScope(Dispatchers.IO).launch {
        try {
            currFile?.let {
                imageRef.child("images/$filename").putFile(it).await()
                withContext(Dispatchers.Main){
                    Toast.makeText(this@PhotoUploadActivity,"Successfully uploaded image", Toast.LENGTH_LONG).show()
                }
            }

        }catch (e:Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(this@PhotoUploadActivity,e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                currFile = it
                ImageViewUpload.setImageURI(it)
            }
        }
    }
}