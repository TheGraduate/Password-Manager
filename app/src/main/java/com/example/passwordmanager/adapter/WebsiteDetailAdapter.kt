package com.example.passwordmanager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.WebsiteFragmentBinding
import com.example.passwordmanager.dto.Website

class WebsiteDetailAdapter(
    private val onInteractionListener: OnInteractionListenerWebsites,
) : ListAdapter<Website, WebsiteDetailAdapter.WebsiteDetailViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteDetailViewHolder {
        val binding = WebsiteFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WebsiteDetailViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: WebsiteDetailViewHolder, position: Int) {
        val webSite = getItem(position)
        holder.bind(webSite)
    }

   /* override fun getItemCount(): Int {
        return websiteList.size
    }*/

    class WebsiteDetailViewHolder(
        private val binding: WebsiteFragmentBinding,
        private val onInteractionListener: OnInteractionListenerWebsites,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(website: Website) {
            binding.apply {
                binding.UsersWebsiteName.text = website.name
                binding.UsersWebsiteLogin.text = website.login
                binding.UsersWebsitePassword.text = website.password
                binding.UsersWebsiteURL.text = website.url
                binding.UsersWebsiteDescription.text = website.description
                binding.dateOfAdding.text = website.dateOfAdding

                iconImageWebsiteMenuOnWebsiteFragment.setOnClickListener {
                    PopupMenu(it.context, it).apply {
                        inflate(R.menu.options_for_website_card)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.remove -> {
                                    onInteractionListener.onRemove(website)
                                    true
                                }
                                R.id.edit -> {
                                    onInteractionListener.onEdit(website)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }

            }
        }
    }
}
