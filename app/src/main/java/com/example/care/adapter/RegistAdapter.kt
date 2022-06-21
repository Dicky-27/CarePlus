package com.example.care.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.care.R
import com.example.care.model.PlaceReserved
import com.example.care.model.QuarantinePlace
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class RegistAdapter(
    query: Query,
    private val listener: RegistAdapterListener
) : FirestoreAdapter<RegistAdapter.RegistViewHolder>(query) {

    class RegistViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val cardStatus: CardView = itemView.findViewById(R.id.card_status)
        private val title: TextView = itemView.findViewById(R.id.tv_title)
        private val date: TextView = itemView.findViewById(R.id.tv_date)
        private val image: ImageView = itemView.findViewById(R.id.iv_quarantine)
        private val status: TextView = itemView.findViewById(R.id.tv_status)

        fun bind(snapshot: DocumentSnapshot, listener: RegistAdapterListener) {
            val registItem: PlaceReserved? = snapshot.toObject(PlaceReserved::class.java)
            status.text = registItem?.status
            val sdf = SimpleDateFormat("EEEE, dd MMM yyyy | hh:mm")
            val dateFormat = sdf.format(registItem?.createAt).toString()
            date.text = dateFormat

            if (registItem?.status == "Menunggu") {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.brown))
                status.setBackgroundResource(R.drawable.bg_corner_yellow)
                status.setPadding(20, 4, 20, 4)
            } else if (registItem?.status == "Diterima") {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.primaryBlue))
                status.setBackgroundResource(R.drawable.bg_corner_radius)
                status.setPadding(20, 4, 20, 4)
            } else {
                status.setTextColor(ContextCompat.getColor(itemView.context, R.color.red_error))
                status.setBackgroundResource(R.drawable.bg_corner_radius_red)
                status.setPadding(20, 4, 20, 4)
            }

            FirebaseFirestore.getInstance().collection("quarantinePlace")
                .document(registItem?.placeId.toString())
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val data = document.toObject(QuarantinePlace::class.java)
                        title.text = data?.title
                        Picasso.get()
                            .load(data?.imageUrl)
                            .fit()
                            .centerCrop()
                            .placeholder(R.drawable.loading_img_animation)
                            .into(image)
                    } else {
                        Log.v("firebase", "No such document")
                    }
                }
                .addOnFailureListener { e ->
                    Log.v("firebase", "Failed to read coused $e")
                }

            cardStatus.setOnClickListener {
                listener.onItemSelected(registItem)
            }
        }


    }

    interface RegistAdapterListener {
        fun onItemSelected(regist: PlaceReserved?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistViewHolder {
        return RegistViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_regist_status, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RegistViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}
