package com.example.finanzapro.model

data class Ticket(
    val id: String = "",
    val imageUrl: String = "",
    val relatedTransactionId: String = "",
    val status: String = "pending",
    val uploadDate: Long = 0L // para el formato de fechas me parece que es: 22/10/1995
)