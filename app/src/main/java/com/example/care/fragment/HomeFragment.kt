package com.example.care.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.care.QuarantineDetailActivity
import com.example.care.databinding.FragmentHomeBinding
import com.example.care.adapter.QuarantineAdapter
import com.example.care.model.QuarantinePlace
import com.google.firebase.firestore.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var quarantineAdapter: QuarantineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val query: Query = FirebaseFirestore.getInstance().collection("quarantinePlace")
        val recyclerView = binding.recyclerList
        quarantineAdapter = QuarantineAdapter(query, object : QuarantineAdapter.QuarantineAdapterListener {
            override fun onPlaceSelected(quarantinePlaces: QuarantinePlace?) {
                startActivity(
                    Intent(activity, QuarantineDetailActivity::class.java)
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
    }

    override fun onStop() {
        super.onStop()
        quarantineAdapter.stopListening()
    }
}
