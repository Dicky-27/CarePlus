package com.example.care.fragment

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.care.activity.LoginActivity
import com.example.care.databinding.FragmentProfileBinding
import com.example.care.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storageReference: StorageReference
    private lateinit var dialog: Dialog
    private lateinit var user: User
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.currentUser?.uid.toString()



        if(uid.isNotEmpty()){
            getUserData(uid)
        }


        checkUser()



        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val email = firebaseUser.email
            binding.tvEmail.text = email
        } else {
            val intent = Intent (activity, LoginActivity::class.java)
            activity?.startActivity(intent)
        }
    }


    private fun getUserData(uid : String){

        val userRef = FirebaseFirestore
            .getInstance().collection("users").document(uid)
        userRef.get()
            .addOnCompleteListener {
                if(it.isSuccessful){

                    binding.tvEmailPhone.text = it.result["phoneNumber"].toString()
                    binding.tvEmailName.text = it.result["name"].toString()
                }
            }
    }
}