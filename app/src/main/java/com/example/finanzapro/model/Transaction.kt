package com.example.finanzapro.model

data class Transaction(
    var id: String = "",
    var amount: Double = 0.0,
    var description: String = "",
    var category: String = "",
    var date: String = "",
    var hour: String = "",
    var paymentMethod: String = "",

    var type: String = "",
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id,
            "amount" to amount.toString(),
            "description" to description,
            "category" to category,
            "date" to date,
            "hour" to hour,
            "paymentMethod" to paymentMethod,
            "type" to type
        )
    }
}