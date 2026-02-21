package com.example.finanzapro.model

data class Transaction(
    var id: String = "",
    var amount: Double = 0.0,
    var description: String = "",
    var categoryId: String = "",
    var paymentMethod: String = "",
    var type: String = "",
    var imageUrl: String = "", //no borrar, voy a ver si añado la opcion de cargar fotos
    var timestamp: Long = 0L
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "id" to id,
            "amount" to amount.toString(),
            "description" to description,
            "categoryId" to categoryId,
            "paymentMethod" to paymentMethod,
            "type" to type,
            "imageUrl" to imageUrl,
            "timestamp" to timestamp.toString()
        )
    }
}