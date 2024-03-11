package com.example.passwordmanager

import android.app.LauncherActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.passwordmanager.databinding.FragmentWebsiteListBinding
import com.google.android.material.appbar.MaterialToolbar
import java.util.Date


class WebsiteListFragment : Fragment() {
    private var _binding: FragmentWebsiteListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWebsiteListBinding.inflate(inflater, container, false)

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_websiteListFragment_to_websiteCreateFragment)
            //val action =  WebsiteListFragmentDirections.actionWebsiteCreateFragmentToWebsiteCreateFragment()
            //requireParentFragment().findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemList = listOf(
            Website(1, "Google", "dfdsfdsf", "12345", "25.03.24", "eto googol", "https://www.google.com","https://www.google.com"),
            Website(2, "Google2", "dsfdsfdsf", "12345","25.03.24", "eto googol","https://www.google.com","https://www.google.com"),
            Website(3, "Google3", "sdfsdf", "12345","25.03.24", "eto googol","https://www.google.com","https://www.google.com"),
            Website(4, "Google4", "dsfdsf", "12345","25.03.24", "eto googol","https://www.google.com","https://www.google.com"),
            Website(5, "Google5", "fsdfdsf", "12345","25.03.24", "eto googol","https://www.google.com","https://www.google.com"),
            Website(6, "Google6", "dsfdsf", "12345","25.03.24", "eto googol","https://www.google.com","https://www.google.com"),
            // Add more items as needed
        )
        val adapter = WebsiteAdapter(object : OnInteractionListenerWebsites {
            override fun onWebsite(website: Website) {

            }
        })

        adapter.submitList(itemList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())


    }

}