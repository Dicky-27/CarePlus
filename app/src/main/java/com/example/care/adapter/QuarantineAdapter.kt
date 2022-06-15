package com.example.care.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.care.R
import com.example.care.model.QuarantinePlace
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class QuarantineAdapter(
    query: Query,
    private val listener: QuarantineAdapterListener
) : FirestoreAdapter<QuarantineAdapter.QuarantineViewHolder>(query) {

    class QuarantineViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val address: TextView = itemView.findViewById(R.id.tv_address)
        private val image: ImageView = itemView.findViewById(R.id.iv_quarantine)
        private val cardView: CardView = itemView.findViewById(R.id.item_quarantine_card)

        fun bind(snapshot: DocumentSnapshot, listener: QuarantineAdapterListener) {
            val quarantinePlaces: QuarantinePlace? = snapshot.toObject(QuarantinePlace::class.java)
            title.text = quarantinePlaces?.title
            address.text = quarantinePlaces?.quarantinePlace?.address
            Picasso.get()
                .load(quarantinePlaces?.imageUrl)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.loading_img_animation)
                .into(image)

            cardView.setOnClickListener {
                listener.onPlaceSelected(quarantinePlaces)
            }
        }
    }

    interface QuarantineAdapterListener {
        fun onPlaceSelected(quarantinePlaces: QuarantinePlace?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuarantineViewHolder {
        return QuarantineViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_quarantine_place, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: QuarantineViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}