package com.example.passwordmanager.activityAndFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.passwordmanager.util.AndroidUtils
import com.example.passwordmanager.util.StringArg
import com.example.passwordmanager.viewModel.WebsiteViewModel

import com.example.passwordmanager.databinding.FragmentWebsiteCreateBinding

class WebsiteCreateFragment : Fragment() {

    companion object {
        var Bundle.textArg: String? by StringArg
    }

    private val viewModel: WebsiteViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    private var _binding: FragmentWebsiteCreateBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebsiteCreateBinding.inflate(inflater, container, false)

        val actionBar = (requireActivity() as MainActivity).supportActionBar

        binding.buttonAddWebsite.setOnClickListener {
            viewModel.changeWebsiteName(binding.enterWebsiteName.text.toString())
            viewModel.changeLogin(binding.enterLogin.text.toString())
            viewModel.changePassword(binding.enterPassword.text.toString())
            viewModel.changeURL(binding.enterURL.text.toString())
            viewModel.changeDescription(binding.enterDescription.text.toString())
            viewModel.save()
            AndroidUtils.hideKeyboard(requireView())
            findNavController().navigateUp()


            actionBar?.setDisplayHomeAsUpEnabled(false)
            //val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
            //actionBar?.setDisplayHomeAsUpEnabled(false)
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}