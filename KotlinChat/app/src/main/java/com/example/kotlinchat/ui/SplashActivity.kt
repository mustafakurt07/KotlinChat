package com.example.kotlinchat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.kotlinchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.Transaction

class SplashActivity : AppCompatActivity() {
    private  lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mAuth= FirebaseAuth.getInstance()
        val  user=mAuth.currentUser
        Handler().postDelayed({
                              if(user!=null)
                              {
                                  val intent=Intent(this,MainActivity::class.java)
                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                  startActivity(intent)
                              }
            else
                              {
                                  val intent=Intent(this,LoginActivity::class.java)
                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                  startActivity(intent)

                              }
        },2000)
    }
}