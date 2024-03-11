package com.example.passwordmanager

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.io.IOException

class WebsiteRepositorySharedPrefsImpl(
    context: Context,
) : WebsiteRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("rep", Context.MODE_PRIVATE)
    private val type = TypeToken.getParameterized(List::class.java, Website::class.java).type
    private val key = "posts"
    private var nextId = 0L//1L
    private var websites = emptyList<Website>()
    private val data = MutableLiveData(websites)

    init {
        prefs.getString(key, null)?.let {
            websites = gson.fromJson(it, type)
            nextId = websites.maxByOrNull { it.id }?.id?.plus(1) ?: 1L
            data.value = websites
        }
    }

    override fun getAll(): LiveData<List<Website>> = data

    override fun save(website: Website) {
        if (website.id == 0L) {
            websites = listOf(
                website.copy(
                    id = nextId++,
                    dateOfAdding = "now"
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