package com.example.passwordmanager.activityAndFragments

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.passwordmanager.util.AndroidUtils
import com.example.passwordmanager.viewModel.WebsiteViewModel

import com.example.passwordmanager.databinding.FragmentWebsiteCreateBinding

class WebsiteCreateFragment : Fragment() {

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

        binding.showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.enterPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                binding.enterPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        binding.buttonAddWebsite.setOnClickListener {
            if (binding.enterWebsiteName.text.isNotEmpty() && binding.enterLogin.text.isNotEmpty() && binding.enterPassword.text.isNotEmpty()
                && binding.enterURL.text.isNotEmpty()
            ) {
                viewModel.changeWebsiteName(binding.enterWebsiteName.text.toString())
                viewModel.changeLogin(binding.enterLogin.text.toString())
                viewModel.changePassword(binding.enterPassword.text.toString())
                viewModel.changeURL(binding.enterURL.text.toString())
                viewModel.changeDescription(binding.enterDescription.text.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
                actionBar?.setDisplayHomeAsUpEnabled(false)
            } else {
                Toast.makeText(requireContext(), "Fields: website name, login, password, url - can not be empty  ", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}