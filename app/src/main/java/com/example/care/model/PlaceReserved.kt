package com.example.care.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class PlaceReserved(
    @DocumentId
    var documentId: String? = null,
    var createAt: Date = Calendar.getInstance().time,
    var placeId: String? = null,
    var userId: String? = null,
    var name: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var address: String? = null,
    var status: String? = null
) : Parcelable

enum class Status(val value: String) {
    Accept("Diterima"),
    Except("Ditolak"),
    Waiting("Menunggu"),
}