package com.example.passwordmanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.example.passwordmanager.databinding.FragmentWebsiteCreateBinding

class WebsiteCreateFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private var _binding: FragmentWebsiteCreateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebsiteCreateBinding.inflate(inflater, container, false)


        binding.buttonAddWebsite.setOnClickListener {
            //viewModel.changeContent(binding.edit.text.toString())
            //viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()
            //val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            //actionBar?.setDisplayHomeAsUpEnabled(false)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}