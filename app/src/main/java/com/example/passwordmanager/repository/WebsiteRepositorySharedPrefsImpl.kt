package com.example.passwordmanager.repository

import android.content.Context
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.passwordmanager.crypto.EncryptionManager
import com.example.passwordmanager.dto.Website

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WebsiteRepositorySharedPrefsImpl(
    context: Context,
    private val encryptionManager: EncryptionManager
) : WebsiteRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("rep", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Website::class.java).type
    private val key = "posts"
    private var nextId = 0L
    private var websites = emptyList<Website>()
    private val data = MutableLiveData(websites)


    /*init {
        prefs.getString(key, null)?.let { json ->
            websites = gson.fromJson<List<Website>>(json, type).map { website ->
                website.copy(password = encryptionManager.decryptData(Base64.decode(website.password, Base64.DEFAULT)).toString(Charsets.UTF_8))
            }
            nextId = websites.maxByOrNull { it.id }?.id?.plus(1) ?: 1L
            data.value = websites
        }
    }*/

    override fun getAll(): LiveData<List<Website>> = data

    override fun save(website: Website) {
        val encryptedPassword = encryptionManager.encryptData(website.password.toByteArray(Charsets.UTF_8))
        //val encodedPassword = Base64.encodeToString(encryptedPassword, Base64.DEFAULT)//

        if (website.id == 0L) {
            websites = listOf(
                website.copy(
                    id = nextId++,
                    dateOfAdding = "now",
                    password = Base64.encodeToString(encryptedPassword, Base64.DEFAULT)
                )
            ) + websites
            data.value = websites
            sync()
            return
        }

        websites = websites.map {
            if (it.id != website.id) it else it.copy(
                name = website.name,
                login = website.login,
                password = website.password,
                url = website.url,
                description = website.description
            )
        }
        data.value = websites
        sync()
    }

    override fun removeById(id: Long) {
        websites = websites.filter { it.id != id }
        data.value = websites
        sync()
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(websites))
            apply()
        }
    }
}