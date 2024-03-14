package com.example.passwordmanager.viewModel

import android.app.Application
import android.content.Context
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.passwordmanager.crypto.EncryptionManager
import com.example.passwordmanager.dto.Website
import com.example.passwordmanager.repository.WebsiteRepository
import com.example.passwordmanager.repository.WebsiteRepositorySharedPrefsImpl

private val empty = Website(
    id = 0,
    name = "",
    login = "",
    password = "",
    dateOfAdding = "",
    description = "",
    url = "",
    //iconURL = "",//todo delete
)

/*val sharedPreferences1 = context?.getSharedPreferences("my_prefs2", Context.MODE_PRIVATE)
val editor = sharedPreferences1?.edit()
val myString = binding.text.text.toString()
val encryptedData = encryptionManager.encryptData(myString.toByteArray(Charsets.UTF_8))
editor?.putString("my_key2", Base64.encodeToString(encryptedData, Base64.DEFAULT))
editor?.apply()
val myString2 = sharedPreferences1?.getString("my_key2", "")
val encryptedDataFromSharedPrefs = Base64.decode(myString2, Base64.DEFAULT)
val decryptedData = encryptedDataFromSharedPrefs?.let { encryptionManager.decryptData(it) }
binding.text.text = decryptedData?.toString(Charsets.UTF_8)*/

class WebsiteViewModel(application: Application) : AndroidViewModel(application) {
    private val encryptionManager = EncryptionManager()
    private val repository: WebsiteRepository = WebsiteRepositorySharedPrefsImpl(application, encryptionManager)
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = empty
    }

    fun edit(website: Website) {
        edited.value = website
    }

    fun changeDescription(description: String) {
        val text = description.trim()
        if (edited.value?.description == text) {
            return
        }
        edited.value = edited.value?.copy(description = text)
    }

    fun changeWebsiteName(name: String) {
        val text = name.trim()
        if (edited.value?.name == text) {
            return
        }
        edited.value = edited.value?.copy(name = text)
    }

    fun changePassword(password: String) {
        val text = password.trim()
        if (edited.value?.password == text) {
            return
        }
        edited.value = edited.value?.copy(password = text)
    }

    fun changeLogin(login: String) {
        val text = login.trim()
        if (edited.value?.login == text) {
            return
        }
        edited.value = edited.value?.copy(login = text)
    }

    fun changeURL(url: String) {
        val text = url.trim()
        if (edited.value?.url == text) {
            return
        }
        edited.value = edited.value?.copy(url = text)
    }

    fun removeById(id: Long) = repository.removeById(id)
}