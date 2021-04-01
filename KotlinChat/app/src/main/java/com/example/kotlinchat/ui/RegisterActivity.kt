package com.example.kotlinchat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinchat.R
import com.example.kotlinchat.databinding.ActivityRegisterBinding
import com.example.kotlinchat.util.Constants
import com.example.kotlinchat.util.gone
import com.example.kotlinchat.util.visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {
    private  lateinit var mAuth:FirebaseAuth
    private val mDatabase: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private lateinit var mRef:DatabaseReference
    private lateinit var mUserRef:DatabaseReference
    private  lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        binding.registerButton.setOnClickListener {
            val email:String=binding.registerEmail.text.toString().trim()
            val name:String=binding.registerName.text.toString().trim()
            val pass:String=binding.registerPassword.text.toString().trim()
            if (email.isNotEmpty()&&name.isNotEmpty()&&pass.isNotEmpty())
            {
                if(pass.length>=6)
                {
                    binding.registerProgressBar.visible()
                    registerUser(name,email,pass)

                } else
                {
                    binding.registerPassword.error="The password has min 6 charecters"
                }
            }
            else{
                if(email.isEmpty())binding.registerEmail.error="Mail cant be blank"
                if(name.isEmpty())binding.registerName.error="Name  cant be blank"
                if(pass.isEmpty())binding.registerPassword.error="Password cant be blank"
            }
        }
        binding.registerLoginButton.setOnClickListener {
            val loginGo=Intent(this,LoginActivity::class.java)
            startActivity(loginGo)
            finish()
        }
    }
    private fun registerUser(name:String, email:String, pass:String)
    {
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
            if(it.isSuccessful)
            {
                val currentUser=mAuth.currentUser
                val userId=currentUser?.uid
                mRef=mDatabase.reference
                mUserRef=mRef.child(Constants.CHILD_USERS).child(userId!!)
                val userMap=HashMap<String , String>()
                userMap["name"]=name
                userMap["profil_image"]="no_image"
                userMap["status"]="Hey there I'm using KurtWidow"
                mUserRef.setValue(userMap).addOnCompleteListener {
                    Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()
                    binding.registerProgressBar.gone()
                }
                val mainGo=Intent(this,MainActivity::class.java)
                startActivity(mainGo)
                finish()
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext,it.localizedMessage,Toast.LENGTH_LONG).show()
            binding.registerProgressBar.gone()
        }


    }

}