package com.example.care.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.care.R
import com.example.care.model.QuarantinePlace
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class QuarantineAdapter(
    query: Query,
    private val listener: QuarantineAdapterListener
) : FirestoreAdapter<QuarantineAdapter.QuarantineViewHolder>(query) {

    class QuarantineViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.tv_title)

        fun bind(snapshot: DocumentSnapshot, listener: QuarantineAdapterListener) {
            val quarantinePlaces: QuarantinePlace? = snapshot.toObject(QuarantinePlace::class.java)
            title.text = quarantinePlaces?.title

            title.setOnClickListener {
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