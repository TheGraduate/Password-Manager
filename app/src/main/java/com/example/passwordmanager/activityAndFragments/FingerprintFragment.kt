package com.example.passwordmanager.activityAndFragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.biometric.BiometricPrompt
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.passwordmanager.R

class FingerprintFragment : Fragment() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fingerprint_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Проверяем разрешение на использование биометрии
        //if (checkBiometricPermission()) {
            // Если есть разрешение, создаем BiometricPrompt
            createBiometricPrompt()
            showBiometricPrompt()
        //} else {
            // Если нет разрешения, запрашиваем его
            //requestBiometricPermission()
       // }

        // Настройка кнопки для запуска аутентификации по отпечатку пальца


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
                    // Обработка ошибки аутентификации
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val action = FingerprintFragmentDirections.actionFingerprintFragmentToWebsiteListFragment ()
                    findNavController().navigate(action)
                    // Аутентификация успешна, выполняем нужные действия
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    // Аутентификация не удалась
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Вход в приложение")
            .setSubtitle("Используйте отпечаток пальца для входа")
            .setNegativeButtonText("Отмена")
            .build()
    }

    private fun showBiometricPrompt() {
        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        private const val REQUEST_BIOMETRIC_PERMISSION = 100
    }
}