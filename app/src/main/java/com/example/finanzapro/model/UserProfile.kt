package com.example.finanzapro.model
data class UserProfile(
    val name: String = "",
    val email: String = "",
    val currency: String = "PEN",
    val monthly_budget: Double = 0.0
)