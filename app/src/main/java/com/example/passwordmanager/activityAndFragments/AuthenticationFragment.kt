package com.example.passwordmanager.activityAndFragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.passwordmanager.R
import com.example.passwordmanager.databinding.FragmentWebsiteListBinding

class AuthenticationFragment : Fragment() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var firstTimeLogin = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.authentication_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val enterByPassButton = view.findViewById<Button>(R.id.enterByPasswordButton)
        val enterByFingerprintButton = view.findViewById<Button>(R.id.enterByFingerprintButton)

        enterByPassButton?.setOnClickListener {
            showDialog()
        }

        enterByFingerprintButton?.setOnClickListener {
            if (checkBiometricPermission()) {
                createBiometricPrompt()
                showBiometricPrompt()
            } else {
                requestBiometricPermission()
            }
        }
    }

    private fun showDialog() {
        val dialogView = layoutInflater.inflate(R.layout.enter_by_pass_dialog_layout, null)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        val confirmButton = dialogView.findViewById<Button>(R.id.dialog_confirm_button)
        confirmButton.setOnClickListener {

            val passwordInput = dialogView.findViewById<EditText>(R.id.password_input).text.toString()
            val savedPassword = getSavedPassword()

            if (firstTimeLogin) {
                savePassword(passwordInput)
                firstTimeLogin = false
                showToast("Password saved")
                navigateToMainScreen()
            } else {
                if (passwordInput == savedPassword) {
                    navigateToMainScreen()
                } else {
                    showToast("Wrong password")
                }
            }

            dialog.dismiss()
        }
        dialog.show()
    }

    private fun getSavedPassword(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("password", null)
    }

    private fun savePassword(password: String) {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("password", password)
        editor.apply()
    }

    private fun checkBiometricPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.USE_BIOMETRIC
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestBiometricPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.USE_BIOMETRIC),
            REQUEST_BIOMETRIC_PERMISSION
        )
    }

    private fun createBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(
            requireActivity(),
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    saveFingerprintAuthenticationStatus()
                    navigateToMainScreen()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast("Do not recognize fingerprint")
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Log in to the app")
            .setSubtitle("Use your fingerprint to log in")
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    private fun saveFingerprintAuthenticationStatus() {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("fingerprint_authenticated", true)
        editor.apply()
    }

    private fun navigateToMainScreen() {
        val action = AuthenticationFragmentDirections.actionAuthenticationFragmentToWebsiteListFragment()
        requireActivity().findNavController(R.id.nav_host_fragment).navigate(action)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_BIOMETRIC_PERMISSION = 100
    }
}