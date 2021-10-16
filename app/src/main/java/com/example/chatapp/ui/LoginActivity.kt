package com.example.chatapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.chatapp.R
import com.example.chatapp.data.User
import com.example.chatapp.db.UserDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        textview_register.setOnClickListener {
           startActivity(Intent(this, SignupActivity::class.java))
        }
        button_login.setOnClickListener {
            when {
                TextUtils.isEmpty(edit_text_email.text.toString().trim() {it <= ' '}) -> {
                    Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show()
                }

                TextUtils.isEmpty(edit_text_password.text.toString().trim() {it <= ' '}) -> {
                    Toast.makeText(this,"Please enter the password",Toast.LENGTH_LONG).show()
                }
                else ->{
                    val email:String = edit_text_email.text.toString().trim() {it <= ' '}
                    val password:String = edit_text_password.text.toString().trim() {it <= ' '}

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(this,"You are logged in successfully",Toast.LENGTH_LONG).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id",FirebaseAuth.getInstance().currentUser!!.email)
                                startActivity(intent)
                                finish()
                            }
                            else{
                                Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        updateUi(currentUser)
    }

    @SuppressLint("RestrictedApi")
    fun updateUi(firebaseUser:FirebaseUser?){
        if(firebaseUser != null){
            val user = User(firebaseUser.uid,firebaseUser.email.toString())
            val usersDao = UserDao()
            usersDao.addUsers(user)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        else{
            Toast.makeText(this,"Login here to continue..",Toast.LENGTH_LONG).show()
        }
    }
}