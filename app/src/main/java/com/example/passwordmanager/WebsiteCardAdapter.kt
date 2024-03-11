package com.example.passwordmanager

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.example.passwordmanager.databinding.SiteCardBinding
import java.text.SimpleDateFormat
import java.util.Locale

interface OnInteractionListenerWebsites {
    fun onWebsite(website: Website) {}
}

class WebsiteAdapter(
    private val onInteractionListener: OnInteractionListenerWebsites,
) : ListAdapter<Website, WebsiteAdapter.WebsiteViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder {
        val binding = SiteCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WebsiteViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        val site = getItem(position)
        holder.bind(site)
    }

   /* override fun getItemCount(): Int {
        return websiteList.size
    }*/

    class WebsiteViewHolder(
        private val binding: SiteCardBinding,
        private val onInteractionListener: OnInteractionListenerWebsites
    ) : RecyclerView.ViewHolder(binding.root) {

        private val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())

        fun bind(website: Website) {
            binding.apply {


                binding.titleTextView.text = website.name
                binding.descriptionTextView.text = website.description
                binding.dateTextView.text = website.dateOfAdding.toString() // todo: format date

                Glide.with(binding.iconImageView)
                    .load(website.iconURL)
                    .placeholder(R.drawable.icon_site_placeholder)
                    .error(R.drawable.icon_site_error)
                    .into(iconImageView)

            }
        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Website>() {
    override fun areItemsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem == newItem
    }
}
