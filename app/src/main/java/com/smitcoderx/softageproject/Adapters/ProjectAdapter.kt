package com.smitcoderx.softageproject.Adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smitcoderx.softageproject.Models.LocationDetails
import com.smitcoderx.softageproject.databinding.SavedItemLayoutBinding

class ProjectAdapter() : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(private val binding: SavedItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(details: LocationDetails) {
            binding.apply {
                tvDate.text = details.date
                tvLatitudeLayout.text = details.lat
                tvLongitudeLayout.text = details.long
                tvStatusLayout.text = details.status
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding =
            SavedItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProjectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val currentItem = differ.currentList[position]

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<LocationDetails>() {
            override fun areItemsTheSame(
                oldItem: LocationDetails,
                newItem: LocationDetails
            ) = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: LocationDetails,
                newItem: LocationDetails
            ) = oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)
}