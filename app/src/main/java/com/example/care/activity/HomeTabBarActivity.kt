package com.example.care.activity

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.care.R
import com.example.care.databinding.ActivityHomeTabBarBinding
import com.example.care.fragment.HomeFragment
import com.example.care.fragment.ProfileFragment
import com.example.care.fragment.RegistFragment


class HomeTabBarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeTabBarBinding
    private lateinit var actionBar: ActionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeTabBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val firstFragment= HomeFragment()
        val secondFragment= RegistFragment()
        var thirdFragment = ProfileFragment()

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

                R.id.regist -> {
                    actionBar = supportActionBar!!
                    actionBar.title = "Regist"
                    setCurrentFragment(secondFragment)
                }

                R.id.profile -> {
                    actionBar = supportActionBar!!
                    actionBar.title = "Profile"
                    setCurrentFragment(thirdFragment)
                }
            }
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val backFrom = intent.getStringExtra("back")
        if (backFrom == "backFromRegist") {
            binding.bottomNavigationView.selectedItemId = R.id.regist
        } else {
            return
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
    }
}