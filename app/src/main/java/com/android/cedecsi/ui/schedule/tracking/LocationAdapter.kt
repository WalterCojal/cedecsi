package com.android.cedecsi.ui.schedule.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.cedecsi.R
import com.android.cedecsi.room.entity.Location
import com.android.cedecsi.util.getStringFormatDate

class LocationAdapter: RecyclerView.Adapter<LocationAdapter.Holder>() {

    var items = listOf<Location>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(items[position]) {
            holder.txtId.text = "Ubicación N $id"
            holder.txtLatitude.text = "Latitud: $latitude"
            holder.txtLongitude.text = "Longitud: $longitude"
            holder.txtDate.text = "Fecha: ${date.getStringFormatDate()}"
        }
    }

    override fun getItemCount(): Int = items.size

    inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {

        var txtId = itemView.findViewById<TextView>(R.id.txtId)
        var txtLatitude = itemView.findViewById<TextView>(R.id.txtLatitude)
        var txtLongitude = itemView.findViewById<TextView>(R.id.txtLongitude)
        var txtDate = itemView.findViewById<TextView>(R.id.txtDate)

    }

}