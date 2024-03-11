package com.example.passwordmanager

import java.util.Date

data class Website(
    val id: Long,
    val name: String,
    val login: String,
    val password: String,
    val dateOfAdding: String,
    val description: String,
    val url: String,
    val iconURL: String,
)