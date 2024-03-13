package com.example.passwordmanager.crypto

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec

/*
class EncryptionManager(private val context: Context) {

    companion object {
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
        private const val KEY_ALIAS = "key_alias"
        private const val IV_SIZE = 16 // Размер вектора инициализации
    }

    init {
        generateKey()
    }

    fun encryptData(data: ByteArray): ByteArray {
        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val iv = cipher.iv
        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, cipher)
        cipherOutputStream.write(data)
        cipherOutputStream.close()
        return iv + outputStream.toByteArray() // Присоединяем IV к зашифрованным данным
    }

    fun decryptData(data: ByteArray): ByteArray {
       */
/* val iv = encryptedData.copyOfRange(0, IV_SIZE) // Получаем IV из зашифрованных данных
        val cipher = getCipher(Cipher.DECRYPT_MODE, iv)
        val inputStream = ByteArrayInputStream(encryptedData, IV_SIZE, encryptedData.size - IV_SIZE)
        val cipherInputStream = CipherInputStream(inputStream, cipher)
        val buffer = ByteArrayOutputStream()
        cipherInputStream.copyTo(buffer)
        return buffer.toByteArray()*//*

        val cipher = getCipher(Cipher.ENCRYPT_MODE)
        val iv = cipher.iv // Получаем IV, сгенерированный внутри getCipher
        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, cipher)
        cipherOutputStream.write(data)
        cipherOutputStream.close()
        return iv + outputStream.toByteArray() // При
    }

    private fun getCipher(mode: Int, iv: ByteArray? = null): Cipher {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        val key = keyStore.getKey(KEY_ALIAS, null) as SecretKey
        val cipher = Cipher.getInstance(TRANSFORMATION)
        if (mode == Cipher.ENCRYPT_MODE) {
            val secureRandom = SecureRandom()
            val ivParameterSpec = ByteArray(IV_SIZE)
            secureRandom.nextBytes(ivParameterSpec)
            cipher.init(mode, key, IvParameterSpec(ivParameterSpec))
        } else {
            cipher.init(mode, key, IvParameterSpec(iv))
        }
        return cipher
    }

    private fun generateKey() {
        if (!isKeyExists()) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .build()
            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    private fun isKeyExists(): Boolean {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        keyStore.load(null)
        return keyStore.containsAlias(KEY_ALIAS)
    }
}*/
