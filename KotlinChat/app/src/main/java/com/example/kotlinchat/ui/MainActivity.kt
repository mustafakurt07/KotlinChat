package com.example.kotlinchat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.kotlinchat.R
import com.example.kotlinchat.adapter.ViewPagerAdapter
import com.example.kotlinchat.databinding.ActivityMainBinding
import com.example.kotlinchat.fragment.ChatsFragment
import com.example.kotlinchat.fragment.FriendsFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private  lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth= FirebaseAuth.getInstance()
        ui()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId)
        {
            R.id.action_logout -> {
                mAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                finish()
            }



        }
        return true
    }
    private fun ui()
    {  val  toolbar=binding.mainToolbar
        setSupportActionBar(toolbar)
        setupViewPager()
        binding.mainTabs.setupWithViewPager(binding.mainViewPager)

    }
    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.apply {
            addFragment(ChatsFragment(), "Mesajlar")
            addFragment(FriendsFragment(), "Arkadaşlar")
        }
        binding.mainViewPager .adapter = adapter
    }

}