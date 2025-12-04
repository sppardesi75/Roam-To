package com.sanskarpardesi.roamto_sanskar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sanskarpardesi.roamto_sanskar.data.Place
import com.sanskarpardesi.roamto_sanskar.databinding.ItemSavedPlaceBinding

class SavedPlaceAdapter(
    private val onDelete: (Place) -> Unit,
    private val onClick: (Place) -> Unit
) : RecyclerView.Adapter<SavedPlaceAdapter.SavedPlaceViewHolder>() {

    private var places: List<Place> = listOf()

    fun submitList(newList: List<Place>) {
        places = newList
        notifyDataSetChanged()
    }

    inner class SavedPlaceViewHolder(private val binding: ItemSavedPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            binding.textTitle.text = place.title
            binding.textDescription.text = place.description
            binding.textDate.text = place.date

            binding.root.setOnClickListener {
                onClick(place)
            }

            binding.btnDelete.setOnClickListener {
                onDelete(place)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSavedPlaceBinding.inflate(inflater, parent, false)
        return SavedPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedPlaceViewHolder, position: Int) {
        holder.bind(places[position])
    }

    override fun getItemCount(): Int = places.size
}
