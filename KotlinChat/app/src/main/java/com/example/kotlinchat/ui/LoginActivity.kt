package com.example.kotlinchat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.kotlinchat.R
import com.example.kotlinchat.databinding.ActivityLoginBinding
import com.example.kotlinchat.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {
    private  lateinit var mAuth: FirebaseAuth
    private  lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener {
            val email=binding.inputEmail.text.toString().trim()
            val pass=binding.inputPassword.text.toString().trim()
            singIn(email,pass)
        }
        binding.gotoRegister.setOnClickListener {
            val registerGo=Intent(this,RegisterActivity::class.java)
            startActivity(registerGo)
            finish()
        }
    }
    private fun singIn(email:String,pass:String) {
        if(email.isEmpty()) {
            binding.inputEmail.error = "Mail yazmalisin"

            }
        else if (pass.isEmpty()) {

                binding.inputPassword.error="Parola girmelisin"
            }
        else {
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful)
            {
                val currentUser=mAuth.currentUser?.email.toString()
                Toast.makeText(this,"Hosgeldin: ${currentUser}",Toast.LENGTH_LONG).show()
                val  mainGo1= Intent(this,MainActivity::class.java)
                startActivity(mainGo1)
                finish()
            }
        }.addOnFailureListener{
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
        }
        }
    }
}