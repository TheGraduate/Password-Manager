package com.example.passwordmanager.adapter

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.caverock.androidsvg.SVG
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.SiteCardBinding
import com.example.passwordmanager.databinding.WebsiteFragmentBinding
import com.example.passwordmanager.dto.Website
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.io.IOException
import java.net.URL
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.Locale


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
        //private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    ) : RecyclerView.ViewHolder(binding.root) {

        private val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())// todo разобраться с форматом времени

        /*private fun getFaviconUrl(websiteUrl: String): String? {
            try {
                val document = Jsoup.connect(websiteUrl).get()
                val faviconElement = document.select("link[rel~=(?i)icon]").first()

                if (faviconElement != null) {
                    return faviconElement.absUrl("href")
                }
                val shortcutIconElement = document.select("link[rel~=(?i)shortcut icon]").first()

                return shortcutIconElement?.absUrl("href")
            }catch (e: UnknownHostException) {
                  e.printStackTrace()
             } catch (e: IOException) {
                    e.printStackTrace()
              } catch (e: Exception) {
                     e.printStackTrace()
              }

            return null
        }*/ //todo delete

        fun bind(website: Website) {
            binding.apply {
                binding.UsersWebsiteName.text = website.name
                binding.UsersWebsiteLogin.text = website.login
                binding.UsersWebsitePassword.text = website.password
                binding.UsersWebsiteURL.text = website.url
                binding.UsersWebsiteDescription.text = website.description
            }
        }
    }
}
