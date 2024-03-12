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


class WebsiteListFragment : Fragment() {

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
    ): View? {

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar
        //actionBar?.setDisplayHomeAsUpEnabled(false)

        _binding = FragmentWebsiteListBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            actionBar?.setDisplayHomeAsUpEnabled(true)
            findNavController().navigate(R.id.action_websiteListFragment_to_websiteCreateFragment)
            //val action =  WebsiteListFragmentDirections.actionWebsiteCreateFragmentToWebsiteCreateFragment()
            //requireParentFragment().findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemList = listOf(// todo delete this
            Website(1, "Google", "dfdsfdsf", "12345", "25.03.24", "eto googol", "https://www.google.com"),
            Website(2, "Google2", "dsfdsfdsf", "12345","25.03.24", "eto googol","https://www.google.com"),
            Website(3, "Google3", "sdfsdf", "12345","25.03.24", "eto googol","https://www.google.com"),
            Website(4, "Google4", "dsfdsf", "12345","25.03.24", "eto googol","https://www.google.com"),
            Website(5, "Google5", "fsdfdsf", "12345","25.03.24", "eto googol","https://www.google.com"),
            Website(6, "Google6", "dsfdsf", "12345","25.03.24", "eto googol","https://www.google.com"),
            // Add more items as needed
        )
        val adapter = WebsiteAdapter(object : OnInteractionListenerWebsites {
            val actionBar = (activity as? AppCompatActivity)?.supportActionBar
            override fun onWebsite(website: Website) {
                //actionBar?.setDisplayHomeAsUpEnabled(false)
                val action = WebsiteListFragmentDirections.actionWebsiteListFragmentToWebsiteFragment(website.id)
                requireParentFragment().findNavController().navigate(action)
                //findNavController().navigate(R.id.action_websiteListFragment_to_websiteFragment)
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

        //adapter.submitList(itemList)
    }

}