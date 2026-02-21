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
import java.util.UUID

class TransactionViewModel: ViewModel() {

    private val database = Firebase.database
    private val transactionsRef = database.getReference("users/")

    private var _listTransactions = MutableLiveData<List<Transaction>>(emptyList())
    val listTransactions: LiveData<List<Transaction>> = _listTransactions

    fun getTransactions(user: String, table: String) {
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val snapshot = transactionsRef.child("$user/$table").get().await()
                val transactions = mutableListOf<Transaction>()

                snapshot.children.forEach { child ->
                    child.getValue(Transaction::class.java)?.let { transaction ->
                        transaction.id = child.key ?: ""
                        transactions.add(transaction)
                    }
                }

                //orden decensdente
                transactions.sortByDescending { it.timestamp }

                _listTransactions.postValue(transactions)
            } catch (e: Exception) {
                e.printStackTrace()
                _listTransactions.postValue(emptyList())
            }
        }
    }

    fun saveTransactions(transaction: Transaction, user: String, table: String) {
        transaction.id = UUID.randomUUID().toString()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionsRef.child(user + "/" + table + "/" + transaction.id).setValue(transaction).await()
                getTransactions(user, table)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionsRef.child(transaction.id).setValue(transaction).await()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteTransaction(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                transactionsRef.child(id).removeValue().await()
                _listTransactions.postValue(_listTransactions.value?.filter { it.id != id })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}