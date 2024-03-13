package com.example.passwordmanager.activityAndFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
import com.example.passwordmanager.databinding.WebsiteEditFragmentBinding
import com.example.passwordmanager.databinding.WebsiteFragmentBinding
import com.example.passwordmanager.dto.Website
import com.example.passwordmanager.util.AndroidUtils
import com.example.passwordmanager.viewModel.WebsiteViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.Args

class WebsiteEditFragment: Fragment() {

    private val viewModel: WebsiteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private val args by navArgs<WebsiteEditFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = WebsiteEditFragmentBinding.inflate(inflater, container, false)

        viewModel.data.observe(viewLifecycleOwner) { websites ->
            val website = websites.find { it.id == args.website.id } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewModel.edited.value = website
        }

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)

        //val args: WebsiteEditFragmentArgs by navArgs()
        val website: Website = args.website

        binding.editsWebsiteName.setText(website.name)
        binding.editWebsiteLogin.setText(website.login)
        binding.editWebsitePassword.setText(website.password)
        binding.editWebsiteURL.setText(website.url)
        binding.editWebsiteDescription.setText(website.description)

        binding.buttonEditWebsite.setOnClickListener {
            viewModel.changeWebsiteName(binding.editsWebsiteName.text.toString())
            viewModel.changeLogin(binding.editWebsiteLogin.text.toString())
            viewModel.changePassword(binding.editWebsitePassword.text.toString())
            viewModel.changeURL(binding.editWebsiteURL.text.toString())
            viewModel.changeDescription(binding.editWebsiteDescription.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
            //val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            //actionBar?.setDisplayHomeAsUpEnabled(false)
        }

        /*viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                return@observe
            }// todo переход от карточки сайта к редактированию


            val action = WebsiteListFragmentDirections.action(it.id)
            requireParentFragment().findNavController().navigate(action)
        }*/




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = activity?.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
        //val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        //actionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar?.inflateMenu(R.menu.options_for_website_card)

        val navController = findNavController()
        toolbar?.setNavigationOnClickListener {
            navController.navigateUp()
        }

    }

}