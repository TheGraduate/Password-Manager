package com.example.passwordmanager.activityAndFragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passwordmanager.R
import com.example.passwordmanager.adapter.OnInteractionListenerWebsites
import com.example.passwordmanager.adapter.WebsiteDetailAdapter
import com.example.passwordmanager.databinding.WebsiteFragmentBinding
import com.example.passwordmanager.dto.Website
import com.example.passwordmanager.viewModel.WebsiteViewModel

class WebsiteFragment: Fragment() {

    private val viewModel: WebsiteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val args by navArgs<WebsiteFragmentArgs>()

    private lateinit var webView: WebView
    private lateinit var autoFillWebViewHelper: AutoFillWebViewHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = WebsiteFragmentBinding.inflate(inflater, container, false)
        val viewHolder = WebsiteDetailAdapter.WebsiteDetailViewHolder(binding, object : OnInteractionListenerWebsites {
                override fun onEdit(website: Website) {
                    viewModel.edit(website)
                }

                override fun onRemove(website: Website) {
                    viewModel.removeById(website.id)
                }
            })

        viewModel.data.observe(viewLifecycleOwner) { websites ->
            val website = websites.find { it.id == args.id } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(website)
        }

        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                return@observe
            }

            val action = WebsiteFragmentDirections.actionWebsiteFragmentToWebsiteEditFragment (website = it)
            findNavController().navigate(action)
        }

        binding.buttonCopyLogin.setOnClickListener {
            val textToCopy = viewModel.data.value?.find { it.id == args.id }?.login?: return@setOnClickListener
            val context = requireContext()
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Label", textToCopy)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, getString(R.string.login_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }

        binding.buttonCopyPassword.setOnClickListener {
            val textToCopy = viewModel.data.value?.find { it.id == args.id }?.password?: return@setOnClickListener
            val context = requireContext()
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Label", textToCopy)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, getString(R.string.password_copied_to_clipboard), Toast.LENGTH_SHORT).show()
        }

        binding.webViewContainer.visibility = View.INVISIBLE
        webView = WebView(requireContext())
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        binding.webViewContainer.addView(webView)
        binding.buttonAutofill.setOnClickListener {
            binding.webViewContainer.visibility = View.VISIBLE
            startAutoFillProcess()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.inflateMenu(R.menu.options_for_website_card)
    }

    private fun startAutoFillProcess() {
        autoFillWebViewHelper = AutoFillWebViewHelper(requireContext(), webView)
        val websiteUrl = viewModel.data.value?.find { it.id == args.id }?.url
        if (!websiteUrl.isNullOrEmpty()) {
            autoFillWebViewHelper.loadUrl(websiteUrl)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        webView.apply {
            stopLoading()
            destroy()
        }
    }
}