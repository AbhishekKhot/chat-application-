package com.example.chatapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.chatapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        textview_register_option.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        button_signup.setOnClickListener {
           when{
               TextUtils.isEmpty(textview_email_signup.text.toString().trim() {it <= ' '}) -> {
                   Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show()
               }

               TextUtils.isEmpty(textview_password_signup.text.toString().trim() {it<= ' '}) -> {
                   Toast.makeText(this,"Please enter the password",Toast.LENGTH_LONG).show()
               }
               else -> {
                   val email = textview_email_signup.text.toString().trim() {it <= ' '}
                   val password = textview_password_signup.text.toString().trim() {it <= ' '}

                   FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                       .addOnCompleteListener(
                           OnCompleteListener { task ->
                               if(task.isSuccessful){
                                   val firebaseUer:FirebaseUser = task.result!!.user!!
                                   Toast.makeText(this,"You have successfully Registered",Toast.LENGTH_LONG).show()
                                   val intent = Intent(this, MainActivity::class.java)
                                   intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                   intent.putExtra("user_id",firebaseUer.uid)
                                   intent.putExtra("email_id",email)
                                   startActivity(intent)
                                   finish()
                               }
                               else{
                                   Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()

                               }
                           }
                       )
               }
           }
        }
    }
}