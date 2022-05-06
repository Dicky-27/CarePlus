package com.example.care.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var name: String,
    var email: String,
    var phoneNumber: String,
) : Parcelable

