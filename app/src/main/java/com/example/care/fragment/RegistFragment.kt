package com.example.care.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.care.R
import com.example.care.activity.LoginActivity
import com.example.care.adapter.RegistAdapter
import com.example.care.databinding.FragmentProfileBinding
import com.example.care.databinding.FragmentRegistBinding
import com.example.care.model.PlaceReserved
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class RegistFragment : Fragment() {
    private lateinit var binding: FragmentRegistBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registAdapter: RegistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRegistBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun initView() {
        val firebaseUser = firebaseAuth.currentUser
        val recyclerView = binding.recyclerListRegist
        if (firebaseUser != null) {
            val query: Query = FirebaseFirestore
                .getInstance()
                .collection("regist")
                .whereEqualTo("userId", firebaseUser.uid)

            registAdapter = RegistAdapter(query, object : RegistAdapter.RegistAdapterListener {
                override fun onItemSelected(regist: PlaceReserved?) {

                }
            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = registAdapter
            }

        } else {
            val intent = Intent (activity, LoginActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        registAdapter.startListening()
        registAdapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        registAdapter.startListening()
        registAdapter.notifyDataSetChanged()
    }
}