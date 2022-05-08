package com.example.care

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.example.care.databinding.ActivityHomeTabBarBinding
import com.example.care.databinding.ActivityLoginBinding
import com.example.care.fragment.HomeFragment
import com.example.care.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarMenu

class HomeTabBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeTabBarBinding
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTabBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment=HomeFragment()
        val secondFragment=ProfileFragment()

        actionBar = supportActionBar!!
        actionBar.title = "Home"
        setCurrentFragment(firstFragment)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> {
                    actionBar = supportActionBar!!
                    actionBar.title = "Home"
                    setCurrentFragment(firstFragment)
                }
                R.id.profile -> {
                    actionBar = supportActionBar!!
                    actionBar.title = "Profile"
                    setCurrentFragment(secondFragment)
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }
}