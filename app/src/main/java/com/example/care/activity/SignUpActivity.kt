package com.example.care.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.care.databinding.ActivitySignUpBinding
import com.example.care.model.User
import com.example.care.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var actionBar: ActionBar
    private lateinit var progressDialog: ProgressDialog
    private lateinit var  firebaseAuth: FirebaseAuth

    private var name = ""
    private var email = ""
    private var phone = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            validateData()
        }
        binding.tvAccount.setOnClickListener {
            onBackPressed()
        }
    }

    private fun validateData() {
        email = binding.edEmail.text.toString().trim()
        password = binding.edPassword.text.toString().trim()
        name = binding.edName.text.toString().trim()
        phone = binding.edPhone.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailTil.error = "Invalid email format"
        } else if (TextUtils.isEmpty(name)) {
            binding.nameTil.error = "Please enter name"
        } else if (TextUtils.isEmpty(phone)) {
            binding.phoneTil.error = "Please enter phone number"
        } else if (TextUtils.isEmpty(password)) {
            binding.passwordTil.error = "Please enter password"
        } else if (password.length < 6){
            binding.passwordTil.error = "Password must atleast 6 characters long"
        } else {
            firebaseSignup()
        }
    }

    private fun firebaseSignup() {
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                val newUser = User(firebaseUser.uid.toString(), name, email.toString(), phone)

                FirebaseUtils().fireStoreDatabase.collection("users")
                    .document(firebaseUser.uid)
                    .set(newUser)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeTabBarActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(this, "SignUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                    }

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}