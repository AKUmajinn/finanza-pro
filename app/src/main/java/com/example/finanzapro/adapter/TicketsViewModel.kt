package com.example.finanzapro.adapter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finanzapro.model.Transaction
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TicketsViewModel : ViewModel() {
    private val database = Firebase.database
    private val transactionsRef = database.getReference("users/")

    private val _listTickets = MutableLiveData<List<Transaction>>(emptyList())
    val listTickets: LiveData<List<Transaction>> = _listTickets

    fun getTickets(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val snapshot = transactionsRef.child("$userId/transactions").get().await()
                val tickets = mutableListOf<Transaction>()

                snapshot.children.forEach { child ->
                    child.getValue(Transaction::class.java)?.let { transaction ->
                        transaction.id = child.key ?: ""
                        tickets.add(transaction)
                    }
                }

                tickets.sortByDescending { it.timestamp }

                _listTickets.postValue(tickets)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}