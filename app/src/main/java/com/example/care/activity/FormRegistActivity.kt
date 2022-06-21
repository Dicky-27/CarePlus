package com.example.care.activity

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.care.R
import com.example.care.databinding.ActivityFormRegistBinding
import com.example.care.model.PlaceReserved
import com.example.care.model.QuarantinePlace
import com.example.care.model.Status
import com.example.care.utils.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.timerTask

class FormRegistActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormRegistBinding
    private lateinit var  firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var quarantinePlace: QuarantinePlace

    private var name = ""
    private var email = ""
    private var phone = ""
    private var address = ""
    private var placeId = ""
    @RequiresApi(Build.VERSION_CODES.O)
    private var dateCheckin = Date()

    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationManager: NotificationManager
    lateinit var builder: Notification.Builder
    private val channelId = "12345"
    private val description = "Notification Status"

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormRegistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        quarantinePlace = intent.getParcelableExtra("place")!!

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Create regist...")
        progressDialog.setCanceledOnTouchOutside(false)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        binding.btnSubmit.setOnClickListener {
            didTapRegist()
        }

        binding.edDate.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                binding.edDate.setText("" + dayOfMonth + "-" + monthOfYear + "-" + year)
                val calendar = Calendar.getInstance()
                calendar[Calendar.YEAR] = year
                calendar[Calendar.MONTH] = monthOfYear
                calendar[Calendar.DATE] = dayOfMonth
                val date = calendar.time
                dateCheckin = date
            }, year, month, day)

            dpd.show()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(id: String) {
        val rnds = (0..2).random()
        val intent = Intent(this, HomeTabBarActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager .IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId)
                .setContentTitle("Status Registration")
                .setContentText("Hi your registration has been updated!")
                .setSmallIcon(R.mipmap.ic_care_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap
                    .ic_care_logo))
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this, channelId)
                .setContentTitle("Status Registration")
                .setContentText("Hi your registration has been updated!")
                .setSmallIcon(R.mipmap.ic_care_logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap
                    .ic_care_logo))
                .setContentIntent(pendingIntent)
        }

        Timer().schedule(timerTask {
            if (rnds == 0) {
                FirebaseFirestore
                    .getInstance()
                    .collection("regist")
                    .document(id)
                    .update("status", Status.Accept.value)
                    .addOnSuccessListener {
                        notificationManager.notify(12345, builder.build())
                    }
                    .addOnFailureListener { e ->
                        Log.v("firebase", "Failed to store coused $e")
                    }
            } else {
                FirebaseFirestore
                    .getInstance()
                    .collection("regist")
                    .document(id)
                    .update("status", Status.Except.value)
                    .addOnSuccessListener {
                        notificationManager.notify(12345, builder.build())
                    }
                    .addOnFailureListener { e ->
                        Log.v("firebase", "Failed to store coused $e")
                    }
            }
        }, 10000)
    }

    fun Context.popToRoot() {
        val intent = Intent(this, HomeTabBarActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(
            intent
                .putExtra("back", "backFromRegist"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("RemoteViewLayout")
    private fun didTapRegist() {
        email = binding.edEmail.text.toString().trim()
        name = binding.edName.text.toString().trim()
        phone = binding.edNumber.text.toString().trim()
        address = binding.edAddress.text.toString().trim()

        val dialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_after_regist)

        var btnBack = dialog.findViewById<Button>(R.id.btn_back_home)

        btnBack.setOnClickListener {
            dialog.dismiss()
            this.popToRoot()
        }

        val firebaseUser = firebaseAuth.currentUser
        val newRegist = PlaceReserved(
            "",
            Calendar.getInstance().time,
            quarantinePlace.documentId,
            firebaseUser!!.uid.toString(),
            name,
            phone,
            email,
            address,
            Status.Waiting.value,
            dateCheckin
        )
        progressDialog.show()
        FirebaseUtils().fireStoreDatabase.collection("regist")
            .add(newRegist)
            .addOnSuccessListener {
                progressDialog.dismiss()
                dialog.show()
                showNotification(it.id)
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Regist failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}