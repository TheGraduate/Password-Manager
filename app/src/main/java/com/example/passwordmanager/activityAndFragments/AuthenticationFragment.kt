package com.example.passwordmanager.activityAndFragments

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
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
import com.example.passwordmanager.R
import com.example.passwordmanager.crypto.EncryptionManager

class AuthenticationFragment : Fragment() {

    private val encryptionManager: EncryptionManager = EncryptionManager()
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private var firstTimeLogin: Boolean
        get() {
            val sharedPreferences = requireContext().getSharedPreferences("first_time_login_prefs", Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("first_time_login", true)
        }
        set(value) {
            val sharedPreferences = requireContext().getSharedPreferences("first_time_login_prefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("first_time_login", value)
            editor.apply()
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.authentication_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firstTimeLogin = firstTimeLogin

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

            if (firstTimeLogin) {
                savePassword(passwordInput)
                firstTimeLogin = false
                showToast(getString(R.string.password_saved))
                navigateToMainScreen()
            } else {
                val savedPassword = getSavedPassword()
                if (passwordInput == savedPassword) {
                    navigateToMainScreen()
                } else {
                    showToast(getString(R.string.password_wrong))
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    fun getSavedPassword(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE)
        val encryptedDataFromSharedPrefs = Base64.decode(sharedPreferences.getString("password", null), Base64.DEFAULT)
        val decryptedData = encryptedDataFromSharedPrefs?.let { encryptionManager.decryptData(it) }
        return decryptedData?.toString(Charsets.UTF_8)
    }

    fun savePassword(password: String) {
        val sharedPreferences = requireContext().getSharedPreferences("password_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val encryptedDataToSharedPrefs = encryptionManager.encryptData(password.toByteArray(Charsets.UTF_8))
        editor.putString("password", Base64.encodeToString(encryptedDataToSharedPrefs, Base64.DEFAULT))
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
                    showToast(getString(R.string.do_not_recognize_fingerprint))
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.log_in_to_the_app_biometrically))
            .setSubtitle(getString(R.string.use_your_fingerprint_to_log_in))
            .setNegativeButtonText(getString(R.string.biometric_dialog_cancel))
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