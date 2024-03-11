package com.example.passwordmanager

import androidx.lifecycle.LiveData


interface WebsiteRepository {
    fun getAll(): LiveData<List<Website>>
    fun save(website: Website)
    fun removeById(id: Long)
}