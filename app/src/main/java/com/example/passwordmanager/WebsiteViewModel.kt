package com.example.passwordmanager

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

private val empty = Website(
    id = 0,
    name = "",
    login = "",
    password = "",
    dateOfAdding = "",
    description = "",
    url = "",
    iconURL = "",
)

class WebsiteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WebsiteRepository = WebsiteRepositorySharedPrefsImpl(application)
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

   /* fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value? == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }*/ // todo

    fun removeById(id: Long) = repository.removeById(id)
}