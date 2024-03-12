package com.example.passwordmanager.activityAndFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.passwordmanager.R
import com.example.passwordmanager.adapter.OnInteractionListenerWebsites
import com.example.passwordmanager.adapter.WebsiteAdapter
import com.example.passwordmanager.adapter.WebsiteDetailAdapter
import com.example.passwordmanager.databinding.WebsiteFragmentBinding
import com.example.passwordmanager.dto.Website
import com.example.passwordmanager.viewModel.WebsiteViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.Args

class WebsiteFragment: Fragment() {

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

        /*viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id == 0L) {
                return@observe
            }// todo переход от карточки сайта к редактированию

            val sendPostText = Bundle()
            sendPostText.putString(Intent.EXTRA_TEXT, it.content)
            val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(false)
            findNavController().navigate(R.id.action_postFragment_to_editPostFragment, sendPostText)
        }*/

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionBar = (activity as? AppCompatActivity)?.supportActionBar

        //val toolbar: Toolbar = view.findViewById(R.id.toolbar)
       // toolbar.setNavigationOnClickListener {
            //actionBar?.setDisplayHomeAsUpEnabled(false)
            //findNavController().navigateUp()
        //}

    }

}