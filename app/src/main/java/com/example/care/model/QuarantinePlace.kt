package com.example.care.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuarantinePlace(
    @DocumentId
    var documentId: String? = null,
    var title: String? = null,
    var imageUrl: String? = null,
    var quarantinePlace: Location? = null,
    var phone: String? = null,
    var type: String? = null
): Parcelable

@Parcelize
data class Location(
    var address: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null
): Parcelable