package com.example.passwordmanager.repository

import androidx.lifecycle.LiveData
import com.example.passwordmanager.dto.Website


interface WebsiteRepository {
    fun getAll(): LiveData<List<Website>>
    fun save(website: Website)
    fun removeById(id: Long)
}