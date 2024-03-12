package com.example.passwordmanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
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

class WebsiteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WebsiteRepository = WebsiteRepositorySharedPrefsImpl(application)
    val data = repository.getAll()
    private val edited = MutableLiveData(empty)

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