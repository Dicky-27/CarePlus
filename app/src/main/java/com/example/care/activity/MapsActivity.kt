package com.example.care.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.care.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.care.databinding.ActivityMapsBinding
import com.example.care.model.QuarantinePlace
import com.squareup.picasso.Picasso

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var quarantinePlace: QuarantinePlace

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quarantinePlace = intent.getParcelableExtra("intent_quarantine")!!
        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        binding.tvTitlePlace.text = quarantinePlace.title
        binding.tvAddressPlace.text = quarantinePlace.quarantinePlace?.address
        binding.tvTypePlace.text = quarantinePlace.type
        binding.tvPhone.text = quarantinePlace.phone
        Picasso.get()
            .load(quarantinePlace.imageUrl)
            .into(binding.ivPlace)

        mMap = googleMap
        val sydney = quarantinePlace.quarantinePlace.let { location ->
            LatLng(location?.latitude!!, location.longitude!!)
        }
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13.0f))

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
                    .putExtra("place", quarantinePlace)
            )
        }
    }
}