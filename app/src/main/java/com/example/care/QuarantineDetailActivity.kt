package com.example.care

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.care.databinding.ActivityQuarantineDetailBinding
import com.example.care.model.QuarantinePlace
import com.squareup.picasso.Picasso

class QuarantineDetailActivity : AppCompatActivity() {

    private lateinit var quarantinePlace: QuarantinePlace
    private lateinit var binding: ActivityQuarantineDetailBinding

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuarantineDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quarantinePlace = intent.getParcelableExtra("intent_quarantine")!!
        supportActionBar?.hide()
        configureView()
    }


    private fun configureView() {
        binding.tvTitlePlace.text = quarantinePlace.title
        binding.tvAddressPlace.text = quarantinePlace.quarantinePlace?.address
        Picasso.get()
            .load(quarantinePlace.imageUrl)
            .into(binding.ivPlace)

        binding.btnSeeLocation.setOnClickListener {
            val uri = Uri.parse("google.navigation:q=${quarantinePlace.quarantinePlace?.latitude},${quarantinePlace.quarantinePlace?.longitude}&mode=1")
            val locationIntent = Intent(Intent.ACTION_VIEW, uri)
            locationIntent.setPackage("com.google.android.apps.maps")
            locationIntent.resolveActivity(packageManager)?.let {
                startActivity(locationIntent)
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegist.setOnClickListener {
            startActivity(
                Intent(this, FormRegistActivity::class.java)
            )
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(
            Intent(this, HomeTabBarActivity::class.java)
        )
    }
}