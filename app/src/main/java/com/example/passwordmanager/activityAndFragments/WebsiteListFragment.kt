package com.example.passwordmanager.activityAndFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.passwordmanager.adapter.OnInteractionListenerWebsites
import com.example.passwordmanager.R
import com.example.passwordmanager.dto.Website
import com.example.passwordmanager.adapter.WebsiteAdapter
import com.example.passwordmanager.viewModel.WebsiteViewModel
import com.example.passwordmanager.databinding.FragmentWebsiteListBinding

class WebsiteListFragment() : Fragment() {

    private val viewModel: WebsiteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var _binding: FragmentWebsiteListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        _binding = FragmentWebsiteListBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            actionBar?.setDisplayHomeAsUpEnabled(true)
            findNavController().navigate(R.id.action_websiteListFragment_to_websiteCreateFragment)
        }

        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                return@observe
            }

            val action = WebsiteListFragmentDirections.actionWebsiteListFragmentToWebsiteEditFragment(website = it)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = WebsiteAdapter(object : OnInteractionListenerWebsites {
            override fun onWebsite(website: Website) {
                val action = WebsiteListFragmentDirections.actionWebsiteListFragmentToWebsiteFragment(website.id)
                requireParentFragment().findNavController().navigate(action)
            }
            override fun onEdit(website: Website) {
                viewModel.edit(website)
            }
            override fun onRemove(website: Website) {
                viewModel.removeById(website.id)
            }
        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        viewModel.data.observe(viewLifecycleOwner) { websites ->
            adapter.submitList(websites)
        }

    }

}