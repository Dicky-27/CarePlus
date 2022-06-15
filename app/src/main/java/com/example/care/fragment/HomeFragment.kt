package com.example.care.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.care.activity.HomeTabBarActivity
import com.example.care.activity.MapsActivity
import com.example.care.adapter.QuarantineAdapter
import com.example.care.databinding.FragmentHomeBinding
import com.example.care.model.QuarantinePlace
import com.firebase.geofire.GeoQueryBounds
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var quarantineAdapter: QuarantineAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val query: Query = FirebaseFirestore
            .getInstance()
            .collection("quarantinePlace")

        val recyclerView = binding.recyclerList
        quarantineAdapter = QuarantineAdapter(query, object : QuarantineAdapter.QuarantineAdapterListener {
            override fun onPlaceSelected(quarantinePlaces: QuarantinePlace?) {
                startActivity(
                    Intent(activity, MapsActivity::class.java)
                        .putExtra("intent_quarantine", quarantinePlaces)
                )
            }
        })
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = quarantineAdapter
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        quarantineAdapter.startListening()
        quarantineAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        quarantineAdapter.stopListening()
        quarantineAdapter.notifyDataSetChanged()
    }
}
