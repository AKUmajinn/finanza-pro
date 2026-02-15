package com.example.finanzapro.model

data class Ticket(
    val id: String = "",
    val merchant: String = "Comercio",
    val amount: Double = 0.0,
    val categoryId: String = "cat_others",
    val status: String = "",
    val uploadDate: Long = 0,
    val imageUrl: String = ""
)