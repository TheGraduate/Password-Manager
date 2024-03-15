package com.example.passwordmanager.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.SiteCardBinding
import com.example.passwordmanager.dto.Website
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URI
import java.net.UnknownHostException


interface OnInteractionListenerWebsites {
    fun onWebsite(website: Website) {}
    fun onRemove(website: Website) {}
    fun onEdit(website: Website) {}
}

class WebsiteAdapter(
    private val onInteractionListener: OnInteractionListenerWebsites,
) : ListAdapter<Website, WebsiteAdapter.WebsiteViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebsiteViewHolder {
        val binding = SiteCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WebsiteViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: WebsiteViewHolder, position: Int) {
        val webSite = getItem(position)
        holder.bind(webSite)
    }

    class WebsiteViewHolder(
        private val binding: SiteCardBinding,
        private val onInteractionListener: OnInteractionListenerWebsites,
        private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(website: Website) {
            binding.apply {
                binding.nameTextWebsite.text = website.name
                binding.descriptionTextWebsite.text = website.description
                binding.dateTextWebsite.text = website.dateOfAdding

                root.setOnClickListener {
                    onInteractionListener.onWebsite(website)
                }

                coroutineScope.launch {
                    val requestOptions = RequestOptions()
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .disallowHardwareConfig()

                    var bitmap: Bitmap? = null

                    try {
                        val uri = URI(website.url)
                        val domain = uri.host ?: website.url
                        val fullUrl = if (uri.scheme == null) "https://$domain" else website.url
                        bitmap = withContext(Dispatchers.IO) {
                            Glide.with(itemView.context)
                                .asBitmap()
                                .load("$fullUrl/favicon.ico")
                                .submit()
                                .get()
                        }
                    } catch (e: UnknownHostException) {
                        e.printStackTrace()
                        bitmap = null
                    } catch (e: IOException) {
                        e.printStackTrace()
                        bitmap = null
                    } catch (e: Exception) {
                        e.printStackTrace()
                        bitmap = null
                    }

                    binding.iconImageWebsite.post {
                        bitmap?.let {
                            Glide.with(binding.iconImageWebsite)
                                .setDefaultRequestOptions(requestOptions)
                                .load(it)
                                .placeholder(R.drawable.placeholer_website_icon_image)
                                .error(R.drawable.icon_site_error)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .signature(ObjectKey(website.url))
                                .circleCrop()
                                .into(binding.iconImageWebsite)
                        }
                    }
                }

                iconImageWebsiteMenu.setOnClickListener {
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

class PostDiffCallback : DiffUtil.ItemCallback<Website>() {
    override fun areItemsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Website, newItem: Website): Boolean {
        return oldItem == newItem
    }
}
