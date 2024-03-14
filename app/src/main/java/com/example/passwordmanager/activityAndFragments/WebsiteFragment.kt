package com.example.passwordmanager.activityAndFragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import com.example.passwordmanager.R
import com.example.passwordmanager.adapter.OnInteractionListenerWebsites
import com.example.passwordmanager.adapter.WebsiteAdapter
import com.example.passwordmanager.adapter.WebsiteDetailAdapter
import com.example.passwordmanager.crypto.EncryptionManager
import com.example.passwordmanager.databinding.WebsiteFragmentBinding
import com.example.passwordmanager.dto.Website
import com.example.passwordmanager.viewModel.WebsiteViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.Args

class WebsiteFragment: Fragment() {
    private val encryptionManager: EncryptionManager = EncryptionManager()

    private val viewModel: WebsiteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val args by navArgs<WebsiteFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.inflateMenu(R.menu.options_for_website_card)


       /* toolbar?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.edit -> {
                    // Обработка нажатия на кнопку "Редактировать"
                    //findNavController().navigate(R.id.action_websiteFragment_to_editWebsiteFragment)
                    true
                }
                R.id.remove -> {
                    // Обработка нажатия на кнопку "Удалить"
                    //actionBar?.setDisplayHomeAsUpEnabled(false)
                    //findNavController().navigate(R.id.action_websiteFragment_to_websiteListFragment)
                    // Вызов метода удаления сайта
                    true
                }
                else -> false
            }
        }*/

    }

}