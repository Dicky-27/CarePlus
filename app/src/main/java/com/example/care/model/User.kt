package com.example.care.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @DocumentId
    var documentId: String,
    var name: String,
    var email: String,
    var phoneNumber: String,
) : Parcelable

